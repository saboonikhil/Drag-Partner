package com.drag.partner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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

import com.drag.partner.model.Cab;
import com.drag.partner.model.Partner;
import com.drag.partner.network.APIUtils;
import com.drag.partner.network.EndPointInterface;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripDetailsActivity extends AppCompatActivity {

    private String TAG = "TripDetailsActivity";
    private String token;
    private Partner partner;
    private Cab[] cabsBooked;
    private int position;
    private TextView riderNameView, carNameView, pickupView, dropView, seatsView,
            driverNameView, driverContactView, carNumberView, fareView;
    private ImageButton riderContactView;
    private CardView driverInfoView;
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
        cabsBooked = (Cab[]) intent.getSerializableExtra("trip_details");
        position = Integer.parseInt(intent.getStringExtra("position"));

        riderNameView.setText(cabsBooked[position].getRiders()[0].getName());
        riderContactView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAction();
            }
        });

        driverName = cabsBooked[position].getDriverName();
        driverContact = cabsBooked[position].getDriverContact();
        if (driverName == null || driverContact == null || driverName.length() < 1 || driverContact.length() < 1)
            driverInfoView.setVisibility(View.GONE);
        else
            setDriverInfo(driverName, driverContact);

        carName = cabsBooked[position].getCarName();
        carNameView.setText(carName);
        carNumber = cabsBooked[position].getCarNumber();
        if (carNumber == null || carNumber.length() < 1)
            carNumberView.setVisibility(View.GONE);
        else
            setCabInfo(carNumber);

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
    }

    private void saveTrip(final String driverName, final String driverContact, final String carName, final String carNumber) {
        pd = ProgressDialog.show(this, "", "Saving...", true, false);

        EndPointInterface service = APIUtils.getAPIService(TripDetailsActivity.this);
        service.cabUpdate(cabsBooked[position].get_id(), partner.getEmail(), token, carName,
                carNumber, driverName, driverContact).enqueue(new Callback<Cab>() {
            @Override
            public void onResponse(@NonNull Call<Cab> call, @NonNull Response<Cab> response) {
                if (response.body() != null) {
                    cabsBooked[position].setDriverName(driverName);
                    cabsBooked[position].setDriverContact(driverContact);
                    cabsBooked[position].setCarName(carName);
                    cabsBooked[position].setCarNumber(carNumber);

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
                Snackbar.make(rootView, "Something went wrong. Please try again later!", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setDriverInfo(String driverName, String driverContact) {
        driverInfoView.setVisibility(View.VISIBLE);
        driverNameView.setText(driverName);
        driverContactView.setText(driverContact);
    }

    private void setCabInfo(String carNumber) {
        carNumberView.setVisibility(View.VISIBLE);
        carNumberView.setText(carNumber);
    }

    private void togglePositiveButton(boolean enable) {
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(enable);
    }

    private void callAction() {
        String riderContact = cabsBooked[position].getRiders()[0].getContact();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + riderContact));

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
        driverInfoView = findViewById(R.id.trip_details_driver_info);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                callAction();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}