package com.octagon.octagondu;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.view.View; // Import the missing View class

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class LocationList extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private LocationInfoAdapter locationInfoAdapter;
    private int delay = 10000; // 5 seconds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Add this line
        setContentView(R.layout.activity_location_list);

        String busName = "khonika";
        String busIime = "6:00";
        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Location").child(busName).child(busIime);

        recyclerView = findViewById(R.id.locationRecycleView); // Use findViewById to find the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Use 'this' since you are inside an Activity
        locationInfoAdapter = new LocationInfoAdapter(new ArrayList<>());
        recyclerView.setAdapter(locationInfoAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<LocationInfo> list = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        LocationInfo location = snapshot.getValue(LocationInfo.class);
                        if (location != null) {
                            list.add(location);
                        }
                    }
                    locationInfoAdapter = new LocationInfoAdapter(list);
                    recyclerView.setAdapter(locationInfoAdapter);
                } else {
                    // No data found for the given bus name
                    Toast.makeText(LocationList.this, "No data found for this bus name", Toast.LENGTH_SHORT).show(); // Use 'LocationList.this'
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
}