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

public class AddRideFragment extends DialogFragment {

    public static String TAG = "AddRideFragment";
    private Activity parentActivity;
    private View rootView;
    private Location[] locations;
    private String[] cities;
    private Partner partner;
    private ImageButton closeView;
    private TextView titleView;
    private Button saveView;
    private Switch switchDateView;
    private AutoCompleteTextView cityView, pickupView, dropView;
    private EditText fromDateView, toDateView, dateView, timeView, seatsView, fareView;
    private ImageButton swapLocationView;
    private LinearLayout fromToDateLayout, dateTimeLayout;
    private Button increaseSeatView, decreaseSeatView;
    private InputMethodManager imm;
    private Calendar fromDateCalendar, toDateCalendar, DateCalendar, TimeCalendar;
    private String cityText, token, pickup, drop, seats, fare;
    private long thirtyDays = 2592000000L;
    private int seatsText = 4;
    private boolean route = true;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_add_ride, container, false);
        return rootView;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();

        SharedPreferences pref = parentActivity.getSharedPreferences("AppPref", MODE_PRIVATE);
        token = pref.getString("token", "");
        String json = pref.getString("dbObj", "");
        partner = new Gson().fromJson(json, Partner.class);

        locations = (Location[]) ObjectSerializer.deserialize(pref.getString("locations",
                ObjectSerializer.serialize(new Location[10])));
        cities = new String[locations.length];
        for (int i = 0; i < locations.length; i++)
            cities[i] = locations[i].getCity();

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
                saveRide();
            }
        });

        switchDateView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    titleView.setText(R.string.new_rides);
                    fromToDateLayout.setVisibility(View.VISIBLE);
                    dateTimeLayout.setVisibility(View.GONE);
                } else {
                    titleView.setText(R.string.new_ride);
                    fromToDateLayout.setVisibility(View.GONE);
                    dateTimeLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        setupCitySpinner();
        cityView.addTextChangedListener(new TextWatcher() {
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
                cityText = cityView.getText().toString();
                selectLocation(cityText, route);
            }
        });

        pickupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                imm.hideSoftInputFromWindow(pickupView.getWindowToken(), 0);
                if (TextUtils.isEmpty(cityText)) {
                    cityView.requestFocus();
                    cityView.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                }
                return true;
            }
        });

        dropView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                imm.hideSoftInputFromWindow(dropView.getWindowToken(), 0);
                if (TextUtils.isEmpty(cityText)) {
                    cityView.requestFocus();
                    cityView.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                }
                return true;
            }
        });

        swapLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(cityText)) {
                    cityView.requestFocus();
                    cityView.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                    imm.hideSoftInputFromWindow(cityView.getWindowToken(), 0);
                } else if ((TextUtils.isEmpty(pickupView.getText().toString())) && (TextUtils.isEmpty(dropView.getText().toString()))) {
                    pickupView.requestFocus();
                    pickupView.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                    imm.hideSoftInputFromWindow(pickupView.getWindowToken(), 0);
                } else {
                    route = !route;
                    Editable location = pickupView.getText();
                    pickupView.setText(dropView.getText());
                    dropView.setText(location);
                    selectLocation(cityText, route);
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

    private void selectLocation(String citySelected, boolean routeSelected) {
        boolean isCityPresent = false;
        int position = 0;
        for (int i = 0; i < cities.length; i++) {
            if (citySelected.equals(cities[i])) {
                isCityPresent = true;
                position = i;
            }
        }
        if (isCityPresent) {
            setupPickupLocationSpinner(position, routeSelected);
            setupDropLocationSpinner(position, routeSelected);
        } else {
            pickupView.setKeyListener(null);
            dropView.setKeyListener(null);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupCitySpinner() {
        ArrayAdapter<String> citySpinnerAdapter = new ArrayAdapter<>(parentActivity,
                R.layout.support_simple_spinner_dropdown_item, cities);
        cityView.setAdapter(citySpinnerAdapter);
        cityView.setKeyListener(null);
        cityView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((AutoCompleteTextView) view).showDropDown();
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupPickupLocationSpinner(int position, boolean routeSelected) {
        /*ArrayAdapter<String> pickupSpinnerAdapter;
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
        });*/
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupDropLocationSpinner(int position, boolean routeSelected) {
        /*ArrayAdapter<String> dropSpinnerAdapter;
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
        });*/
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
    private void saveRide() {
        String city = cityView.getText().toString();
        seats = seatsView.getText().toString();
        pickup = pickupView.getText().toString();
        drop = dropView.getText().toString();
        String sDate = dateView.getText().toString();
        String sTime = timeView.getText().toString();

        if (!TextUtils.isEmpty(fareView.getText().toString())) {
            fare = fareView.getText().toString();
        }

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(city)) {
            focusView = cityView;
            cancel = true;
        } else if (TextUtils.isEmpty(pickup)) {
            focusView = pickupView;
            cancel = true;
        } else if (TextUtils.isEmpty(drop)) {
            focusView = dropView;
            cancel = true;
        } else if (TextUtils.isEmpty(sDate)) {
            focusView = dateView;
            cancel = true;
        } else if (TextUtils.isEmpty(sTime)) {
            focusView = timeView;
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
                        saveRides();
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
                            addRide(startTime);
                        } else {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(23400000);
                            Date time = calendar.getTime();
                            String startTime = formatDateTime(date, time);
                            addRide(startTime);
                        }
                    }
                }
            } else
                Snackbar.make(rootView, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }

    private void saveRides() {
        if (fromDateCalendar.before(toDateCalendar) || fromDateCalendar.equals(toDateCalendar)) {
            Date date = fromDateCalendar.getTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(23400000);
            Date time = calendar.getTime();
            String startTime = formatDateTime(date, time);
            addRide(startTime);
        } else {
            postOnResponse();
            Toast.makeText(parentActivity, "Rides added successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void addRide(String startTime) {
        EndPointInterface service = APIUtils.getAPIService(parentActivity);
        Call<Partner> call = service.addRide(partner.getEmail(), token, pickup, drop, startTime, seats, fare);
        call.enqueue(new Callback<Partner>() {
            @Override
            public void onResponse(@NonNull Call<Partner> call, @NonNull Response<Partner> response) {
                if (response.body() != null) {
                    if (fromToDateLayout.getVisibility() == View.GONE) {
                        postOnResponse();
                        Toast.makeText(parentActivity, "Ride added successfully", Toast.LENGTH_SHORT).show();
                    } else if (response.isSuccessful()) {
                        fromDateCalendar.add(Calendar.DATE, 1);
                        saveRides();
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
        closeView = rootView.findViewById(R.id.add_ride_close);
        titleView = rootView.findViewById(R.id.add_ride_title);
        saveView = rootView.findViewById(R.id.add_ride_save);
        cityView = rootView.findViewById(R.id.add_ride_city);
        switchDateView = rootView.findViewById(R.id.add_ride_switch_date);
        pickupView = rootView.findViewById(R.id.add_ride_pickup);
        dropView = rootView.findViewById(R.id.add_ride_drop);
        swapLocationView = rootView.findViewById(R.id.add_ride_swap_location);
        fromToDateLayout = rootView.findViewById(R.id.add_ride_from_to_date_layout);
        fromDateView = rootView.findViewById(R.id.add_ride_from_date);
        toDateView = rootView.findViewById(R.id.add_ride_to_date);
        dateTimeLayout = rootView.findViewById(R.id.add_ride_date_time_layout);
        dateView = rootView.findViewById(R.id.add_ride_date);
        timeView = rootView.findViewById(R.id.add_ride_time);
        seatsView = rootView.findViewById(R.id.add_ride_seats);
        increaseSeatView = rootView.findViewById(R.id.add_ride_increase_seat);
        decreaseSeatView = rootView.findViewById(R.id.add_ride_decrease_seat);
        fareView = rootView.findViewById(R.id.add_ride_fare);
    }
}