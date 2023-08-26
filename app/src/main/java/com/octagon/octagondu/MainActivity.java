package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    DatabaseReference reference;
    TextView textView;
    Button button;
    ImageView imageView;
    private TextView selectedTimeTextView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.DataEntry);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
        button = findViewById(R.id.schedule);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BusList.class);
                startActivity(intent);
            }
        });
        button = findViewById(R.id.developer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Developers.class);
                startActivity(intent);
            }
        });
        imageView = findViewById(R.id.sendEmailIcon);
        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Email.class);
            startActivity(intent);
        });
        imageView = findViewById(R.id.callIcon);
        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SMS.class);
            startActivity(intent);
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        int itemId = menuItem.getItemId();

                        if (itemId == R.id.nav_item1) {
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                        } else if (itemId == R.id.nav_item2) {
                            Intent intent = new Intent(getApplicationContext(), Developers.class);
                            startActivity(intent);
                        } else if (itemId == R.id.nav_item3) {
                            Intent intent = new Intent(getApplicationContext(), Email.class);
                            startActivity(intent);
                        }else if (itemId == R.id.nav_item4) {
                            Intent intent = new Intent(getApplicationContext(), SMS.class);
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