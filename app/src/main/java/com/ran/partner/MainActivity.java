package com.ran.partner;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences pref;
    private DrawerLayout rootLayout;
    private Toolbar toolbarView;
    private NavigationView navigationDrawerView;
    private NestedScrollView nestedScrollView;
    private BottomNavigationView bottomNavigationView;
    private boolean isNavigationHidden = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariables();
        setSupportActionBar(toolbarView);

        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        String role = pref.getString("role", "");
        customLayout(role);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, rootLayout,
                toolbarView, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        rootLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationDrawerView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment frag;
                switch (item.getItemId()) {
                    case R.id.navigation_drawer_dashboard:
                        frag = new DashboardFragment();
                        ft.replace(R.id.main_content_frame, frag).commit();
                        break;

                    case R.id.navigation_drawer_connections:
                        frag = new ConnectionsFragment();
                        ft.replace(R.id.main_content_frame, frag).commit();
                        break;

                    case R.id.navigation_drawer_profile:
                        frag = new ProfileFragment();
                        ft.replace(R.id.main_content_frame, frag).commit();
                        break;

                    case R.id.navigation_drawer_cars:
                        frag = new CarsFragment();
                        ft.replace(R.id.main_content_frame, frag).commit();
                        break;

                    case R.id.navigation_drawer_logout:
                        showLogoutDialog();
                        break;

                    case R.id.navigation_drawer_support:
                        break;
                }
                rootLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < oldScrollY) { // up
                    animateBottomNavigation(false);
                }
                if (scrollY > oldScrollY) { // down
                    animateBottomNavigation(true);
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        displaySelectedScreen(R.id.bottom_navigation_ongoing);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag;
        switch (itemId) {
            case R.id.bottom_navigation_upcoming:
                frag = new UpcomingFragment();
                ft.replace(R.id.main_content_frame, frag).commit();
                break;

            case R.id.bottom_navigation_ongoing:
                frag = new OngoingFragment();
                ft.replace(R.id.main_content_frame, frag).commit();
                break;

            case R.id.bottom_navigation_completed:
                frag = new CompletedFragment();
                ft.replace(R.id.main_content_frame, frag).commit();
                break;
        }
        int size = navigationDrawerView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationDrawerView.getMenu().getItem(i).setChecked(false);
        }
    }

    private void animateBottomNavigation(final boolean hide) {
        if (isNavigationHidden && hide || !isNavigationHidden && !hide) return;
        isNavigationHidden = hide;
        int moveY = hide ? (2 * bottomNavigationView.getHeight()) : 0;
        bottomNavigationView.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }

    @Override
    public void onBackPressed() {
        if (rootLayout.isDrawerOpen(GravityCompat.START)) {
            rootLayout.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
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

    private void showLogoutDialog() {
        new AlertDialog.Builder(MainActivity.this, R.style.MaterialAlertDialogStyle)
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pref.edit().clear().apply();
                                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.
                                        FLAG_ACTIVITY_CLEAR_TASK);
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

                break;
            case "org":

                break;
            case "driver":
                navigationDrawerView.getMenu().removeItem(R.id.navigation_drawer_connections);
                navigationDrawerView.getMenu().removeItem(R.id.navigation_drawer_cars);
                break;
        }
    }

    private void initVariables() {
        rootLayout = findViewById(R.id.activity_main_layout);
        toolbarView = findViewById(R.id.main_toolbar);
        navigationDrawerView = findViewById(R.id.main_navigation_drawer);
        nestedScrollView = findViewById(R.id.main_nested_scroll);
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
    }
}
