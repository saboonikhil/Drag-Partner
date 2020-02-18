package com.drag.partner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.drag.partner.model.Cab;
import com.drag.partner.model.Partner;
import com.drag.partner.network.APIUtils;
import com.drag.partner.network.EndPointInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripDetailsActivity extends AppCompatActivity {

    private static String TAG = TripDetailsActivity.class.getSimpleName();
    private String token;
    private Partner partner;
    private Cab[] trips;
    private int position;
    private Calendar startTime;
    private LinearLayout rider0View, rider1View, rider2View, rider3View;
    private TextView startTimeView, carNameView, pickupView, dropView, driverNameView, driverContactView, carNumberView, amountView, amountPendingView;
    private CardView riderInfoView, driverInfoView;
    private ImageButton backView;
    private Button updateView;
    private AlertDialog dialog;
    private EditText editDriverNameView, editDriverContactView, editCarNameView, editCarNumberView;
    private String countryCode = "+91 ", driverName, driverContact, carName, carNumber;
    private ProgressDialog pd;
    private LinearLayout rootView;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        setContentView(R.layout.activity_trip_details);
        initViews();

        SharedPreferences pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        token = pref.getString("token", "");
        String json = pref.getString("dbObj", "");
        partner = new Gson().fromJson(json, Partner.class);

        Intent intent = getIntent();
        trips = (Cab[]) intent.getSerializableExtra("trip_details");
        position = Integer.parseInt(intent.getStringExtra("position"));

        try {
            startTime = Calendar.getInstance();
            Date displayTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(trips[position].getStartTime());
            startTime.setTime(displayTime);
            startTime.add(Calendar.HOUR, 5);
            startTime.add(Calendar.MINUTE, 30);
            startTimeView.setText(new SimpleDateFormat("EEE, MMM d, hh:mm a").format(startTime.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (isShowTime()) {

            if (trips[position].getRiders().length > 0) {
                rider0View.setVisibility(View.VISIBLE);
                TextView riderNameView = findViewById(R.id.trip_details_rider_name0);
                riderNameView.setText(trips[position].getRiders()[0].getId().getName());

                TextView riderContactView = findViewById(R.id.trip_details_rider_contact0);
                riderContactView.setText(trips[position].getRiders()[0].getId().getContact());
            }
            if (trips[position].getRiders().length > 1) {
                rider1View.setVisibility(View.VISIBLE);
                TextView riderNameView = findViewById(R.id.trip_details_rider_name1);
                riderNameView.setText(trips[position].getRiders()[1].getId().getName());

                TextView riderContactView = findViewById(R.id.trip_details_rider_contact1);
                riderContactView.setText(trips[position].getRiders()[1].getId().getContact());
            }
            if (trips[position].getRiders().length > 2) {
                rider2View.setVisibility(View.VISIBLE);
                TextView riderNameView = findViewById(R.id.trip_details_rider_name2);
                riderNameView.setText(trips[position].getRiders()[2].getId().getName());

                TextView riderContactView = findViewById(R.id.trip_details_rider_contact2);
                riderContactView.setText(trips[position].getRiders()[2].getId().getContact());
            }
            if (trips[position].getRiders().length > 3) {
                rider3View.setVisibility(View.VISIBLE);
                TextView riderNameView = findViewById(R.id.trip_details_rider_name3);
                riderNameView.setText(trips[position].getRiders()[3].getId().getName());

                TextView riderContactView = findViewById(R.id.trip_details_rider_contact3);
                riderContactView.setText(trips[position].getRiders()[3].getId().getContact());
            }

        } else {
            riderInfoView.setVisibility(View.GONE);
        }

        driverName = trips[position].getDriverName();
        driverContact = trips[position].getDriverContact();
        if (driverName == null || driverContact == null || driverName.length() < 1 || driverContact.length() < 1) {
            driverInfoView.setVisibility(View.GONE);
            riderInfoView.setVisibility(View.GONE);
        } else
            setDriverInfo(driverName, driverContact);

        carName = trips[position].getCarName();
        if (carName == null || carName.length() < 1)
            carNameView.setText(trips[position].getType());
        else
            carNameView.setText(carName);

        carNumber = trips[position].getCarNumber();
        if (carNumber == null || carNumber.length() < 1) {
            carNumberView.setVisibility(View.GONE);
            riderInfoView.setVisibility(View.GONE);
        } else
            setCabInfo(carNumber);

        pickupView.setText(trips[position].getRiders()[0].getPickup());
        dropView.setText(trips[position].getRiders()[0].getDrop());

        float fare = Float.parseFloat(trips[position].getFare());
        float commission = Float.parseFloat(trips[position].getRiders()[0].getLuggageCount());
        String displayAmount = "₹ " + String.format(java.util.Locale.US, "%.2f", (fare - (commission * 0.01 * fare)));
        amountView.setText(displayAmount);

        float amountPaid = Float.parseFloat(trips[position].getRiders()[0].getFare());
        String displayAmountPending = "₹ " + String.format(java.util.Locale.US, "%.2f", (fare - amountPaid));
        amountPendingView.setText(displayAmountPending);

        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Calendar.getInstance().getTimeInMillis() < startTime.getTimeInMillis())
            updateView.setEnabled(true);
        else
            updateView.setEnabled(false);

        updateView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                View customView = getLayoutInflater().inflate(R.layout.layout_update_trip, rootView, false);
                editDriverNameView = customView.findViewById(R.id.update_trip_driver_name);
                editDriverContactView = customView.findViewById(R.id.update_trip_driver_number);
                editCarNameView = customView.findViewById(R.id.update_trip_car_name);
                editCarNumberView = customView.findViewById(R.id.update_trip_car_number);
                editDriverNameView.setText(driverName);
                editDriverContactView.setText(driverContact);
                editCarNameView.setText(carName);
                editCarNumberView.setText(carNumber);

                dialog = new AlertDialog.Builder(TripDetailsActivity.this, R.style.MaterialAlertDialogStyle)
                        .setTitle("Update Trip")
                        .setView(customView)
                        .setPositiveButton("Save", null)
                        .setNegativeButton("Cancel", null)
                        .create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface di) {
                        dialog.getButton(di.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!editDriverNameView.getText().toString().equals(driverName))
                                    driverName = editDriverNameView.getText().toString();
                                if (!editDriverContactView.getText().toString().equals(driverContact))
                                    driverContact = editDriverContactView.getText().toString();
                                if (!editCarNameView.getText().toString().equals(carName))
                                    carName = editCarNameView.getText().toString();
                                if (!editCarNumberView.getText().toString().equals(carNumber))
                                    carNumber = editCarNumberView.getText().toString();

                                saveTrip(driverName, driverContact, carName, carNumber);
                                dialog.dismiss();
                            }
                        });
                        dialog.getButton(di.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
                dialog.show();
                togglePositiveButton(false);

                editDriverNameView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().equals(driverName))
                            togglePositiveButton(false);
                        else
                            togglePositiveButton(true);
                    }
                });

                editDriverContactView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!s.toString().startsWith("+91 ")) {
                            editDriverContactView.setText(countryCode);
                            Selection.setSelection(editDriverContactView.getText(), editDriverContactView.getText().length());
                        } else if (s.toString().length() >= 4 && s.toString().length() < 14) {
                            togglePositiveButton(false);
                        } else {
                            togglePositiveButton(true);
                        }
                    }
                });

                editCarNameView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().equals(carName))
                            togglePositiveButton(false);
                        else
                            togglePositiveButton(true);
                    }
                });

                editCarNumberView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().equals(carNumber))
                            togglePositiveButton(false);
                        else
                            togglePositiveButton(true);
                    }
                });
            }
        });
    }

    private void saveTrip(final String driverName, final String driverContact, final String carName, final String carNumber) {
        pd = ProgressDialog.show(this, "", "Saving...", true, false);

        EndPointInterface service = APIUtils.getAPIService(TripDetailsActivity.this);
        service.cabUpdate(trips[position].get_id(), partner.getEmail(), token, carName,
                carNumber, driverName, driverContact).enqueue(new Callback<Cab>() {

            @Override
            public void onResponse(@NonNull Call<Cab> call, @NonNull Response<Cab> response) {
                if (response.body() != null) {
                    trips[position].setDriverName(driverName);
                    trips[position].setDriverContact(driverContact);
                    trips[position].setCarName(carName);
                    trips[position].setCarNumber(carNumber);

                    if (driverName.length() < 1 || driverContact.length() < 1)
                        driverInfoView.setVisibility(View.GONE);
                    else
                        setDriverInfo(driverName, driverContact);

                    carNameView.setText(carName);
                    if (carNumber.length() < 1)
                        carNumberView.setVisibility(View.GONE);
                    else
                        setCabInfo(carNumber);

                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Trip updated successfully", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Cab> call, @NonNull Throwable t) {
                pd.dismiss();
                Log.e(TAG + " On Failure", t.getMessage());
                Snackbar.make(rootView, "Please check your internet connection or try again later.", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setDriverInfo(String driverName, String driverContact) {
        if (isShowTime()) riderInfoView.setVisibility(View.VISIBLE);
        driverInfoView.setVisibility(View.VISIBLE);
        driverNameView.setText(driverName);
        driverContactView.setText(driverContact);
    }

    private void setCabInfo(String carNumber) {
        carNumberView.setVisibility(View.VISIBLE);
        carNumberView.setText(carNumber);
    }

    private boolean isShowTime() {
        return (Calendar.getInstance().getTimeInMillis() - startTime.getTimeInMillis()) > -21600000 &&
                (Calendar.getInstance().getTimeInMillis() - startTime.getTimeInMillis()) < 86400000;
    }

    private void togglePositiveButton(boolean enable) {
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(enable);
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
    }

    private void initViews() {
        rootView = findViewById(R.id.trip_details_activity_layout);
        backView = findViewById(R.id.trip_details_back);
        startTimeView = findViewById(R.id.trip_details_start_time);
        updateView = findViewById(R.id.trip_details_update);
        riderInfoView = findViewById(R.id.trip_details_rider_info);
        rider0View = findViewById(R.id.trip_details_rider_layout0);
        rider1View = findViewById(R.id.trip_details_rider_layout1);
        rider2View = findViewById(R.id.trip_details_rider_layout2);
        rider3View = findViewById(R.id.trip_details_rider_layout3);
        driverInfoView = findViewById(R.id.trip_details_driver_info);
        driverNameView = findViewById(R.id.trip_details_driver_name);
        driverContactView = findViewById(R.id.trip_details_driver_contact);
        carNameView = findViewById(R.id.trip_details_car_name);
        carNumberView = findViewById(R.id.trip_details_car_number);
        pickupView = findViewById(R.id.trip_details_pickup);
        dropView = findViewById(R.id.trip_details_drop);
        amountView = findViewById(R.id.trip_details_amount);
        amountPendingView = findViewById(R.id.trip_details_amount_pending);
    }
}