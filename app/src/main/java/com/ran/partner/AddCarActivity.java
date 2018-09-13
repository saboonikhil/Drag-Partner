package com.ran.partner;

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
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddCarActivity extends AppCompatActivity {

    private final long thirtyDays = 2592000000L;
    private boolean route = true;
    private Calendar DateCalendar, TimeCalendar, now;
    private int selectedDay, seats;
    private AutoCompleteTextView dropView, collegeNameView, pickupView;
    private EditText startDateView, startTimeView, seatsView, fareView, carNameView, carNumberView, driverNameView, driverContactView;
    private String collegeNameText;
    private Button increaseSeatView, decreaseSeatView, addCarView;
    private ImageButton swapLocationView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        setupActionBar();
        initVariables();

        setupDateTimePicker();
        seats = 1;
        String numberAsString = "" + seats;
        seatsView.setText(numberAsString);

        pickupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) AddCarActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(pickupView.getWindowToken(), 0);
                View focusView = null;
                boolean cancel = false;
                collegeNameText = collegeNameView.getText().toString();
                if (TextUtils.isEmpty(collegeNameText)) {
                    collegeNameView.setError("This field is required.");
                    focusView = collegeNameView;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                    return true;
                }
                return true;
            }
        });

        dropView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) AddCarActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(dropView.getWindowToken(), 0);
                View focusView = null;
                boolean cancel = false;
                collegeNameText = collegeNameView.getText().toString();
                if (TextUtils.isEmpty(collegeNameText)) {
                    collegeNameView.setError("This field is required.");
                    focusView = collegeNameView;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();
                    return true;
                }
                return true;
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

        swapLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View focusView;
                if (TextUtils.isEmpty(collegeNameText)) {
                    collegeNameView.setError("This field is required.");
                    focusView = collegeNameView;
                    focusView.requestFocus();
                } else {
                    route = !route;
                    Editable location = pickupView.getText();
                    pickupView.setText(dropView.getText());
                    dropView.setText(location);
                    selectLocation(collegeNameText, route);
                }
            }
        });

        increaseSeatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seats++;
                seatsView.setText(String.valueOf(seats));
            }
        });

        decreaseSeatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seats == 1) {
                    Toast.makeText(getApplicationContext(), "Are you crazy!?", Toast.LENGTH_SHORT).show();
                } else {
                    seats--;
                    seatsView.setText(String.valueOf(seats));
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

    private void saveCar() {
        String seats = seatsView.getText().toString();
        String carName = carNameView.getText().toString();
        String driverName = driverNameView.getText().toString();
        String driverContact = driverContactView.getText().toString();
        String carNumber = carNumberView.getText().toString();
        String collegeName = collegeNameView.getText().toString();
        String startDate = startDateView.getText().toString();
        String startTime = startTimeView.getText().toString();
        String pickup = pickupView.getText().toString();
        String drop = dropView.getText().toString();
        String fare = fareView.getText().toString();

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

    private void updateLabel() {
        String myFormat = "EEE, MMM d";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        startDateView.setText(sdf.format(DateCalendar.getTime()));
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
                pickupView.setError(null);
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
                dropView.setError(null);
                ((AutoCompleteTextView) view).showDropDown();
                return false;
            }
        });
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
                collegeNameView.setError(null);
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
                DateCalendar = Calendar.getInstance();
                startDateView.setError(null);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddCarActivity.this, date, DateCalendar
                        .get(Calendar.YEAR), DateCalendar.get(Calendar.MONTH),
                        DateCalendar.get(Calendar.DAY_OF_MONTH));
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
                View focusView;
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
                        startDateView.setError("This field is required.");
                        focusView = startDateView;
                        focusView.requestFocus();
                    } else
                        startTimeView.setText(String.format("%02d:%02d %s", h == 0 ? 12 : h,
                                minute, hourOfDay < 12 ? "AM" : "PM"));
                }
            }
        };

        startTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeCalendar = Calendar.getInstance();
                startTimeView.setError(null);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddCarActivity.this, time, TimeCalendar
                        .get(Calendar.HOUR_OF_DAY), TimeCalendar.get(Calendar.MINUTE), false);
                mTimePicker.show();
            }
        });
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
    }
}