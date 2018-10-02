package com.ran.partner;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
        displaySelectedScreen(R.id.nav_now);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().getItem(0).setChecked(true);
            displaySelectedScreen(R.id.nav_cars);
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

    private void displaySelectedScreen(int itemId) {

        switch (itemId) {

            case R.id.nav_now:
                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                NowFragment frag1 = new NowFragment();
                ft1.replace(R.id.content_frame, frag1);
                ft1.addToBackStack("Now");
                ft1.commit();
                break;

            case R.id.nav_trips:
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                TripsFragment frag2 = new TripsFragment();
                ft2.replace(R.id.content_frame, frag2);
                ft2.addToBackStack("Trips");
                ft2.commit();
                break;

            case R.id.nav_profile:
                break;

            case R.id.nav_cars:
                FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                CarsFragment frag3 = new CarsFragment();
                ft3.replace(R.id.content_frame, frag3);
                ft3.addToBackStack("Cars");
                ft3.commit();
                break;

            case R.id.nav_logout:
                break;

            case R.id.nav_support:
                FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                AboutFragment frag4 = new AboutFragment();
                ft4.replace(R.id.content_frame, frag4);
                ft4.addToBackStack("About");
                ft4.commit();
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
}
