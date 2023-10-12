package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.MediaRouteButton;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    ScheduleFragment scheduleFragment;
    HomeFragment homeFragment;
    MapFragment mapFragment;
    private  Toolbar toolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        scheduleFragment = new ScheduleFragment();
        homeFragment = new HomeFragment();
        mapFragment = new MapFragment();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.home_button) {
                    openHomeFragment();
                    return true;
                }
                if(item.getItemId() == R.id.schedule_button) {
                    openScheduleFragment();
                    return true;
                }
                if(item.getItemId() == R.id.location_button) {
                    Intent intent = new Intent(getApplicationContext(), MyLocation.class);
                    startActivity(intent);
//                    openMapFragment();
                    return true;
                }
                return false;
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        // Set up the toggle for the navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        openHomeFragment();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.admin) {
                    Intent intent = new Intent(getApplicationContext(), DataEntry.class);
                    startActivity(intent);
                } else if (itemId == R.id.entry) {
                    Intent intent = new Intent(getApplicationContext(), Admin.class);
                    startActivity(intent);
                } else if (itemId == R.id.bug) {
                    Intent intent = new Intent(getApplicationContext(), LocationList.class);
                    startActivity(intent);
                    showToast("Will added later");
                } else if (itemId == R.id.details) {
                    Intent intent = new Intent(getApplicationContext(), DeveloperDetails.class);
                    startActivity(intent);
                } else if (itemId == R.id.sms) {
                    Intent intent = new Intent(getApplicationContext(), SMS.class);
                    startActivity(intent);
                } else if (itemId == R.id.email) {
                    Intent intent = new Intent(getApplicationContext(), Email.class);
                    startActivity(intent);
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    private void openHomeFragment() {
        toolbar.setVisibility(View.VISIBLE);
        getSupportActionBar().setTitle("Octagon");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, homeFragment)
                .commit();

    }
    private void openScheduleFragment() {
        toolbar.setVisibility(View.GONE);
        getSupportActionBar().setTitle("Schedule");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, scheduleFragment)
                .commit();
    }
    private void openMapFragment() {
        toolbar.setVisibility(View.GONE);
        getSupportActionBar().setTitle("Location");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, mapFragment)
                .commit();
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}