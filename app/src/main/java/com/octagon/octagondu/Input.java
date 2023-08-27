package com.octagon.octagondu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Input extends AppCompatActivity {
    Spinner spinnerBusName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        String busName = getIntent().getStringExtra("BUS_NAME_EXTRA");
        spinnerBusName = findViewById(R.id.spinnerBusName);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                android.R.id.text1
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add(busName);
        spinnerBusName.setAdapter(adapter);
    }
}