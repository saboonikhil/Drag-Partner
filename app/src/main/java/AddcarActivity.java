package com.bonvoyage.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddcarActivity extends AppCompatActivity {

    private EditText destinationText;
    private EditText carText;
    private EditText driverText;
    private EditText mobileText;
    private EditText numberText;
    private EditText orgText;
    private Button addcarButton;
    private TextView loginLink;

    private static final String TAG = "AddcarActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcar);

        destinationText = (EditText) findViewById(R.id.destination);
        carText = (EditText) findViewById(R.id.car_name);
        driverText = (EditText) findViewById(R.id.driver_name);
        mobileText = (EditText) findViewById(R.id.driver_number);
        numberText = (EditText) findViewById(R.id.car_number);
        orgText = (EditText) findViewById(R.id.org_name);
        addcarButton = (Button) findViewById(R.id.btn_addcar);
        loginLink = (TextView) findViewById(R.id.link_login);

        addcarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the car registration screen and return to the Main activity
                Intent intent = new Intent(getApplicationContext(), com.bonvoyage.admin.MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(com.bonvoyage.admin.R.anim.push_left_in, com.bonvoyage.admin.R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Addcar");

        addcarButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(AddcarActivity.this,
                com.bonvoyage.admin.R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding Car...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onAddcarSuccess or onAddcarFailed
                        // depending on success
                        onAddcarSuccess();
                        // onAddcarFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onAddcarSuccess() {
        addcarButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }
}