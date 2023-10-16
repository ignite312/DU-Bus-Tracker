package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    FragmentSchedule fragmentSchedule;
    FragmentHome fragmentHome;
    FragmentNewsFeed fragmentNewsFeed;
    FragmentFindBusLocation fragmentFindBusLocation;
    FragmentProfileMy fragmentProfileMy;
    FragmentPostCreate fragmentPostCreate;

    private  Toolbar toolbar;
    public static String userRegUnique;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userRegUnique = "mara";
//        userRegUnique = getIntent().getStringExtra("userId");
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        fragmentHome = new FragmentHome();
        fragmentSchedule = new FragmentSchedule();
        fragmentNewsFeed = new FragmentNewsFeed();
        fragmentFindBusLocation = new FragmentFindBusLocation();
        fragmentProfileMy = new FragmentProfileMy();
        fragmentPostCreate = new FragmentPostCreate();

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
                    openFindBusLocation();
                    return true;
                }
                if(item.getItemId() == R.id.feed_button) {
                    openFeedFragment();
                    return true;
                }
                if(item.getItemId() == R.id.profile_button) {
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
                    Intent intent = new Intent(getApplicationContext(), LoginDeveloper.class);
                    startActivity(intent);
                } else if (itemId == R.id.entry) {
                    Intent intent = new Intent(getApplicationContext(), LoginAdmin.class);
                    startActivity(intent);
                } else if (itemId == R.id.bug) {
//                    Intent intent = new Intent(getApplicationContext(), CreatePost.class);
//                    startActivity(intent);
//                    showToast("Will added later");
                    openCreatePosFragment();
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
    private void openHomeFragment() {
        toolbar.setVisibility(View.VISIBLE);
        getSupportActionBar().setTitle("Octagon");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragmentHome)
                .commit();

    }
    private void openScheduleFragment() {
        toolbar.setVisibility(View.GONE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragmentSchedule)
                .commit();
    }
    private void openFindBusLocation() {
        toolbar.setVisibility(View.GONE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragmentFindBusLocation)
                .commit();
    }
    private void openFeedFragment() {
        toolbar.setVisibility(View.GONE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragmentNewsFeed)
                .commit();
    }
    private void openProfileFragment() {
        toolbar.setVisibility(View.GONE);
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
        getSupportActionBar().setTitle("Octagon");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragmentPostCreate)
                .commit();

    }
}