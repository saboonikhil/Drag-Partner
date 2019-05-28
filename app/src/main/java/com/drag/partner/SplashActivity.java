package com.drag.partner;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.drag.partner.model.Location;
import com.drag.partner.network.APIUtils;
import com.drag.partner.network.EndPointInterface;
import com.drag.partner.util.ObjectSerializer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends Activity {

    private static String TAG = "SplashActivity";
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pref = getSharedPreferences("AppPref", MODE_PRIVATE);

        EndPointInterface service = APIUtils.getAPIService();
        service.listLocation().enqueue(new Callback<Location[]>() {
            @Override
            public void onResponse(@NonNull Call<Location[]> call, @NonNull Response<Location[]> response) {
                if (response.body() != null) {
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("locations", ObjectSerializer.serialize(response.body()));
                    edit.apply();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Location[]> call, @NonNull Throwable t) {
                Log.e(TAG + " On Failure", t.getMessage());
            }
        });

        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}