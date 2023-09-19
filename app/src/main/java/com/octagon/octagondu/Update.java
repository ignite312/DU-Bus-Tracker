package com.octagon.octagondu;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
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
    private String pastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Initialize your form fields with the received data
        String busId = getIntent().getStringExtra("busId");
        String busName = getIntent().getStringExtra("busName");
        String busType = getIntent().getStringExtra("busType");
        String destinationLocation = getIntent().getStringExtra("destinationLocation");
        String startLocation = getIntent().getStringExtra("startLocation");
        String time = getIntent().getStringExtra("time");
        pastTime = time;

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
        viewtime.setText("Departure Time "+time);
        if (busType.equals("Up")) spinnerBusType.setSelection(0);
        else spinnerBusType.setSelection(1);
        inputTime = time; // Set the received time as the default value

        // Set click listener for the Submit button
        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String busName = spinnerBusName.getSelectedItem().toString();
                String busType = spinnerBusType.getSelectedItem().toString();
                String busId = textViewBusId.getText().toString();
                String time = inputTime;
                String startLocation = textViewRouteSt.getText().toString();
                String destinationLocation = textViewRoute.getText().toString();

                if (!busId.isEmpty() && !time.isEmpty() && !startLocation.isEmpty() && !destinationLocation.isEmpty()) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    BusInformation busInformation = new BusInformation(busName, busType, busId, startLocation, destinationLocation, time);

                    // First, delete the existing data
                    deleteBus(busName, pastTime, databaseReference);
                } else {
                    showToast("Please Fill Completely");
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
                        viewtime.setText("Departure Time: " + Update.this.inputTime);
                    }
                },
                hour,
                minute,
                true // Use true if you want 24-hour format, false for 12-hour format
        );

        // Show the dialog
        timePickerDialog.show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void deleteBus(String busName, String pastTime, DatabaseReference databaseReference) {
        String pathToDelete = "Bus Name/" + busName + "/" + pastTime;

        databaseReference.child(pathToDelete).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Data deleted successfully, now insert the new data
                            insertBus(busName, inputTime);
                        } else {
                            showToast("Failed to Delete Data");
                        }
                    }
                });
    }

    private void insertBus(String busName, String time) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        BusInformation busInformation = new BusInformation(busName, spinnerBusType.getSelectedItem().toString(),
                textViewBusId.getText().toString(), textViewRouteSt.getText().toString(), textViewRoute.getText().toString(), time);

        String pathToInsert = "Bus Name/" + busName + "/" + time;

        databaseReference.child(pathToInsert).setValue(busInformation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Data inserted successfully
                            showToast("Successfully Submitted Response");
                            finish();
                        } else {
                            // Failed to insert data
                            showToast("Failed to Insert Data");
                        }
                    }
                });
    }
}
