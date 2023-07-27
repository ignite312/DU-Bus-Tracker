package com.octagon.octagondu;

// BusDetailsActivity.java
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BusDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);

        // Retrieve the selected bus name from extras
        String busName = getIntent().getStringExtra("busName");

        // TODO: Use the busName to display the relevant information in the tables (up and down time).
        // You can use this information to fetch data from a database or any other data source and populate the tables accordingly.
        // For demonstration purposes, you can display the busName in a TextView or simply log it:
        TextView tvBusName = findViewById(R.id.tvBusName);
        tvBusName.setText(busName);
    }
}