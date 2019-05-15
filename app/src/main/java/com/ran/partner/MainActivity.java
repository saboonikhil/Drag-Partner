package com.ran.partner;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ran.partner.model.Partner;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences pref;
    private DrawerLayout rootView;
    private Toolbar toolbarView;
    private NavigationView navigationDrawerView;
    private int count = 0;

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
        String json = pref.getString("dbObj", "");
        Partner partner = new Gson().fromJson(json, Partner.class);
        if (partner != null) {
            nameView.setText(partner.getName());
            emailView.setText(partner.getEmail());
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, rootView,
                toolbarView, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        rootView.addDrawerListener(toggle);
        toggle.syncState();

        navigationDrawerView.setNavigationItemSelectedListener(this);
        navigationDrawerView.getMenu().getItem(0).setChecked(true);
        displaySelectedScreen(R.id.navigation_drawer_trips);
    }

    private void displaySelectedScreen(int itemId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (itemId) {
            case R.id.navigation_drawer_trips:
                count = 0;
                ft.replace(R.id.main_content_frame, new TripsFragment(), "Trips").commit();
                break;

            case R.id.navigation_drawer_profile:
                ft.replace(R.id.main_content_frame, new ProfileFragment(), "Profile").commit();
                break;

            case R.id.navigation_drawer_connections:
                ft.replace(R.id.main_content_frame, new ConnectionsFragment(), "Connections").commit();
                break;

            case R.id.navigation_drawer_cars:
                ft.replace(R.id.main_content_frame, new CarsFragment(), "Cars").commit();
                break;

            case R.id.navigation_drawer_logout:
                showLogoutDialog();
                break;

            case R.id.navigation_drawer_support:
                break;
        }
        rootView.closeDrawer(GravityCompat.START);
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(MainActivity.this, R.style.MaterialAlertDialogStyle)
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pref.edit().clear().apply();
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
        switch (role) {
            case "admin":
                navigationDrawerView.getMenu().removeItem(R.id.navigation_drawer_profile);
                break;
            case "org":

                break;
            case "driver":
                navigationDrawerView.getMenu().removeItem(R.id.navigation_drawer_connections);
                navigationDrawerView.getMenu().removeItem(R.id.navigation_drawer_cars);
                break;
        }
    }

    private void initViews() {
        rootView = findViewById(R.id.activity_main_layout);
        toolbarView = findViewById(R.id.main_toolbar);
        navigationDrawerView = findViewById(R.id.main_navigation_drawer);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    String riderContact = "+91 9876543210";
                    startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + riderContact)));
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        TripsFragment currentFragment = (TripsFragment) getSupportFragmentManager().findFragmentByTag("Trips");
        if (rootView.isDrawerOpen(GravityCompat.START)) {
            rootView.closeDrawer(GravityCompat.START);
        } else if (currentFragment != null && currentFragment.isVisible()) {
            count = count + 1;
            if (count == 1)
                Toast.makeText(MainActivity.this, "Press again to close Drag Partner", Toast.LENGTH_SHORT).show();
            else if (count == 2)
                finish();
        } else {
            navigationDrawerView.getMenu().getItem(0).setChecked(true);
            displaySelectedScreen(R.id.navigation_drawer_trips);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
