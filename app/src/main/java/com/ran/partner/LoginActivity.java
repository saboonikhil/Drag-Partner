package com.ran.partner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button adminView, partnerView;
    private SharedPreferences pref;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupActionBar();
        initVariables();

        pref = getSharedPreferences("AppPref", MODE_PRIVATE);

        adminView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("role", "admin");
                edit.apply();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });

        partnerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("role", "partner");
                edit.apply();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void initVariables() {
        adminView = findViewById(R.id.button_admin);
        partnerView = findViewById(R.id.button_partner);
    }
}