package com.drag.partner;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.drag.partner.model.Location;
import com.drag.partner.model.Partner;
import com.drag.partner.network.APIUtils;
import com.drag.partner.network.EndPointInterface;
import com.drag.partner.util.ObjectSerializer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences pref;
    private String token;
    private Partner partner;
    private DrawerLayout rootView;
    private Toolbar toolbarView;
    private NavigationView navigationDrawerView;
    private int count = 0;
    private BottomNavigationView bottomNavigationView;
    private boolean isNavigationHidden = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setSupportActionBar(toolbarView);

        View navHeader = navigationDrawerView.getHeaderView(0);
        TextView nameView = navHeader.findViewById(R.id.navigation_drawer_name);
        TextView emailView = navHeader.findViewById(R.id.navigation_drawer_email);

        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        token = pref.getString("token", "");
        String json = pref.getString("dbObj", "");
        partner = new Gson().fromJson(json, Partner.class);

        if (partner != null) {
            customLayout(partner.getRole());
            nameView.setText(partner.getName());
            emailView.setText(partner.getEmail());

            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (!task.isSuccessful()) {
                        Log.w("FCM TOKEN Failed", task.getException());
                    } else {
                        if (task.getResult() != null)
                            Log.i("FCM TOKEN", task.getResult().getToken());
                    }
                }
            });
            FirebaseMessaging.getInstance().subscribeToTopic("new_requests");
            getAuthLocations();
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, rootView,
                toolbarView, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        rootView.addDrawerListener(toggle);
        toggle.syncState();

        navigationDrawerView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_drawer_home:
                        displaySelectedScreen(R.id.bottom_navigation_requests);
                        break;

                    case R.id.navigation_drawer_profile:
                        animateBottomNavigation(true);
                        ft.replace(R.id.main_content_frame, new ProfileFragment(), "Profile").commit();
                        break;

                    case R.id.navigation_drawer_connections:
                        animateBottomNavigation(true);
                        ft.replace(R.id.main_content_frame, new ConnectionsFragment(), "Connections").commit();
                        break;

                    case R.id.navigation_drawer_logout:
                        showLogoutDialog();
                        break;

                    case R.id.navigation_drawer_support:
                        callAction();
                        break;
                }
                rootView.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.bottom_navigation_requests);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getAuthLocations() {
        EndPointInterface service = APIUtils.getAPIService(MainActivity.this);
        service.authLocation(partner.getEmail(), token, com.drag.partner.BuildConfig.VERSION_CODE).enqueue(new Callback<Location[]>() {
            @Override
            public void onResponse(@NonNull Call<Location[]> call, @NonNull Response<Location[]> response) {
                if (response.code() == 401) {
                    pref.edit().remove("token").apply();
                    pref.edit().remove("expires").apply();
                    pref.edit().remove("dbObj").apply();
                    Toast.makeText(getApplicationContext(),
                            "Your account is blocked. Please contact help desk for recovery.", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                } else if (response.code() == 403) {
                    pref.edit().remove("token").apply();
                    pref.edit().remove("expires").apply();
                    pref.edit().remove("dbObj").apply();
                    Toast.makeText(getApplicationContext(),
                            "Please update the app with the latest version from the play store.", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                } else if (response.body() != null) {
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
    }

    private void displaySelectedScreen(int itemId) {
        navigationDrawerView.getMenu().getItem(0).setChecked(true);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (itemId) {
            case R.id.bottom_navigation_requests:
                animateBottomNavigation(false);
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                ft.replace(R.id.main_content_frame, new RequestsFragment(), "Requests").commit();
                break;

            case R.id.bottom_navigation_trips:
                ft.replace(R.id.main_content_frame, new TripsFragment(), "Trips").commit();
                break;
        }
    }

    public void animateBottomNavigation(final boolean hide) {
        if (isNavigationHidden && hide || !isNavigationHidden && !hide) return;
        isNavigationHidden = hide;
        int moveY = hide ? (2 * bottomNavigationView.getHeight()) : 0;
        bottomNavigationView.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(MainActivity.this, R.style.MaterialAlertDialogStyle)
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pref.edit().remove("token").apply();
                                pref.edit().remove("expires").apply();
                                pref.edit().remove("dbObj").apply();
                                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }
                        })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void customLayout(String role) {
        if ("admin".equals(role)) {
            navigationDrawerView.getMenu().removeItem(R.id.navigation_drawer_profile);
        } else {
            navigationDrawerView.getMenu().removeItem(R.id.navigation_drawer_connections);
        }
    }

    private void callAction() {
        String helpDesk = "+91 7010823612";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + helpDesk));

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Calling permission is revoked");
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            Log.v(TAG, "Calling permission is granted");
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                callAction();
            } else {
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        RequestsFragment currentFragment = (RequestsFragment) getSupportFragmentManager().findFragmentByTag("Requests");
        if (rootView.isDrawerOpen(GravityCompat.START)) {
            rootView.closeDrawer(GravityCompat.START);
        } else if (currentFragment != null && currentFragment.isVisible()) {
            count = count + 1;
            if (count == 1)
                Toast.makeText(MainActivity.this, "Tap again to exit Drag Partner", Toast.LENGTH_SHORT).show();
            else if (count == 2)
                finish();
        } else {
            displaySelectedScreen(R.id.bottom_navigation_requests);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void initViews() {
        rootView = findViewById(R.id.main_activity_layout);
        toolbarView = findViewById(R.id.main_toolbar);
        navigationDrawerView = findViewById(R.id.main_navigation_drawer);
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
    }
}