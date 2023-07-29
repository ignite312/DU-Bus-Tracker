package com.octagon.octagondu;

// BusDetailsActivity.java
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class BusDetailsActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private BusAdapter2 busAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);

        // Retrieve the selected bus name from extras
        String busName = getIntent().getStringExtra("busName");
        busName = busName.substring(3);
        Toast.makeText(BusDetailsActivity.this, busName, Toast.LENGTH_SHORT).show();
        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Bus Name").child(busName);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        busAdapter = new BusAdapter2(new ArrayList<>());
        recyclerView.setAdapter(busAdapter);

        // Fetch data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BusInformation> busList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        BusInformation bus = snapshot.getValue(BusInformation.class);
                        if (bus != null) {
                            Toast.makeText(BusDetailsActivity.this, "YEEEEh", Toast.LENGTH_SHORT).show();
                            busList.add(bus);
                        } else {
                            Toast.makeText(BusDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // No data found for the given bus name
                    Toast.makeText(BusDetailsActivity.this, "No data found for this bus name", Toast.LENGTH_SHORT).show();
                }
                busAdapter = new BusAdapter2(busList);
                recyclerView.setAdapter(busAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
        // TODO: Use the busName to display the relevant information in the tables (up and down time).
        // You can use this information to fetch data from a database or any other data source and populate the tables accordingly.
        // For demonstration purposes, you can display the busName in a TextView or simply log it:
        TextView tvBusName = findViewById(R.id.tvBusName);
        tvBusName.setText(busName);
    }
}