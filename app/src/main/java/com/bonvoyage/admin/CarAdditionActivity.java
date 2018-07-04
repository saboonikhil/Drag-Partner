package com.bonvoyage.admin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bonvoyage.admin.model.CarDetailsAdmin;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CarAdditionActivity extends AppCompatActivity {

    private EditText seatsAvailableText;
    private EditText carNameText;
    private EditText driverNameText;
    private EditText driverMobileText;
    private EditText carNumberText;
    private EditText orgNameText, defaultStartDateTextView, defaultStartTimeTextView;
    private Button addcarButton;
    private TextView loginLink;
    private String seatsAvailable, carName, driverName, driverMobile, carNumber, orgName;
    private Calendar DateCalendar;
    private Calendar TimeCalendar;
    final long thirtydays = 2592000000L;
    private int CurrentDay;
    private String college;
    private AutoCompleteTextView collegeSpinnerTextView, defaultPickupLocationTextView, defaultDropLocationEditText;


    private static final String TAG = "CarAdditionActivity";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        setupActionBar();

        collegeSpinnerTextView = (AutoCompleteTextView) findViewById(R.id.spinner_select_college);
        seatsAvailableText = (EditText) findViewById(R.id.seats_available);
        carNameText = (EditText) findViewById(R.id.car_name);
        driverNameText = (EditText) findViewById(R.id.driver_name);
        driverMobileText = (EditText) findViewById(R.id.driver_number);
        carNumberText = (EditText) findViewById(R.id.car_number);
        orgNameText = (EditText) findViewById(R.id.org_name);
        defaultPickupLocationTextView = (AutoCompleteTextView) findViewById(R.id.default_pickup_location);
        defaultDropLocationEditText = (AutoCompleteTextView) findViewById(R.id.default_drop_location);
        defaultStartDateTextView = (EditText) findViewById(R.id.default_start_date);
        defaultStartTimeTextView = (EditText) findViewById(R.id.default_start_time);

        setupDateTimePicker();
        setupCollegeSpinner();

        addcarButton = (Button) findViewById(R.id.btn_addcar);

        addcarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCar();
            }
        });

        defaultPickupLocationTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(defaultPickupLocationTextView.getWindowToken(), 0);
                View focusView = null;
                boolean cancel = false;
                college = collegeSpinnerTextView.getText().toString();
                if (TextUtils.isEmpty(college)) {
                    collegeSpinnerTextView.setError("This Field is Required");
                    focusView = collegeSpinnerTextView;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                    return true;
                }
                return true;
            }
        });

        defaultDropLocationEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(defaultDropLocationEditText.getWindowToken(), 0);
                View focusView = null;
                boolean cancel = false;
                college = collegeSpinnerTextView.getText().toString();
                if (TextUtils.isEmpty(college)) {
                    collegeSpinnerTextView.setError("This Field is Required");
                    focusView = collegeSpinnerTextView;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                    return true;
                }
                return true;
            }
        });

        collegeSpinnerTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                defaultPickupLocationTextView.setText("");
                defaultDropLocationEditText.setText("");

            }

            @Override
            public void afterTextChanged(Editable editable) {
                college = collegeSpinnerTextView.getText().toString();
                selectLocation(college);
            }
        });

    }

    private void selectLocation(String collegeSelected) {

        boolean isCollegePresent = false;

        String availableColleges[] = getResources().getStringArray(R.array.colleges);
        for (String college : availableColleges) {
            if (collegeSelected.equals(college)) {
                isCollegePresent = true;
            }

        }
        if (isCollegePresent) {

            setupPickupLocationSpinner(collegeSelected);
            setupDropLocationSpinner(collegeSelected);

        } else {
            Toast.makeText(this, "Please Select College",
                    Toast.LENGTH_SHORT).show();
            defaultPickupLocationTextView.setKeyListener(null);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupPickupLocationSpinner(String collegeSelected) {
        ArrayAdapter pickupSpinnerAdapter;
        if (collegeSelected.equals(getResources().getStringArray(R.array.colleges)[0])) {

            pickupSpinnerAdapter = ArrayAdapter.createFromResource(this,
                    R.array.array_KGP_options_A, R.layout.support_simple_spinner_dropdown_item);
        } else {
            pickupSpinnerAdapter = ArrayAdapter.createFromResource(this,
                    R.array.array_VIT_options_A, R.layout.support_simple_spinner_dropdown_item);
        }


        defaultPickupLocationTextView.setAdapter(pickupSpinnerAdapter);
        defaultPickupLocationTextView.setKeyListener(null);

        defaultPickupLocationTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                defaultPickupLocationTextView.setError(null);
                ((AutoCompleteTextView) v).showDropDown();
                return false;
            }

        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupDropLocationSpinner(String collegeSelected) {

        ArrayAdapter dropSpinnerAdapter;

        if (collegeSelected.equals(getResources().getStringArray(R.array.colleges)[0])) {

            dropSpinnerAdapter = ArrayAdapter.createFromResource(this,
                    R.array.array_KGP_options_B, R.layout.support_simple_spinner_dropdown_item);
        } else {
            dropSpinnerAdapter = ArrayAdapter.createFromResource(this,
                    R.array.array_VIT_options_B, R.layout.support_simple_spinner_dropdown_item);
        }


        defaultDropLocationEditText.setAdapter(dropSpinnerAdapter);
        defaultDropLocationEditText.setKeyListener(null);
        defaultDropLocationEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                defaultDropLocationEditText.setError(null);
                ((AutoCompleteTextView) view).showDropDown();
                return false;
            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void saveCar() {

        String defaultTravelDate = "", defaultTravelTime = "", defaultPickupLocation = "Available to Book", defaultDropLocation = "Available to Book";

        seatsAvailable = seatsAvailableText.getText().toString();
        carName = carNameText.getText().toString();
        driverName = driverNameText.getText().toString();
        driverMobile = driverMobileText.getText().toString();
        carNumber = carNumberText.getText().toString();
        college = collegeSpinnerTextView.getText().toString();
        defaultTravelDate = defaultStartDateTextView.getText().toString();
        defaultTravelTime = defaultStartTimeTextView.getText().toString();
        defaultPickupLocation = defaultPickupLocationTextView.getText().toString();
        defaultDropLocation = defaultDropLocationEditText.getText().toString();


        CarDetailsAdmin carDetailsAdmin = new CarDetailsAdmin(carName, defaultPickupLocation, defaultDropLocation,
                defaultTravelDate, defaultTravelTime, seatsAvailable, driverName, driverMobile, carNumber, orgName, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRefCarsAvailable = database.getReference().child("Cars Available").push();
        mRefCarsAvailable.setValue(carDetailsAdmin);
        Toast.makeText(this, "Car added successfully!",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(CarAdditionActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(com.bonvoyage.admin.R.anim.push_left_in, com.bonvoyage.admin.R.anim.push_left_out);

    }

    private void setupDateTimePicker() {


        DateCalendar = Calendar.getInstance();
        TimeCalendar = Calendar.getInstance();


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                DateCalendar.set(Calendar.YEAR, year);
                DateCalendar.set(Calendar.MONTH, monthOfYear);
                DateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (TimeCalendar.get(Calendar.DAY_OF_MONTH) == DateCalendar.get(Calendar.DAY_OF_MONTH)) {
                    if (TimeCalendar.getTimeInMillis() >= System.currentTimeMillis() - 60000) {
                        CurrentDay = DateCalendar.get(Calendar.DAY_OF_MONTH);
                        updateLabel();
                    } else {
                        if (TextUtils.isEmpty(defaultStartTimeTextView.getText().toString())) {
                            CurrentDay = DateCalendar.get(Calendar.DAY_OF_MONTH);
                            updateLabel();
                        } else
                            Toast.makeText(CarAdditionActivity.this, "Invalid Date", Toast.LENGTH_LONG).show();
                    }
                } else {
                    CurrentDay = DateCalendar.get(Calendar.DAY_OF_MONTH);
                    updateLabel();
                }
            }
        };

        defaultStartDateTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                defaultStartDateTextView.setError(null);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CarAdditionActivity.this, date, DateCalendar
                        .get(Calendar.YEAR), DateCalendar.get(Calendar.MONTH),
                        DateCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + thirtydays);
                datePickerDialog.show();
            }
        });


        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                TimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                TimeCalendar.set(Calendar.MINUTE, minute);
                int h = hourOfDay % 12;
                if (TimeCalendar.get(Calendar.DAY_OF_MONTH) == CurrentDay) {
                    if (TimeCalendar.getTimeInMillis() >= System.currentTimeMillis() - 60000)
                        defaultStartTimeTextView.setText(String.format("%02d:%02d %s", h == 0 ? 12 : h,
                                minute, hourOfDay < 12 ? "AM" : "PM"));
                    else
                        Toast.makeText(CarAdditionActivity.this, "Invalid Time", Toast.LENGTH_LONG).show();
                } else {
                    if (TextUtils.isEmpty(defaultStartDateTextView.getText().toString()))
                        Toast.makeText(CarAdditionActivity.this, "Please Enter Date!", Toast.LENGTH_LONG).show();
                    else
                        defaultStartTimeTextView.setText(String.format("%02d:%02d %s", h == 0 ? 12 : h,
                                minute, hourOfDay < 12 ? "AM" : "PM"));
                }
            }
        };

        defaultStartTimeTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                defaultStartTimeTextView.setError(null);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CarAdditionActivity.this, time, TimeCalendar
                        .get(Calendar.HOUR_OF_DAY), TimeCalendar.get(Calendar.MINUTE), false);
                mTimePicker.show();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "MMMM d, yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        defaultStartDateTextView.setText(sdf.format(DateCalendar.getTime()));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupCollegeSpinner() {


        ArrayAdapter collegeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.colleges, R.layout.support_simple_spinner_dropdown_item);
        collegeSpinnerTextView.setAdapter(collegeSpinnerAdapter);
        collegeSpinnerTextView.setKeyListener(null);

        collegeSpinnerTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                collegeSpinnerTextView.setError(null);
                ((AutoCompleteTextView) view).showDropDown();
                return false;
            }
        });

    }
}