package com.octagon.octagondu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataEntry extends AppCompatActivity {

    private Spinner spinnerBusName;
    private Spinner spinnerBusType;
    private TextView textViewBusId;
    private TextView textViewTime;
    private TextView textViewRouteSt;
    private TextView textViewRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);

        // Find views
        spinnerBusName = findViewById(R.id.spinnerBusName);
        spinnerBusType = findViewById(R.id.spinnerBusType);
        textViewBusId = findViewById(R.id.textViewBusId);
        textViewTime = findViewById(R.id.textViewTime);
        textViewRouteSt = findViewById(R.id.textViewRouteSt);
        textViewRoute = findViewById(R.id.textViewRoute);

        // Set click listener for the Submit button
        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected values from the spinners and the input fields
                String busName = spinnerBusName.getSelectedItem().toString();
                String busType = spinnerBusType.getSelectedItem().toString();
                String busId = textViewBusId.getText().toString();
                String time = textViewTime.getText().toString();
                String startLocation = textViewRouteSt.getText().toString();
                String destinationLocation = textViewRoute.getText().toString();
                if (!busId.isEmpty() && !time.isEmpty() && !startLocation.isEmpty() && !destinationLocation.isEmpty()) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    BusInformation busInformation = new BusInformation(busType, busId, startLocation, destinationLocation);
                    databaseReference.child("Bus Name").child(busName).child(time).setValue(busInformation);
                    Toast.makeText(DataEntry.this, "Successfully Submitted Response", Toast.LENGTH_SHORT).show();
                    clearForm();
                } else {
                    Toast.makeText(DataEntry.this, "Please Fill Completely", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void clearForm () {
        // Clear TextViews
        textViewBusId.setText("");
        textViewTime.setText("");
        textViewRouteSt.setText("");
        textViewRoute.setText("");

        // Reset Spinners to default selection (usually the first item)
        spinnerBusName.setSelection(0);
        spinnerBusType.setSelection(0);
    }
}
