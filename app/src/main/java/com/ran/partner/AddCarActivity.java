package com.ran.partner;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
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
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddCarActivity extends AppCompatActivity {

    private AutoCompleteTextView collegeNameView, pickupView, dropView;
    private EditText startDateView, startTimeView, seatsView, fareView, carNameView, carNumberView, driverNameView, driverContactView;
    private ImageButton swapLocationView;
    private Button increaseSeatView, decreaseSeatView, addCarView;
    private InputMethodManager imm;
    private TextWatcher textWatcher;
    private Calendar DateCalendar, TimeCalendar, now;
    private String collegeNameText;
    private String countryCode = "+91 ";
    private long thirtyDays = 2592000000L;
    private int selectedDay;
    private int seats = 4;
    private boolean route = true;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        setupActionBar();
        initVariables();

        setupCollegeSpinner();
        setupDateTimePicker();

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

        driverContactView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                driverContactView.setFocusableInTouchMode(true);
                driverContactView.requestFocus();
                imm.showSoftInput(driverContactView, InputMethodManager.SHOW_IMPLICIT);
                return true;
            }
        });

        driverContactView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (driverContactView.getText().toString().length() == 0) {
                        driverContactView.setText(countryCode);
                        Selection.setSelection(driverContactView.getText(), driverContactView.getText().length());
                    }
                    imm.showSoftInput(driverContactView, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    if (driverContactView.getText().toString().equals(countryCode)) {
                        driverContactView.removeTextChangedListener(textWatcher);
                        driverContactView.getText().clear();
                        driverContactView.addTextChangedListener(textWatcher);
                    }
                }
            }
        });

        driverContactView.addTextChangedListener(textWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("+91 ")) {
                    driverContactView.setText(countryCode);
                    Selection.setSelection(driverContactView.getText(), driverContactView.getText().length());
                }

            }
        });

        addCarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCar();
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
        ArrayAdapter collegeSpinnerAdapter = ArrayAdapter.createFromResource(this,
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
                pickupSpinnerAdapter = ArrayAdapter.createFromResource(this,
                        R.array.array_KGP_options_A, R.layout.support_simple_spinner_dropdown_item);
            } else {
                pickupSpinnerAdapter = ArrayAdapter.createFromResource(this,
                        R.array.array_VIT_options_A, R.layout.support_simple_spinner_dropdown_item);
            }
        } else {
            if (collegeSelected.equals(getResources().getStringArray(R.array.colleges)[0])) {
                pickupSpinnerAdapter = ArrayAdapter.createFromResource(this,
                        R.array.array_KGP_options_B, R.layout.support_simple_spinner_dropdown_item);
            } else {
                pickupSpinnerAdapter = ArrayAdapter.createFromResource(this,
                        R.array.array_VIT_options_B, R.layout.support_simple_spinner_dropdown_item);
            }
        }
        pickupView.setAdapter(pickupSpinnerAdapter);
        pickupView.setKeyListener(null);
        pickupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
                dropSpinnerAdapter = ArrayAdapter.createFromResource(this,
                        R.array.array_KGP_options_B, R.layout.support_simple_spinner_dropdown_item);
            } else {
                dropSpinnerAdapter = ArrayAdapter.createFromResource(this,
                        R.array.array_VIT_options_B, R.layout.support_simple_spinner_dropdown_item);
            }
        } else {
            if (collegeSelected.equals(getResources().getStringArray(R.array.colleges)[0])) {
                dropSpinnerAdapter = ArrayAdapter.createFromResource(this,
                        R.array.array_KGP_options_A, R.layout.support_simple_spinner_dropdown_item);
            } else {
                dropSpinnerAdapter = ArrayAdapter.createFromResource(this,
                        R.array.array_VIT_options_A, R.layout.support_simple_spinner_dropdown_item);
            }
        }
        dropView.setAdapter(dropSpinnerAdapter);
        dropView.setKeyListener(null);
        dropView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
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
                    if (TextUtils.isEmpty(startTimeView.getText().toString())) {
                        selectedDay = DateCalendar.get(Calendar.DAY_OF_MONTH);
                        updateLabel();
                    } else {
                        if (TimeCalendar.getTimeInMillis() >= System.currentTimeMillis() - 60000) {
                            selectedDay = DateCalendar.get(Calendar.DAY_OF_MONTH);
                            updateLabel();
                        } else
                            Toast.makeText(getApplicationContext(), "Don't look back you're not going that way!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    selectedDay = DateCalendar.get(Calendar.DAY_OF_MONTH);
                    updateLabel();
                }
            }
        };

        startDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(startDateView.getWindowToken(), 0);
                DateCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddCarActivity.this, date, DateCalendar
                        .get(Calendar.YEAR), DateCalendar.get(Calendar.MONTH), DateCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + thirtyDays);
                datePickerDialog.show();
            }
        });

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                TimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                TimeCalendar.set(Calendar.MINUTE, minute);
                int h = hourOfDay % 12;
                if (TimeCalendar.get(Calendar.DAY_OF_MONTH) == selectedDay) {
                    if (TimeCalendar.getTimeInMillis() >= System.currentTimeMillis() - 60000)
                        startTimeView.setText(String.format("%02d:%02d %s", h == 0 ? 12 : h,
                                minute, hourOfDay < 12 ? "AM" : "PM"));
                    else
                        Toast.makeText(getApplicationContext(), "Don't look back you're not going that way!", Toast.LENGTH_LONG).show();
                } else {
                    if (TextUtils.isEmpty(startDateView.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Hey! You missed selecting the date.", Toast.LENGTH_LONG).show();
                        startDateView.setFocusable(true);
                        startDateView.requestFocus();
                        startDateView.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                    } else
                        startTimeView.setText(String.format("%02d:%02d %s", h == 0 ? 12 : h,
                                minute, hourOfDay < 12 ? "AM" : "PM"));
                }
            }
        };

        startTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimeView.requestFocus();
                imm.hideSoftInputFromWindow(startTimeView.getWindowToken(), 0);
                TimeCalendar = Calendar.getInstance();
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddCarActivity.this, time, TimeCalendar
                        .get(Calendar.HOUR_OF_DAY), TimeCalendar.get(Calendar.MINUTE), false);
                mTimePicker.show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "EEE, MMM d";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        startDateView.setText(sdf.format(DateCalendar.getTime()));
    }

    private void saveCar() {
        String collegeName = collegeNameView.getText().toString();
        String pickup = pickupView.getText().toString();
        String drop = dropView.getText().toString();
        String startDate = startDateView.getText().toString();
        String startTime = startTimeView.getText().toString();
        String seats = seatsView.getText().toString();
        String fare = fareView.getText().toString();
        String carName = carNameView.getText().toString();
        String carNumber = carNumberView.getText().toString();
        String driverName = driverNameView.getText().toString();
        String driverContact = driverContactView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(collegeName)) {
            focusView = collegeNameView;
            cancel = true;
        } else if (TextUtils.isEmpty(seats)) {
            focusView = seatsView;
            cancel = true;
        } else if (TextUtils.isEmpty(carName)) {
            focusView = carNameView;
            cancel = true;
        } else if (TextUtils.isEmpty(carNumber)) {
            focusView = carNumberView;
            cancel = true;
        } else if (driverContact.equals(countryCode)) {
            driverContactView.removeTextChangedListener(textWatcher);
            driverContactView.getText().clear();
            driverContactView.addTextChangedListener(textWatcher);
        } else if (driverContact.length() > 4 && driverContact.length() < 14) {
            focusView = driverContactView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            focusView.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
        } else {
            CabDetails cabDetails = new CabDetails(collegeName, carName, pickup, drop, startDate,
                    startTime, seats, "Admin", driverName, driverContact, carNumber, fare, "Admin");

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mRefCarsAvailable = database.getReference().child("Cars Available").push();
            mRefCarsAvailable.setValue(cabDetails);
            Toast.makeText(this, "Car added successfully!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(AddCarActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initVariables() {
        collegeNameView = findViewById(R.id.add_car_college);
        pickupView = findViewById(R.id.add_car_pickup);
        dropView = findViewById(R.id.add_car_drop);
        swapLocationView = findViewById(R.id.add_car_swap_location);
        startDateView = findViewById(R.id.add_car_date);
        startTimeView = findViewById(R.id.add_car_time);
        seatsView = findViewById(R.id.add_car_seats);
        increaseSeatView = findViewById(R.id.add_car_increase_seat);
        decreaseSeatView = findViewById(R.id.add_car_decrease_seat);
        fareView = findViewById(R.id.add_car_fare);
        carNameView = findViewById(R.id.add_car_name);
        carNumberView = findViewById(R.id.add_car_number);
        driverNameView = findViewById(R.id.add_car_driver_name);
        driverContactView = findViewById(R.id.add_car_driver_contact);
        addCarView = findViewById(R.id.button_add_car);
        imm = (InputMethodManager) AddCarActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
    }
}