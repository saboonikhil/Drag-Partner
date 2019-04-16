package com.ran.partner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ran.partner.model.Cab;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TripDetailsActivity extends AppCompatActivity {

    private ImageButton backView;
    private Button updateView;
    private TextView riderContactView, carNameView, pickupView, dropView, seatsView, driverContactView, carNumberView, fareView;
    private AlertDialog dialog;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        setContentView(R.layout.activity_trip_details);
        initViews();

        Intent intent = getIntent();
        Cab[] cabsBooked = (Cab[]) intent.getSerializableExtra("trip_details");
        int position = Integer.parseInt(intent.getStringExtra("position"));

        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("InflateParams") View customView = getLayoutInflater().inflate(R.layout.layout_update_trip, null);

                dialog = new AlertDialog.Builder(TripDetailsActivity.this, R.style.MaterialAlertDialogStyle)
                        .setTitle("Update Trip")
                        .setView(customView)
                        .setPositiveButton("Save", null)
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
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

        carNameView.setText(cabsBooked[position].getCarName());
        carNumberView.setText(cabsBooked[position].getCarNumber());
        pickupView.setText(cabsBooked[position].getPickup());
        dropView.setText(cabsBooked[position].getDrop());
        seatsView.setText(cabsBooked[position].getSeats());

        String displayFare = "₹ " + cabsBooked[position].getFare();
        fareView.setText(displayFare);
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

    private void setupActionBar() {
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
    }

    private void initViews() {
        backView = findViewById(R.id.trip_details_back);
        updateView = findViewById(R.id.trip_details_update);
        riderContactView = findViewById(R.id.trip_details_rider_contact);
        driverContactView = findViewById(R.id.trip_details_driver_contact);
        carNameView = findViewById(R.id.trip_details_car_name);
        carNumberView = findViewById(R.id.trip_details_car_number);
        pickupView = findViewById(R.id.trip_details_pickup);
        dropView = findViewById(R.id.trip_details_drop);
        seatsView = findViewById(R.id.trip_details_seats);
        fareView = findViewById(R.id.trip_details_fare);
    }
}