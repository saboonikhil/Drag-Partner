package com.bonvoyage.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CarAdditionActivity extends AppCompatActivity {

    private EditText seatsAvailableText;
    private EditText carNameText;
    private EditText driverNameText;
    private EditText driverMobileText;
    private EditText carNumberText;
    private EditText orgNameText;
    private Button addcarButton;
    private TextView loginLink;
    private String seatsAvailable, carName, driverName, driverMobile, carNumber, orgName;


    private static final String TAG = "CarAdditionActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        setupActionBar();

        seatsAvailableText=(EditText)findViewById(R.id.seats_available);
        carNameText=(EditText)findViewById(R.id.car_name);
        driverNameText=(EditText)findViewById(R.id.driver_name);
        driverMobileText=(EditText)findViewById(R.id.driver_number);
        carNumberText=(EditText)findViewById(R.id.car_number);
        orgNameText=(EditText)findViewById(R.id.org_name);

        addcarButton = (Button) findViewById(R.id.btn_addcar);
        loginLink = (TextView) findViewById(R.id.link_login);

        addcarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCar();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the car registration screen and return to the Main activity
                Intent intent = new Intent(CarAdditionActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(com.bonvoyage.admin.R.anim.push_left_in, com.bonvoyage.admin.R.anim.push_left_out);
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

        seatsAvailable = seatsAvailableText.getText().toString();
        carName = carNameText.getText().toString();
        driverName = driverNameText.getText().toString();
        driverMobile = driverMobileText.getText().toString();
        carNumber = carNumberText.getText().toString();
        orgName = orgNameText.getText().toString();

        CarDetailsAdmin carDetailsAdmin = new CarDetailsAdmin(carName,"", "Available to book",
                "", "", seatsAvailable, driverName, driverMobile, carNumber, orgName,false);

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference mRefCarsAvailable = database.getReference().child("Cars Available").push();
        mRefCarsAvailable.setValue(carDetailsAdmin);
        Toast.makeText(this, "Car added successfully!",
                Toast.LENGTH_SHORT).show();

    }
}