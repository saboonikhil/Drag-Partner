package com.ran.partner;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddCarActivity extends AppCompatActivity {

    private final long thirtyDays = 2592000000L;
    boolean route = true;
    private Calendar DateCalendar;
    private Calendar TimeCalendar;
    private Calendar now;
    private int selectedDay;
    private AutoCompleteTextView dropLocationTextView, collegeSpinnerTextView;
    private AutoCompleteTextView pickupLocationTextView;
    private EditText dateTextView, timeTextView;
    private EditText numberOfPeopleTextView;
    private String college;
    private int seats;
    private Button increaseSeat, decreaseSeat, addCarButton;
    private ImageButton locationSwap;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        setupActionBar();
        initVariables();

        setupDateTimePicker();
        seats = 1;
        numberOfPeopleTextView.setText(Integer.toString(seats));

        pickupLocationTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) AddCarActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(pickupLocationTextView.getWindowToken(), 0);
                View focusView = null;
                boolean cancel = false;
                college = collegeSpinnerTextView.getText().toString();
                if (TextUtils.isEmpty(college)) {
                    collegeSpinnerTextView.setError("This field is required.");
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

        dropLocationTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) AddCarActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(dropLocationTextView.getWindowToken(), 0);
                View focusView = null;
                boolean cancel = false;
                college = collegeSpinnerTextView.getText().toString();
                if (TextUtils.isEmpty(college)) {
                    collegeSpinnerTextView.setError("This field is required.");
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

        setupCollegeSpinner();

        collegeSpinnerTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pickupLocationTextView.setText("");
                dropLocationTextView.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                college = collegeSpinnerTextView.getText().toString();
                selectLocation(college, route);
            }
        });


        locationSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View focusView;
                if (TextUtils.isEmpty(college)) {
                    collegeSpinnerTextView.setError("This field is required.");
                    focusView = collegeSpinnerTextView;
                    focusView.requestFocus();
                } else {
                    route = !route;
                    Editable location = pickupLocationTextView.getText();
                    pickupLocationTextView.setText(dropLocationTextView.getText());
                    dropLocationTextView.setText(location);
                    selectLocation(college, route);
                }
            }
        });

        increaseSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seats++;
                numberOfPeopleTextView.setText(String.valueOf(seats));
            }
        });

        decreaseSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seats == 1) {
                    Toast.makeText(getApplicationContext(), "Are you crazy!?", Toast.LENGTH_SHORT).show();
                } else {
                    seats--;
                    numberOfPeopleTextView.setText(String.valueOf(seats));
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
            pickupLocationTextView.setKeyListener(null);
            dropLocationTextView.setKeyListener(null);
        }
    }

    private void updateLabel() {
        String myFormat = "EEE, MMM d";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateTextView.setText(sdf.format(DateCalendar.getTime()));
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

        pickupLocationTextView.setAdapter(pickupSpinnerAdapter);
        pickupLocationTextView.setKeyListener(null);

        pickupLocationTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pickupLocationTextView.setError(null);
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

        dropLocationTextView.setAdapter(dropSpinnerAdapter);
        dropLocationTextView.setKeyListener(null);
        dropLocationTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dropLocationTextView.setError(null);
                ((AutoCompleteTextView) view).showDropDown();
                return false;
            }
        });
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

    private void setupDateTimePicker() {
        now = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DateCalendar.set(Calendar.YEAR, year);
                DateCalendar.set(Calendar.MONTH, monthOfYear);
                DateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (now.get(Calendar.DAY_OF_MONTH) == dayOfMonth) {
                    if (TextUtils.isEmpty(timeTextView.getText().toString())) {
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

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateCalendar = Calendar.getInstance();
                dateTextView.setError(null);
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
                        timeTextView.setText(String.format("%02d:%02d %s", h == 0 ? 12 : h,
                                minute, hourOfDay < 12 ? "AM" : "PM"));
                    else
                        Toast.makeText(getApplicationContext(), "Don't look back you're not going that way!", Toast.LENGTH_LONG).show();
                } else {
                    if (TextUtils.isEmpty(dateTextView.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Hey! You missed selecting the date.", Toast.LENGTH_LONG).show();
                        dateTextView.setError("This field is required.");
                        focusView = dateTextView;
                        focusView.requestFocus();
                    } else
                        timeTextView.setText(String.format("%02d:%02d %s", h == 0 ? 12 : h,
                                minute, hourOfDay < 12 ? "AM" : "PM"));
                }
            }
        };

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeCalendar = Calendar.getInstance();
                timeTextView.setError(null);
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
        collegeSpinnerTextView = findViewById(R.id.spinner_select_college);
        dropLocationTextView = findViewById(R.id.dropLocationAutoComplete);
        pickupLocationTextView = findViewById(R.id.pickupLocationAutoComplete);
        dateTextView = findViewById(R.id.travel_date);
        timeTextView = findViewById(R.id.travel_time);
        numberOfPeopleTextView = findViewById(R.id.number_of_people);
        increaseSeat = findViewById(R.id.button_increase_seat);
        decreaseSeat = findViewById(R.id.button_decrease_seat);
        locationSwap = findViewById(R.id.location_swap);
        addCarButton = findViewById(R.id.button_add_car);
    }
}