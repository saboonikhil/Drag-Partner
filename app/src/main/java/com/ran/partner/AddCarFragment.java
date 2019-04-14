package com.ran.partner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ran.partner.model.Partner;
import com.ran.partner.network.APIUtils;
import com.ran.partner.network.EndPointInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class AddCarFragment extends DialogFragment {

    public static String TAG = "AddCarFragment";
    public Activity parentActivity;
    private View rootView;
    private SharedPreferences pref;
    private Partner partner;
    private ImageButton closeView;
    private Button saveView;
    private AutoCompleteTextView collegeNameView, pickupView, dropView;
    private EditText dateView, timeView, seatsView, fareView, carNameView, carNumberView;
    private ImageButton swapLocationView;
    private Button increaseSeatView, decreaseSeatView;
    private InputMethodManager imm;
    private Calendar DateCalendar, TimeCalendar, now;
    private String collegeNameText, isoDate, isoTime, startTime, token, pickup, drop;
    private long thirtyDays = 2592000000L;
    private int selectedDay;
    private int seats = 4;
    private boolean route = true;

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

        setupDateTimePicker();
        String numberAsString = "" + seats;
        seatsView.setText(numberAsString);

        increaseSeatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seats != 14) {
                    seats++;
                    seatsView.setText(String.valueOf(seats));
                }
            }
        });

        decreaseSeatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seats != 1) {
                    seats--;
                    seatsView.setText(String.valueOf(seats));
                }
            }
        });
    }

    private void selectLocation(String collegeSelected, boolean routeSelected) {
        boolean isCollegePresent = false;
        String availableColleges[] = getResources().getStringArray(R.array.colleges);
        for (String college : availableColleges) {
            if (collegeSelected.equals(college)) {
                isCollegePresent = true;
            }
        }
        if (isCollegePresent) {
            setupPickupLocationSpinner(collegeSelected, routeSelected);
            setupDropLocationSpinner(collegeSelected, routeSelected);
        } else {
            pickupView.setKeyListener(null);
            dropView.setKeyListener(null);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupCollegeSpinner() {
        ArrayAdapter collegeSpinnerAdapter = ArrayAdapter.createFromResource(parentActivity,
                R.array.colleges, R.layout.support_simple_spinner_dropdown_item);
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
    private void setupPickupLocationSpinner(String collegeSelected, boolean routeSelected) {
        ArrayAdapter pickupSpinnerAdapter;
        if (routeSelected) {
            if (collegeSelected.equals(getResources().getStringArray(R.array.colleges)[0])) {
                pickupSpinnerAdapter = ArrayAdapter.createFromResource(parentActivity,
                        R.array.array_KGP_options_A, R.layout.support_simple_spinner_dropdown_item);
            } else {
                pickupSpinnerAdapter = ArrayAdapter.createFromResource(parentActivity,
                        R.array.array_VIT_options_A, R.layout.support_simple_spinner_dropdown_item);
            }
        } else {
            if (collegeSelected.equals(getResources().getStringArray(R.array.colleges)[0])) {
                pickupSpinnerAdapter = ArrayAdapter.createFromResource(parentActivity,
                        R.array.array_KGP_options_B, R.layout.support_simple_spinner_dropdown_item);
            } else {
                pickupSpinnerAdapter = ArrayAdapter.createFromResource(parentActivity,
                        R.array.array_VIT_options_B, R.layout.support_simple_spinner_dropdown_item);
            }
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
    private void setupDropLocationSpinner(String collegeSelected, boolean routeSelected) {
        ArrayAdapter dropSpinnerAdapter;
        if (routeSelected) {
            if (collegeSelected.equals(getResources().getStringArray(R.array.colleges)[0])) {
                dropSpinnerAdapter = ArrayAdapter.createFromResource(parentActivity,
                        R.array.array_KGP_options_B, R.layout.support_simple_spinner_dropdown_item);
            } else {
                dropSpinnerAdapter = ArrayAdapter.createFromResource(parentActivity,
                        R.array.array_VIT_options_B, R.layout.support_simple_spinner_dropdown_item);
            }
        } else {
            if (collegeSelected.equals(getResources().getStringArray(R.array.colleges)[0])) {
                dropSpinnerAdapter = ArrayAdapter.createFromResource(parentActivity,
                        R.array.array_KGP_options_A, R.layout.support_simple_spinner_dropdown_item);
            } else {
                dropSpinnerAdapter = ArrayAdapter.createFromResource(parentActivity,
                        R.array.array_VIT_options_A, R.layout.support_simple_spinner_dropdown_item);
            }
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

    private void setupDateTimePicker() {
        now = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DateCalendar.set(Calendar.YEAR, year);
                DateCalendar.set(Calendar.MONTH, monthOfYear);
                DateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (now.get(Calendar.DAY_OF_MONTH) == dayOfMonth) {
                    if (TextUtils.isEmpty(timeView.getText().toString())) {
                        selectedDay = DateCalendar.get(Calendar.DAY_OF_MONTH);
                        formatDate();
                    } else {
                        if (TimeCalendar.getTimeInMillis() >= System.currentTimeMillis() - 60000) {
                            selectedDay = DateCalendar.get(Calendar.DAY_OF_MONTH);
                            formatDate();
                        } else
                            Toast.makeText(getContext(), "Don't look back you're not going that way!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    selectedDay = DateCalendar.get(Calendar.DAY_OF_MONTH);
                    formatDate();
                }
            }
        };

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(dateView.getWindowToken(), 0);
                DateCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(parentActivity, date,
                        DateCalendar.get(Calendar.YEAR), DateCalendar.get(Calendar.MONTH), DateCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + thirtyDays);
                datePickerDialog.show();
            }
        });

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                TimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                TimeCalendar.set(Calendar.MINUTE, minute);
                if (TimeCalendar.get(Calendar.DAY_OF_MONTH) == selectedDay) {
                    if (TimeCalendar.getTimeInMillis() >= System.currentTimeMillis() - 60000)
                        formatTime();
                    else
                        Toast.makeText(getContext(), "Don't look back you're not going that way!", Toast.LENGTH_LONG).show();
                } else {
                    if (TextUtils.isEmpty(dateView.getText().toString()))
                        Toast.makeText(getContext(), "Hey! You missed selecting the date.", Toast.LENGTH_LONG).show();
                    else
                        formatTime();
                }
            }
        };

        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeView.requestFocus();
                imm.hideSoftInputFromWindow(timeView.getWindowToken(), 0);
                TimeCalendar = Calendar.getInstance();
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(parentActivity, time,
                        TimeCalendar.get(Calendar.HOUR_OF_DAY), TimeCalendar.get(Calendar.MINUTE), false);
                mTimePicker.show();
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void formatDate() {
        String displayFormat = new SimpleDateFormat("EEE, MMM d", Locale.US).format(DateCalendar.getTime());
        dateView.setText(displayFormat);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        TimeZone tz = TimeZone.getTimeZone("UTC");
        df.setTimeZone(tz);
        isoDate = df.format(DateCalendar.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    private void formatTime() {
        String displayFormat = new SimpleDateFormat("hh:mm a", Locale.US).format(TimeCalendar.getTime());
        timeView.setText(displayFormat);

        DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
        TimeZone tz = TimeZone.getTimeZone("UTC");
        df.setTimeZone(tz);
        isoTime = df.format(TimeCalendar.getTime());
    }

    private void saveCar() {
        String collegeName = collegeNameView.getText().toString();
        String seats = seatsView.getText().toString();
        String fare = fareView.getText().toString();
        String carName = carNameView.getText().toString();
        String carNumber = carNumberView.getText().toString();

        if (!TextUtils.isEmpty(pickupView.getText().toString()))
            pickup = pickupView.getText().toString();
        if (!TextUtils.isEmpty(dropView.getText().toString()))
            drop = dropView.getText().toString();
        if (!TextUtils.isEmpty(isoDate) && !TextUtils.isEmpty(isoTime))
            startTime = isoDate + 'T' + isoTime + 'Z';

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(collegeName)) {
            focusView = collegeNameView;
            cancel = true;
        } else if (TextUtils.isEmpty(seats)) {
            focusView = seatsView;
            cancel = true;
        } else if (TextUtils.isEmpty(fare)) {
            focusView = fareView;
            cancel = true;
        } else if (TextUtils.isEmpty(carName)) {
            focusView = carNameView;
            cancel = true;
        } else if (TextUtils.isEmpty(carNumber)) {
            focusView = carNumberView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            focusView.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
        } else {
            if (isConnectedToInternet()) {
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
                            Toast.makeText(parentActivity, "Car added successfully", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Partner> call, @NonNull Throwable t) {
                        Log.e(TAG + " On Failure", t.getMessage());
                        Toast.makeText(parentActivity, "Something went wrong. Please try again later!", Toast.LENGTH_LONG).show();
                    }
                });
            } else
                Snackbar.make(rootView, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }

    public boolean isConnectedToInternet() {
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
        saveView = rootView.findViewById(R.id.add_car_save);
        collegeNameView = rootView.findViewById(R.id.add_car_college);
        pickupView = rootView.findViewById(R.id.add_car_pickup);
        dropView = rootView.findViewById(R.id.add_car_drop);
        swapLocationView = rootView.findViewById(R.id.add_car_swap_location);
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