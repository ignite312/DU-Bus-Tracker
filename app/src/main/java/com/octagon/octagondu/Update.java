package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        setContentView(R.layout.activity_update);
        String busId = getIntent().getStringExtra("busId");
        String busName = getIntent().getStringExtra("busName");
        String busType = getIntent().getStringExtra("busType");
        String destinationLocation = getIntent().getStringExtra("destinationLocation");
        String startLocation = getIntent().getStringExtra("startLocation");
        String time = getIntent().getStringExtra("time");
        String pastTime = time;

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
        inputTime = time; // Set the received time as the default value

        // Set click listener for the Submit button
        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBus(busName, pastTime);
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
                    showToast("Successfully Submitted Response");
                } else {
                    showToast("Please Fill Completely");
                }
                finish();
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
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void deleteBus(String busName, String pastTime) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Construct the path to the data you want to update
        String pathToDelete = "Bus Name/" + busName + "/" + pastTime;

        databaseReference.child(pathToDelete).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Data exists, proceed with deletion
                    databaseReference.child(pathToDelete).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Data deleted successfully
                                        showToast("Data Deleted Successfully");
                                    } else {
                                        // Failed to delete data
                                        showToast("Failed to Delete Data");
                                    }
                                }
                            });
                } else {
                    // Data does not exist for the specified bus name and time
                    showToast("Data Not Found for Bus Name: " + busName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here, if needed
            }
        });

    }
}
