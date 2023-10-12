package com.octagon.octagondu;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

public class ListBusLocation extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private AdapterBusLocation adapterBusLocation;
    private int delay = 10000; // 5 seconds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Add this line
        setContentView(R.layout.activity_list_location);

        String busName = "khonika";
        String busIime = "6:00";
        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Location").child(busName).child(busIime);

        recyclerView = findViewById(R.id.locationRecycleView); // Use findViewById to find the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Use 'this' since you are inside an Activity
        adapterBusLocation = new AdapterBusLocation(new ArrayList<>());
        recyclerView.setAdapter(adapterBusLocation);

        databaseReference.addValueEventListener(new ValueEventListener() {
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
                    adapterBusLocation = new AdapterBusLocation(list);
                    recyclerView.setAdapter(adapterBusLocation);
                } else {
                    // No data found for the given bus name
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
}