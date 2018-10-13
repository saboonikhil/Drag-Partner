package com.ran.partner;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout rootLayout;
    private Toolbar toolbarView;
    private NavigationView navigationDrawerView;
    private NestedScrollView nestedScrollView;
    private BottomNavigationView bottomNavigationView;
    private boolean isNavigationHide = false;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariables();
        setSupportActionBar(toolbarView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, rootLayout, toolbarView, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        rootLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationDrawerView.setNavigationItemSelectedListener(this);
        navigationDrawerView.getMenu().getItem(0).setChecked(true);
        displaySelectedScreen(R.id.navigation_drawer_dashboard);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < oldScrollY) { // up
                    animateNavigation(false);
                }
                if (scrollY > oldScrollY) { // down
                    animateNavigation(true);
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_navigation_upcoming:
                        return true;
                    case R.id.bottom_navigation_ongoing:
                        return true;
                    case R.id.bottom_navigation_completed:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        switch (itemId) {
            case R.id.navigation_drawer_dashboard:
                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                NowFragment frag1 = new NowFragment();
                ft1.replace(R.id.main_content_frame, frag1);
                ft1.addToBackStack("Now");
                ft1.commit();
                break;

            case R.id.navigation_drawer_connections:
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                TripsFragment frag2 = new TripsFragment();
                ft2.replace(R.id.main_content_frame, frag2);
                ft2.addToBackStack("Trips");
                ft2.commit();
                break;

            case R.id.navigation_drawer_profile:
                break;

            case R.id.navigation_drawer_cars:
                FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                CarsFragment frag3 = new CarsFragment();
                ft3.replace(R.id.main_content_frame, frag3);
                ft3.addToBackStack("Cars");
                ft3.commit();
                break;

            case R.id.navigation_drawer_logout:
                break;

            case R.id.navigation_drawer_support:
                FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                AboutFragment frag4 = new AboutFragment();
                ft4.replace(R.id.main_content_frame, frag4);
                ft4.addToBackStack("About");
                ft4.commit();
                break;
        }
        rootLayout.closeDrawer(GravityCompat.START);
    }

    private void animateNavigation(final boolean hide) {
        if (isNavigationHide && hide || !isNavigationHide && !hide) return;
        isNavigationHide = hide;
        int moveY = hide ? (2 * bottomNavigationView.getHeight()) : 0;
        bottomNavigationView.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }

    @Override
    public void onBackPressed() {
        if (rootLayout.isDrawerOpen(GravityCompat.START)) {
            rootLayout.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            navigationDrawerView.getMenu().getItem(0).setChecked(true);
            displaySelectedScreen(R.id.navigation_drawer_cars);
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            count = count + 1;
            if (count == 1) {
                Toast.makeText(MainActivity.this, "Press again to close RAN Partner",
                        Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
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

    private void initVariables() {
        rootLayout = findViewById(R.id.activity_main_layout);
        toolbarView = findViewById(R.id.main_toolbar);
        navigationDrawerView = findViewById(R.id.main_navigation_drawer);
        nestedScrollView = findViewById(R.id.main_nested_scroll);
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
    }
}
