package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    DatabaseReference reference;
    TextView textView;
    Button button;
    ImageView imageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        textView = findViewById(R.id.uni);
//        reference = FirebaseDatabase.getInstance().getReference("Bus Name");
//        reference.child("Khonika").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if(task.isSuccessful()) {
//                    if(task.getResult().exists()) {
//                        DataSnapshot snapshot = task.getResult();
//                        String uni = String.valueOf(snapshot.child("type").getValue());
//                        String year = String.valueOf(snapshot.child("number").getValue());
//                        textView.setText("University " + uni);
//                        textView = findViewById(R.id.year);
//                        textView.setText("Session : " + year);
//                    }else {
//                        Toast.makeText(MainActivity.this, "Bus Name not found", Toast.LENGTH_SHORT).show();
//                    }
//                    Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(MainActivity.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
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
            String emailsend = "emon@gmail.com";
            String emailsubject = "fsff";
            String emailbody = "fjfsf";

            // define Intent object with action attribute as ACTION_SEND
            Intent intent = new Intent(Intent.ACTION_SEND);

            // add three fields to intent using putExtra function
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailsend});
            intent.putExtra(Intent.EXTRA_SUBJECT, emailsubject);
            intent.putExtra(Intent.EXTRA_TEXT, emailbody);

            // set type of intent
            intent.setType("message/rfc822");

            // startActivity with intent with chooser as Email client using createChooser function
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        });
        imageView = findViewById(R.id.callIcon);
        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SMS.class);
            startActivity(intent);
        });
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
                    Toast.makeText(MainActivity.this, "Home selected", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.support) {
                    Toast.makeText(MainActivity.this, "Support selected", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.share) {
                    Toast.makeText(MainActivity.this, "Share selected", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
}