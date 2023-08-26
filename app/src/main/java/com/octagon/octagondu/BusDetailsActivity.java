package com.octagon.octagondu;

// BusDetailsActivity.java
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
    private RecyclerView recyclerView, recyclerView3;
    private BusAdapter2 busAdapter, busAdapter3;
    private ProgressBar progressBar1, progressBar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);
        progressBar1 = findViewById(R.id.progress_bar1);
        progressBar2 = findViewById(R.id.progress_bar2);
        // Retrieve the selected bus name from extras
        String busName = getIntent().getStringExtra("busName");
        busName = busName.substring(3);
        while(!((busName.charAt(0) >= 'a' && busName.charAt(0) <= 'z') ||
                (busName.charAt(0) >= 'A' && busName.charAt(0) <= 'Z'))) {
            busName = busName.substring(1);
        }
        Toast.makeText(BusDetailsActivity.this, busName, Toast.LENGTH_SHORT).show();
        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Bus Name").child(busName);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        busAdapter = new BusAdapter2(new ArrayList<>());
        recyclerView.setAdapter(busAdapter);

        recyclerView3 = findViewById(R.id.recycler_view3);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        busAdapter3 = new BusAdapter2(new ArrayList<>());
        recyclerView3.setAdapter(busAdapter3);

        // Fetch data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BusInformation> busList1 = new ArrayList<>();
                List<BusInformation> busList3 = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        BusInformation bus = snapshot.getValue(BusInformation.class);
                        if (bus != null) {
                            String temp = String.valueOf(snapshot.child("busType").getValue());
                            //Toast.makeText(BusDetailsActivity.this, temp, Toast.LENGTH_SHORT).show();
                            if ("Up".equals(temp)) {
                                busList1.add(bus);
                            }else {
                                busList3.add(bus);
                            }
                        } else {
                            Toast.makeText(BusDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar1.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                    progressBar1.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                    progressBar1.setVisibility(View.INVISIBLE);
                    progressBar2.setVisibility(View.INVISIBLE);
                } else {
                    // No data found for the given bus name
                    Toast.makeText(BusDetailsActivity.this, "No data found for this bus name", Toast.LENGTH_SHORT).show();
                }
                busAdapter3 = new BusAdapter2(busList3);
                recyclerView3.setAdapter(busAdapter3);
                busAdapter = new BusAdapter2(busList1);
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