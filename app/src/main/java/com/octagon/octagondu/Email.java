package com.octagon.octagondu;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class Email extends AppCompatActivity {
    private EditText subject;
    private EditText body;
    private Button send;
    String selectedBusNumber, receiver;

    private final int CONA = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        init();
        subject = findViewById(R.id.subject);
        body = findViewById(R.id.body);
        Spinner spinnerNumber = findViewById(R.id.spinnerNumber);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.devNumber, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNumber.setAdapter(adapter);

        spinnerNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item
                selectedBusNumber = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here if needed
            }
        });

        send = findViewById(R.id.send);
        send.setOnClickListener(view -> {
            receiver = "contact.emonkhan@gmail.com";
            if(Objects.equals(selectedBusNumber, "Sajid Hasan"))receiver = "mahmudulhasanshajid@gmail.com";
            else if(Objects.equals(selectedBusNumber, "Emon Khan"))receiver = "contact.emonkhan@gmail.com";
            else if(Objects.equals(selectedBusNumber, "Atikur Hridoy"))receiver = "arhridoy2002@gmail.com";
            String subjectText = subject.getText().toString();
            String bodyText = body.getText().toString();

            // define Intent object with action attribute as ACTION_SEND
            Intent intent = new Intent(Intent.ACTION_SEND);

            // add three fields to intent using putExtra function
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{receiver});
            intent.putExtra(Intent.EXTRA_SUBJECT, subjectText);
            intent.putExtra(Intent.EXTRA_TEXT, bodyText);

            // set type of intent
            intent.setType("message/rfc822");

            // startActivity with intent with chooser as Email client using createChooser function
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        });
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
