package com.ran.partner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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

import com.ran.partner.model.Cab;
import com.ran.partner.model.Rider;
import com.ran.partner.network.APIUtils;
import com.ran.partner.network.EndPointInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripDetailsActivity extends AppCompatActivity {

    private String TAG = "TripDetailsActivity";
    private Cab[] cabsBooked;
    private int position;
    private TextView riderNameView, riderContactView, carNameView, pickupView, dropView, seatsView,
            driverNameView, driverContactView, carNumberView, fareView;
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

        Intent intent = getIntent();
        cabsBooked = (Cab[]) intent.getSerializableExtra("trip_details");
        position = Integer.parseInt(intent.getStringExtra("position"));

        driverName = cabsBooked[position].getDriverName();
        driverContact = cabsBooked[position].getDriverContact();
        carName = cabsBooked[position].getCarName();
        carNumber = cabsBooked[position].getCarNumber();

        Rider[] riders = cabsBooked[position].getRiders();
        riderNameView.setText(riders[0].getName());
        riderContactView.setText(riders[0].getContact());
        driverNameView.setText(driverName);
        driverContactView.setText(driverContact);
        carNameView.setText(carName);
        carNumberView.setText(carNumber);
        pickupView.setText(cabsBooked[position].getPickup());
        dropView.setText(cabsBooked[position].getDrop());
        seatsView.setText(cabsBooked[position].getSeats());

        String displayFare = "₹ " + cabsBooked[position].getFare();
        fareView.setText(displayFare);

        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

        try {
            Calendar calendar = Calendar.getInstance();
            String startTime = cabsBooked[position].getStartTime();
            if (startTime != null) {
                Date displayTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(startTime);
                calendar.setTime(displayTime);
                calendar.add(Calendar.HOUR, 5);
                calendar.add(Calendar.MINUTE, 30);
                setTitle(new SimpleDateFormat("EEE, MMM d, hh:mm a").format(calendar.getTime()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        riderContactView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAction();
            }
        });
    }

    private void saveTrip(final String driverName, final String driverContact, final String carName, final String carNumber) {
        pd = ProgressDialog.show(this, "", "Saving...", true, false);

        EndPointInterface service = APIUtils.getAPIService();
        service.cabUpdate(cabsBooked[position].get_id(), carName, carNumber, driverName, driverContact).enqueue(new Callback<Cab>() {
            @Override
            public void onResponse(@NonNull Call<Cab> call, @NonNull Response<Cab> response) {
                if (response.body() != null) {
                    cabsBooked[position].setDriverName(driverName);
                    cabsBooked[position].setDriverContact(driverContact);
                    cabsBooked[position].setCarName(carName);
                    cabsBooked[position].setCarNumber(carNumber);
                    driverNameView.setText(driverName);
                    driverContactView.setText(driverContact);
                    carNameView.setText(carName);
                    carNumberView.setText(carNumber);
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Trip updated successfully", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Cab> call, @NonNull Throwable t) {
                pd.dismiss();
                Log.e(TAG + " On Failure", t.getMessage());
                Snackbar.make(rootView, "Something went wrong. Please try again later!", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void togglePositiveButton(boolean enable) {
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(enable);
    }

    private void callAction() {
        String riderContact = riderContactView.getText().toString();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + "+91" + riderContact));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Log.v("TAG", "Calling permission is revoked");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            Log.v("TAG", "Calling permission is granted");
            startActivity(callIntent);
        }
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
    }

    private void initViews() {
        rootView = findViewById(R.id.trip_details_activity_layout);
        backView = findViewById(R.id.trip_details_back);
        updateView = findViewById(R.id.trip_details_update);
        riderNameView = findViewById(R.id.trip_details_rider_name);
        riderContactView = findViewById(R.id.trip_details_rider_contact);
        driverNameView = findViewById(R.id.trip_details_driver_name);
        driverContactView = findViewById(R.id.trip_details_driver_contact);
        carNameView = findViewById(R.id.trip_details_car_name);
        carNumberView = findViewById(R.id.trip_details_car_number);
        pickupView = findViewById(R.id.trip_details_pickup);
        dropView = findViewById(R.id.trip_details_drop);
        seatsView = findViewById(R.id.trip_details_seats);
        fareView = findViewById(R.id.trip_details_fare);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    callAction();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}