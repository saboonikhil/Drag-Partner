package com.drag.partner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.drag.partner.model.Location;
import com.drag.partner.model.Partner;
import com.drag.partner.network.APIUtils;
import com.drag.partner.network.EndPointInterface;
import com.drag.partner.util.ObjectSerializer;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class AddCarFragment extends DialogFragment {

    public static String TAG = "AddCarFragment";
    private Activity parentActivity;
    private View rootView;
    private Location[] locations;
    private String[] colleges;
    private SharedPreferences pref;
    private Partner partner;
    private ImageButton closeView;
    private TextView titleView;
    private Button saveView;
    private Switch switchDateView;
    private AutoCompleteTextView collegeNameView, pickupView, dropView;
    private EditText fromDateView, toDateView, dateView, timeView, seatsView, fareView, carNameView, carNumberView;
    private ImageButton swapLocationView;
    private LinearLayout fromToDateLayout, dateTimeLayout;
    private Button increaseSeatView, decreaseSeatView;
    private InputMethodManager imm;
    private Calendar fromDateCalendar, toDateCalendar, DateCalendar, TimeCalendar;
    private String collegeNameText, token, pickup, drop, collegeName, seats, fare, carName, carNumber;
    private long thirtyDays = 2592000000L;
    private int seatsText = 4;
    private boolean route = true;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_add_car, container, false);
        return rootView;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();

        pref = parentActivity.getSharedPreferences("AppPref", MODE_PRIVATE);
        token = pref.getString("token", "");
        String json = pref.getString("dbObj", "");
        partner = new Gson().fromJson(json, Partner.class);

        locations = (Location[]) ObjectSerializer.deserialize(pref.getString("locations",
                ObjectSerializer.serialize(new Location[10])));
        colleges = new String[locations.length];
        for (int i = 0; i < locations.length; i++)
            colleges[i] = locations[i].getCollegeName();

        imm = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);

        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        saveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCar();
            }
        });

        switchDateView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    titleView.setText(R.string.new_cars);
                    fromToDateLayout.setVisibility(View.VISIBLE);
                    dateTimeLayout.setVisibility(View.GONE);
                } else {
                    titleView.setText(R.string.new_car);
                    fromToDateLayout.setVisibility(View.GONE);
                    dateTimeLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        setupCollegeSpinner();
        collegeNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pickupView.setText("");
                dropView.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                collegeNameText = collegeNameView.getText().toString();
                selectLocation(collegeNameText, route);
            }
        });

        pickupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                imm.hideSoftInputFromWindow(pickupView.getWindowToken(), 0);
                if (TextUtils.isEmpty(collegeNameText)) {
                    collegeNameView.requestFocus();
                    collegeNameView.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                }
                return true;
            }
        });

        dropView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                imm.hideSoftInputFromWindow(dropView.getWindowToken(), 0);
                if (TextUtils.isEmpty(collegeNameText)) {
                    collegeNameView.requestFocus();
                    collegeNameView.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                }
                return true;
            }
        });

        swapLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(collegeNameText)) {
                    collegeNameView.requestFocus();
                    collegeNameView.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                    imm.hideSoftInputFromWindow(collegeNameView.getWindowToken(), 0);
                } else if ((TextUtils.isEmpty(pickupView.getText().toString())) && (TextUtils.isEmpty(dropView.getText().toString()))) {
                    pickupView.requestFocus();
                    pickupView.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                    imm.hideSoftInputFromWindow(pickupView.getWindowToken(), 0);
                } else {
                    route = !route;
                    Editable location = pickupView.getText();
                    pickupView.setText(dropView.getText());
                    dropView.setText(location);
                    selectLocation(collegeNameText, route);
                }
            }
        });

        setupFromToDatePicker();
        setupDateTimePicker();
        String numberAsString = "" + seatsText;
        seatsView.setText(numberAsString);

        increaseSeatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seatsText != 6) {
                    seatsText++;
                    seatsView.setText(String.valueOf(seatsText));
                }
            }
        });

        decreaseSeatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seatsText != 1) {
                    seatsText--;
                    seatsView.setText(String.valueOf(seatsText));
                }
            }
        });
    }

    private void selectLocation(String collegeSelected, boolean routeSelected) {
        boolean isCollegePresent = false;
        int position = 0;
        for (int i = 0; i < colleges.length; i++) {
            if (collegeSelected.equals(colleges[i])) {
                isCollegePresent = true;
                position = i;
            }
        }
        if (isCollegePresent) {
            setupPickupLocationSpinner(position, routeSelected);
            setupDropLocationSpinner(position, routeSelected);
        } else {
            pickupView.setKeyListener(null);
            dropView.setKeyListener(null);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupCollegeSpinner() {
        ArrayAdapter<String> collegeSpinnerAdapter = new ArrayAdapter<>(parentActivity,
                R.layout.support_simple_spinner_dropdown_item, colleges);
        collegeNameView.setAdapter(collegeSpinnerAdapter);
        collegeNameView.setKeyListener(null);
        collegeNameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((AutoCompleteTextView) view).showDropDown();
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupPickupLocationSpinner(int position, boolean routeSelected) {
        ArrayAdapter<String> pickupSpinnerAdapter;
        if (routeSelected) {
            pickupSpinnerAdapter = new ArrayAdapter<>(parentActivity,
                    R.layout.support_simple_spinner_dropdown_item, locations[position].getSetA());
        } else {
            pickupSpinnerAdapter = new ArrayAdapter<>(parentActivity,
                    R.layout.support_simple_spinner_dropdown_item, locations[position].getSetB());
        }
        pickupView.setAdapter(pickupSpinnerAdapter);
        pickupView.setKeyListener(null);
        pickupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                imm.hideSoftInputFromWindow(pickupView.getWindowToken(), 0);
                ((AutoCompleteTextView) v).showDropDown();
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupDropLocationSpinner(int position, boolean routeSelected) {
        ArrayAdapter<String> dropSpinnerAdapter;
        if (routeSelected) {
            dropSpinnerAdapter = new ArrayAdapter<>(parentActivity,
                    R.layout.support_simple_spinner_dropdown_item, locations[position].getSetB());
        } else {
            dropSpinnerAdapter = new ArrayAdapter<>(parentActivity,
                    R.layout.support_simple_spinner_dropdown_item, locations[position].getSetA());
        }
        dropView.setAdapter(dropSpinnerAdapter);
        dropView.setKeyListener(null);
        dropView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                imm.hideSoftInputFromWindow(dropView.getWindowToken(), 0);
                ((AutoCompleteTextView) view).showDropDown();
                return false;
            }
        });
    }

    private void setupFromToDatePicker() {
        fromDateCalendar = Calendar.getInstance();
        fromDateCalendar.add(Calendar.DATE, 1);
        toDateCalendar = Calendar.getInstance();
        toDateCalendar.add(Calendar.DATE, 2);

        final DatePickerDialog.OnDateSetListener fromDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fromDateCalendar.set(Calendar.YEAR, year);
                fromDateCalendar.set(Calendar.MONTH, monthOfYear);
                fromDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDate(fromDateView, fromDateCalendar);
                if (fromDateCalendar.getTimeInMillis() >= toDateCalendar.getTimeInMillis()) {
                    toDateView.getText().clear();
                    toDateCalendar.setTimeInMillis(fromDateCalendar.getTimeInMillis() + 86400000);
                }
            }
        };

        fromDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(fromDateView.getWindowToken(), 0);
                DatePickerDialog datePickerDialog = new DatePickerDialog(parentActivity, fromDate,
                        fromDateCalendar.get(Calendar.YEAR), fromDateCalendar.get(Calendar.MONTH), fromDateCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 86400000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + thirtyDays);
                datePickerDialog.show();
            }
        });

        final DatePickerDialog.OnDateSetListener toDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                toDateCalendar.set(Calendar.YEAR, year);
                toDateCalendar.set(Calendar.MONTH, monthOfYear);
                toDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDate(toDateView, toDateCalendar);
            }
        };

        toDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(toDateView.getWindowToken(), 0);
                DatePickerDialog datePickerDialog = new DatePickerDialog(parentActivity, toDate,
                        toDateCalendar.get(Calendar.YEAR), toDateCalendar.get(Calendar.MONTH), toDateCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(fromDateCalendar.getTimeInMillis() + 86400000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + thirtyDays);
                datePickerDialog.show();
            }
        });
    }

    private void setupDateTimePicker() {
        DateCalendar = Calendar.getInstance();
        DateCalendar.add(Calendar.DATE, 1);
        TimeCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DateCalendar.set(Calendar.YEAR, year);
                DateCalendar.set(Calendar.MONTH, monthOfYear);
                DateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDate(dateView, DateCalendar);
            }
        };

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(dateView.getWindowToken(), 0);
                DatePickerDialog datePickerDialog = new DatePickerDialog(parentActivity, date,
                        DateCalendar.get(Calendar.YEAR), DateCalendar.get(Calendar.MONTH), DateCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 86400000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + thirtyDays);
                datePickerDialog.show();
            }
        });

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                TimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                TimeCalendar.set(Calendar.MINUTE, minute);
                String displayFormat = new SimpleDateFormat("hh:mm a", Locale.US).format(TimeCalendar.getTime());
                timeView.setText(displayFormat);
            }
        };

        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(timeView.getWindowToken(), 0);
                TimePickerDialog mTimePicker = new TimePickerDialog(parentActivity, time,
                        TimeCalendar.get(Calendar.HOUR_OF_DAY), TimeCalendar.get(Calendar.MINUTE), false);
                mTimePicker.show();
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void saveCar() {
        collegeName = collegeNameView.getText().toString();
        seats = seatsView.getText().toString();
        fare = fareView.getText().toString();
        carName = carNameView.getText().toString();

        if (!TextUtils.isEmpty(pickupView.getText().toString())) {
            pickup = pickupView.getText().toString();
            for (Location location : locations) {
                if (collegeName.equals(location.getCollegeName())) {
                    for (String aList : location.getSetA()) {
                        if (pickup.equals(aList)) {
                            pickup = collegeName;
                        }
                    }
                }
            }
        }
        if (!TextUtils.isEmpty(dropView.getText().toString())) {
            drop = dropView.getText().toString();
            for (Location location : locations) {
                if (collegeName.equals(location.getCollegeName())) {
                    for (String aList : location.getSetA()) {
                        if (drop.equals(aList)) {
                            drop = collegeName;
                        }
                    }
                }
            }
        }
        if (!TextUtils.isEmpty(carNumberView.getText().toString())) {
            carNumber = carNumberView.getText().toString();
        }

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(collegeName)) {
            focusView = collegeNameView;
            cancel = true;
        } else if (TextUtils.isEmpty(fare)) {
            focusView = fareView;
            cancel = true;
        } else if (TextUtils.isEmpty(carName)) {
            focusView = carNameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            focusView.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
        } else {
            if (isConnectedToInternet()) {
                if (fromToDateLayout.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(fromDateView.getText()))
                        Toast.makeText(parentActivity, "Please select from date", Toast.LENGTH_SHORT).show();
                    else if (TextUtils.isEmpty(toDateView.getText()))
                        Toast.makeText(parentActivity, "Please select to date", Toast.LENGTH_SHORT).show();
                    else {
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Saving...");
                        progressDialog.show();
                        saveCars();
                    }
                } else {
                    if (TextUtils.isEmpty(dateView.getText()))
                        Toast.makeText(parentActivity, "Please select date", Toast.LENGTH_SHORT).show();
                    else {
                        Date date = DateCalendar.getTime();
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Saving...");
                        progressDialog.show();
                        if (!TextUtils.isEmpty(timeView.getText())) {
                            Date time = TimeCalendar.getTime();
                            String startTime = formatDateTime(date, time);
                            addCab(startTime);
                        } else {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(23400000);
                            Date time = calendar.getTime();
                            String startTime = formatDateTime(date, time);
                            addCab(startTime);
                        }
                    }
                }
            } else
                Snackbar.make(rootView, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }

    private void saveCars() {
        if (fromDateCalendar.before(toDateCalendar) || fromDateCalendar.equals(toDateCalendar)) {
            Date date = fromDateCalendar.getTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(23400000);
            Date time = calendar.getTime();
            String startTime = formatDateTime(date, time);
            addCab(startTime);
        } else {
            postOnResponse();
            Toast.makeText(parentActivity, "Cars added successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void addCab(String startTime) {
        EndPointInterface service = APIUtils.getAPIService();
        Call<Partner> call = service.addCab(
                partner.get_id(), partner.getEmail(), token, collegeName, pickup, drop, startTime, seats, fare, carName, carNumber);

        call.enqueue(new Callback<Partner>() {
            @Override
            public void onResponse(@NonNull Call<Partner> call, @NonNull Response<Partner> response) {
                if (response.body() != null) {
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("dbObj", new Gson().toJson(response.body()));
                    edit.apply();
                    if (fromToDateLayout.getVisibility() == View.GONE) {
                        postOnResponse();
                        Toast.makeText(parentActivity, "Car added successfully", Toast.LENGTH_SHORT).show();
                    } else if (response.isSuccessful()) {
                        fromDateCalendar.add(Calendar.DATE, 1);
                        saveCars();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Partner> call, @NonNull Throwable t) {
                progressDialog.cancel();
                Log.e(TAG + " On Failure", t.getMessage());
                Snackbar.make(rootView, "Something went wrong. Please try again later!", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void postOnResponse() {
        if (getFragmentManager() != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.main_content_frame, new CarsFragment());
            ft.commit();
        }
        progressDialog.cancel();
        dismiss();
    }

    private void setDate(EditText et, Calendar calendar) {
        String displayFormat = new SimpleDateFormat("EEE, MMM d", Locale.US).format(calendar.getTime());
        et.setText(displayFormat);
    }

    @SuppressLint("SimpleDateFormat")
    private String formatDateTime(Date date, Date time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String isoDate = df.format(date);

        DateFormat df1 = new SimpleDateFormat("HH:mm:ss.SSS");
        String isoTime = df1.format(time);

        String startDateTime = isoDate + 'T' + isoTime + 'Z';
        try {
            Calendar calendar = Calendar.getInstance();
            Date startTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(startDateTime);
            calendar.setTime(startTime);
            calendar.add(Calendar.HOUR, -5);
            calendar.add(Calendar.MINUTE, -30);
            startDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startDateTime;
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connMgr = (ConnectivityManager) parentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window dialogWindow = getDialog().getWindow();
        if (dialogWindow != null) {
            dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }
    }

    private void initViews() {
        closeView = rootView.findViewById(R.id.add_car_close);
        titleView = rootView.findViewById(R.id.add_car_title);
        saveView = rootView.findViewById(R.id.add_car_save);
        collegeNameView = rootView.findViewById(R.id.add_car_college_name);
        switchDateView = rootView.findViewById(R.id.add_car_switch_date);
        pickupView = rootView.findViewById(R.id.add_car_pickup);
        dropView = rootView.findViewById(R.id.add_car_drop);
        swapLocationView = rootView.findViewById(R.id.add_car_swap_location);
        fromToDateLayout = rootView.findViewById(R.id.add_car_from_to_date_layout);
        fromDateView = rootView.findViewById(R.id.add_car_from_date);
        toDateView = rootView.findViewById(R.id.add_car_to_date);
        dateTimeLayout = rootView.findViewById(R.id.add_car_date_time_layout);
        dateView = rootView.findViewById(R.id.add_car_date);
        timeView = rootView.findViewById(R.id.add_car_time);
        seatsView = rootView.findViewById(R.id.add_car_seats);
        increaseSeatView = rootView.findViewById(R.id.add_car_increase_seat);
        decreaseSeatView = rootView.findViewById(R.id.add_car_decrease_seat);
        fareView = rootView.findViewById(R.id.add_car_fare);
        carNameView = rootView.findViewById(R.id.add_car_name);
        carNumberView = rootView.findViewById(R.id.add_car_number);
    }
}