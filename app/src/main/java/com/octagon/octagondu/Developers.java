package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Developers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);
        init();
    }
    void init() {
        DrawerLayout drawerLayout;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        // Set up the toggle for the navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else if (itemId == R.id.admin) {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                }else if (itemId == R.id.entry) {
                    Intent intent = new Intent(getApplicationContext(), Admin.class);
                    startActivity(intent);
                }else if (itemId == R.id.bug) {
//                    Intent intent = new Intent(getApplicationContext(), Bug.class);
//                    startActivity(intent);
                    showToast("Will added later");
                }else if (itemId == R.id.details) {
                    Intent intent = new Intent(getApplicationContext(), Developers.class);
                    startActivity(intent);
                }else if (itemId == R.id.sms) {
                    Intent intent = new Intent(getApplicationContext(), SMS.class);
                    startActivity(intent);
                }else if (itemId == R.id.email) {
                    Intent intent = new Intent(getApplicationContext(), Email.class);
                    startActivity(intent);
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}