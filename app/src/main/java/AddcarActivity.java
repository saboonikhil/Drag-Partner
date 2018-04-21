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

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddcarActivity extends AppCompatActivity {
    private static final String TAG = "AddcarActivity";

    @BindView(R.id.destination) EditText _destinationText;
    @BindView(R.id.car_name) EditText _carText;
    @BindView(R.id.driver_name) EditText _driverText;
    @BindView(R.id.driver_number) EditText _mobileText;
    @BindView(R.id.car_number) EditText _numberText;
    @BindView(R.id.org_name) EditText _orgText;
    @BindView(R.id.btn_addcar) Button _addcarButton;
    @BindView(R.id.link_login) TextView _loginLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcar);
        ButterKnife.bind(this);

        _addcarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
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

        _addcarButton.setEnabled(false);

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
        _addcarButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }
}