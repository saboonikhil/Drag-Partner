package com.bonvoyage.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button placeButton;
    private Button addcarLink;
    private Button blockButton;

    private static final String TAG = "MainActivity";
    private static final int REQUEST_ADDCAR = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placeButton = (Button) findViewById(R.id.addplace);
        addcarLink = (Button) findViewById(R.id.addcar);
        blockButton = (Button) findViewById(R.id.blockuser);

        Intent intent = new Intent(this, com.bonvoyage.admin.LoginActivity.class);
        startActivity(intent);

        addcarLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Car Registration activity
                Intent intent = new Intent(getApplicationContext(), com.bonvoyage.admin.AddcarActivity.class);
                startActivityForResult(intent, REQUEST_ADDCAR);
                finish();
                overridePendingTransition(com.bonvoyage.admin.R.anim.push_left_in, com.bonvoyage.admin.R.anim.push_left_out);
            }
        });
    }
}
