package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    FragmentSchedule fragmentSchedule;
    FragmentHome fragmentHome;
    FragmentNewsFeed fragmentNewsFeed;
    FragmentFindBusLocation fragmentFindBusLocation;
    FragmentProfileMy fragmentProfileMy;
    FragmentPostCreate fragmentPostCreate;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private Toolbar toolbar;
    public static String DUREGNUM = "0";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getReg();
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
                    FirebaseAuth.getInstance().signOut();
//                    Intent intent = new Intent(getApplicationContext(), SignUpUser.class);
//                    startActivity(intent);
                    showToast("Will added later");
//                    openCreatePosFragment();
                } else if (itemId == R.id.details) {
                    Intent intent = new Intent(getApplicationContext(), DeveloperDetails.class);
                    startActivity(intent);
                } else if (itemId == R.id.sms) {
                    Intent intent = new Intent(getApplicationContext(), SendSMS.class);
                    startActivity(intent);
                } else if (itemId == R.id.email) {
                    Intent intent = new Intent(getApplicationContext(), SendEmail.class);
                    startActivity(intent);
                } else if (itemId == R.id.logOut) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), SignInUser.class);
                    startActivity(intent);
                    finish();
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
    private void getReg() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("UserInfo");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean ok = false;
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            InfoUser info = snapshot.getValue(InfoUser.class);
                            if (info != null) {
                                String t_email = String.valueOf(snapshot.child("email").getValue());
                                if (t_email.equals(email)) {
                                    DUREGNUM =  String.valueOf(snapshot.child("regNum").getValue());
                                    ok = true;
                                    break;
                                }
                                if (ok) break;
                            } else {
                                showToast("Something went wrong");
                            }
                        }
                        if (!ok) {
                            showToast("No data found");
                        }
                    } else {
                        showToast("Error");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error fetching data", databaseError.toException());
                }
            });
        }
    }
}