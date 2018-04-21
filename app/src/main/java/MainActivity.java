package com.bonvoyage.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.util.Log;
import android.content.Intent;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_ADDCAR = 0;

    @BindView(R.id.addplace) Button _placeButton;
    @BindView(R.id.addcar) Button _addcarLink;
    @BindView(R.id.blockuser) Button _blockButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Intent intent = new Intent(this, com.bonvoyage.admin.LoginActivity.class);
        startActivity(intent);

        _addcarLink.setOnClickListener(new View.OnClickListener() {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.bonvoyage.admin.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.bonvoyage.admin.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
