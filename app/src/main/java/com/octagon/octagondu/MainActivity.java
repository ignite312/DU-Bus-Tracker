package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    FragmentSchedule fragmentSchedule;
    FragmentHome fragmentHome;
    FragmentNewsFeed fragmentNewsFeed;
    FragmentLocation fragmentLocation;
    FragmentProfileMy fragmentProfileMy;
    FragmentPostCreate fragmentPostCreate;
    Boolean ok = false;
    private Toolbar toolbar;
    Button logout_button;
    public static String DUREGNUM = "0";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DUREGNUM = getIntent().getStringExtra("DUREGNUM");
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentHome = new FragmentHome();
        fragmentSchedule = new FragmentSchedule();
        fragmentNewsFeed = new FragmentNewsFeed();
        fragmentLocation = new FragmentLocation();
        fragmentProfileMy = new FragmentProfileMy();
        fragmentPostCreate = new FragmentPostCreate();

        logout_button = findViewById(R.id.logout);
        logout_button.setOnClickListener(View -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), SignInUser.class);
            startActivity(intent);
            finish();
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home_button) {
                    openHomeFragment();
                    return true;
                }
                if (item.getItemId() == R.id.schedule_button) {
                    openScheduleFragment();
                    return true;
                }
                if (item.getItemId() == R.id.location_button) {
                    openFindBusLocation();
                    return true;
                }
                if (item.getItemId() == R.id.feed_button) {
                    openFeedFragment();
                    return true;
                }
                if (item.getItemId() == R.id.profile_button) {
                    openProfileFragment();
                    return true;
                }
                return false;
            }
        });

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        openHomeFragment();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.admin) {
                    Intent intent = new Intent(getApplicationContext(), LoginDeveloper.class);
                    startActivity(intent);
                } else if (itemId == R.id.entry) {
                    Intent intent = new Intent(getApplicationContext(), LoginAdmin.class);
                    startActivity(intent);
                } else if (itemId == R.id.bug) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), SignInUser.class);
                    startActivity(intent);
                    finish();
                } else if (itemId == R.id.details) {
                    Intent intent = new Intent(getApplicationContext(), DeveloperDetails.class);
                    startActivity(intent);
                } else if (itemId == R.id.sms) {
                    Intent intent = new Intent(getApplicationContext(), SendSMS.class);
                    startActivity(intent);
                } else if (itemId == R.id.email) {
                    Intent intent = new Intent(getApplicationContext(), SendEmail.class);
                    startActivity(intent);
                }
                /*
                else if (itemId == R.id.logOut) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), SignInUser.class);
                    startActivity(intent);
                    finish();
                }
                */
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void openHomeFragment() {
        if (!ok) setup();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragmentHome)
                .commit();

    }

    private void openScheduleFragment() {
        if (ok) undoSetup();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragmentSchedule)
                .commit();
    }

    private void openFindBusLocation() {
        if (ok) undoSetup();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragmentLocation)
                .commit();
    }

    private void openFeedFragment() {
        if (ok) undoSetup();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragmentNewsFeed)
                .commit();
    }

    private void openProfileFragment() {
        if (!ok) setup();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragmentProfileMy)
                .commit();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void openCreatePosFragment() {
        toolbar.setVisibility(View.GONE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragmentPostCreate)
                .commit();

    }


    private void setup() {
        ok = true;
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        // Set up the toggle for the navigation drawer
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        openHomeFragment();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.admin) {
                    Intent intent = new Intent(getApplicationContext(), LoginDeveloper.class);
                    startActivity(intent);
                } else if (itemId == R.id.entry) {
                    Intent intent = new Intent(getApplicationContext(), LoginAdmin.class);
                    startActivity(intent);
                } else if (itemId == R.id.bug) {
//                    Intent intent = new Intent(getApplicationContext(), ProfileOthers.class);
//                    startActivity(intent);
                    showToast("Will added later");
                } else if (itemId == R.id.details) {
                    Intent intent = new Intent(getApplicationContext(), DeveloperDetails.class);
                    startActivity(intent);
                } else if (itemId == R.id.sms) {
                    Intent intent = new Intent(getApplicationContext(), SendSMS.class);
                    startActivity(intent);
                } else if (itemId == R.id.email) {
                    Intent intent = new Intent(getApplicationContext(), SendEmail.class);
                    startActivity(intent);
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void undoSetup() {
        ok = false;
        // Remove the toggle for the navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.removeDrawerListener(toggle);

        // Clear the navigation view's item selection listener
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(null);

        // Set a custom home button indicator (an empty drawable)
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.transparent_drawable); // Create an empty drawable
        }
        // Disable the drawer swipe gesture
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
}