package com.ran.partner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);
        displaySelectedScreen(R.id.nav_trips);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().getItem(0).setChecked(true);
            displaySelectedScreen(R.id.nav_add_car);
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            count = count + 1;
            if (count == 1) {
                Toast.makeText(MainActivity.this, "Press again to close RAN",
                        Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        }
    }

    private void displaySelectedScreen(int itemId) {

        switch (itemId) {

            case R.id.nav_trips:
                break;

            case R.id.nav_profile:
                break;

            case R.id.nav_add_car:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                AddCarFragment frag3 = new AddCarFragment();
                ft.replace(R.id.content_frame, frag3);
                ft.addToBackStack("Add Car");
                ft.commit();
                break;

            case R.id.nav_logout:
                break;

            case R.id.nav_about:
                break;

            case R.id.nav_terms_of_use:
                break;

            case R.id.nav_feedback:
                break;

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }
}
