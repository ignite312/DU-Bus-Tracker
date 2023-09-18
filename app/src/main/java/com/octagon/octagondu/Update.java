package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Update extends AppCompatActivity {
    private Spinner spinnerBusName;
    private Spinner spinnerBusType;
    private TextView textViewBusId;
    private ImageView imageViewTime;
    private TextView textViewRouteSt;
    private TextView textViewRoute;
    private TextView viewtime;
    private String inputTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        init();

        // Retrieve values from the intent
        String busId = getIntent().getStringExtra("busId");
        String busName = getIntent().getStringExtra("busName");
        String busType = getIntent().getStringExtra("busType");
        String destinationLocation = getIntent().getStringExtra("destinationLocation");
        String startLocation = getIntent().getStringExtra("startLocation");
        String time = getIntent().getStringExtra("time");

        // Initialize your form fields with the received data
        spinnerBusName = findViewById(R.id.spinnerBusName);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                android.R.id.text1
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add(busName);
        spinnerBusName.setAdapter(adapter);

        spinnerBusName = findViewById(R.id.spinnerBusName);
        spinnerBusType = findViewById(R.id.spinnerBusType);
        textViewBusId = findViewById(R.id.textViewBusId);
        imageViewTime = findViewById(R.id.setTime);
        textViewRouteSt = findViewById(R.id.textViewRouteSt);
        textViewRoute = findViewById(R.id.textViewRoute);
        viewtime = findViewById(R.id.viewTime);

        // Set the received data to the form fields
        textViewBusId.setText(busId);
        textViewRouteSt.setText(startLocation);
        textViewRoute.setText(destinationLocation);
        viewtime.setText(time);
        if(busType.equals("Up"))spinnerBusType.setSelection(0);
        else spinnerBusType.setSelection(1);
        // Assuming spinnerBusType has a list of options, set the appropriate selection
        // Example: spinnerBusType.setSelection(getIndex(spinnerBusType, busType));
        // Make sure to create the getIndex method to find the index of the given value in the spinner

        inputTime = time; // Set the received time as the default value

        // Set click listener for the Submit button
        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected values from the spinners and the input fields
                String busName = spinnerBusName.getSelectedItem().toString();
                String busType = spinnerBusType.getSelectedItem().toString();
                String busId = textViewBusId.getText().toString();
                String time = inputTime;
                String startLocation = textViewRouteSt.getText().toString();
                String destinationLocation = textViewRoute.getText().toString();
                if (!busId.isEmpty() && !time.isEmpty() && !startLocation.isEmpty() && !destinationLocation.isEmpty()) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    BusInformation busInformation = new BusInformation(busName, busType, busId, startLocation, destinationLocation, time);
                    databaseReference.child("Bus Name").child(busName).child(time).setValue(busInformation);
                    Toast.makeText(Update.this, "Successfully Submitted Response", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Update.this, "Please Fill Completely", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showTimePickerDialog(View view) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Update the selected time in the TextView
                        Update.this.inputTime = String.format("%02d:%02d", hourOfDay, minute);
                        viewtime.setText(Update.this.inputTime);
                    }
                },
                hour,
                minute,
                true // Use true if you want 24-hour format, false for 12-hour format
        );

        // Show the dialog
        timePickerDialog.show();
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
                } else if (itemId == R.id.admin) {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                } else if (itemId == R.id.entry) {
                    Intent intent = new Intent(getApplicationContext(), Admin.class);
                    startActivity(intent);
                } else if (itemId == R.id.bug) {
                    //                    Intent intent = new Intent(getApplicationContext(), Bug.class);
                    //                    startActivity(intent);
                    showToast("Will be added later");
                } else if (itemId == R.id.details) {
                    Intent intent = new Intent(getApplicationContext(), Developers.class);
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

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
