package com.octagon.octagondu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListBusLocation extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private AdapterBusLocation adapterBusLocation;
    private final int delay = 10000; // 5 seconds
    private String busName, busTime;
    MaterialToolbar detailsBusToolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Add this line
        setContentView(R.layout.activity_list_bus_location);
        busName = getIntent().getStringExtra("BUSNAME");
        busTime = getIntent().getStringExtra("BUSTIME");
        detailsBusToolbar = findViewById(R.id.locationForFixedTimeAppBar);
        detailsBusToolbar.setTitle("Locations for " + busName + " " + busTime);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        TextView textView = findViewById(R.id.shareYourLocation);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListBusLocation.this, LocationMy.class);
                intent.putExtra("BUSNAME", busName);
                intent.putExtra("BUSTIME", busTime);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.locationRecycleView); // Use findViewById to find the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Use 'this' since you are inside an Activity
        adapterBusLocation = new AdapterBusLocation(ListBusLocation.this, new ArrayList<>());
        recyclerView.setAdapter(adapterBusLocation);

        databaseReference = database.getReference("Location").child(getCurrentDateFormatted()).child(busName).child(busTime);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<InfoBusLocation> list = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        InfoBusLocation location = snapshot.getValue(InfoBusLocation.class);
                        if (location != null) {
                            list.add(location);
                        }
                    }
                    adapterBusLocation = new AdapterBusLocation(ListBusLocation.this, list);
                    recyclerView.setAdapter(adapterBusLocation);
                } else {
                    Toast.makeText(ListBusLocation.this, "No data found for this bus name", Toast.LENGTH_SHORT).show(); // Use 'LocationList.this'
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public String getCurrentDateFormatted() {
        // Get the current date
        Date currentDate = new Date();

        // Create a SimpleDateFormat object to format the date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        // Format the date and return it as a string
        return dateFormat.format(currentDate);
    }
    public String getCurrentTime24HourFormat() {
        // Get the current time
        Date currentTime = new Date();

        // Create a SimpleDateFormat object to format the time
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        // Format the time and return it as a string
        return timeFormat.format(currentTime);
    }
}