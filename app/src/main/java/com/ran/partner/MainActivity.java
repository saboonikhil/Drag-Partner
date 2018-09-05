package com.ran.partner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_ADDCAR = 0;
    private Button AllotDriverLink;
    private Button AddPlaceLink;
    private Button AddCarLink;
    private Button AddDriverLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActionBar();

        AllotDriverLink = (Button) findViewById(R.id.allot_driver);
        AddPlaceLink = (Button) findViewById(R.id.add_place);
        AddCarLink = (Button) findViewById(R.id.add_car);
        AddDriverLink = (Button) findViewById(R.id.add_driver);

        AllotDriverLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.ran.partner.DriverAllotmentActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        AddPlaceLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.ran.partner.PlaceAdditionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        AddCarLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.ran.partner.CarAdditionActivity.class);
                startActivityForResult(intent, REQUEST_ADDCAR);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        AddDriverLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.ran.partner.DriverAdditionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
}
