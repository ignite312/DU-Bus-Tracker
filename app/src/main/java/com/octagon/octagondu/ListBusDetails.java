package com.octagon.octagondu;

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

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListBusDetails extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView, recyclerView3;
    private AdapterBusDetails busAdapter, busAdapter3;
    private ProgressBar progressBar1, progressBar2;
    MaterialToolbar detailsBusToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bus_details);
        detailsBusToolbar = findViewById(R.id.DetailsBus);
        progressBar1 = findViewById(R.id.progress_bar1);
        progressBar2 = findViewById(R.id.progress_bar2);
        String busName = getIntent().getStringExtra("busName");
        detailsBusToolbar.setTitle("Location for " + busName);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Bus Name").child(busName);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        busAdapter = new AdapterBusDetails(new ArrayList<>());
        recyclerView.setAdapter(busAdapter);

        recyclerView3 = findViewById(R.id.recycler_view3);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        busAdapter3 = new AdapterBusDetails(new ArrayList<>());
        recyclerView3.setAdapter(busAdapter3);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<InfoBusDetails> busList1 = new ArrayList<>();
                List<InfoBusDetails> busList3 = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        InfoBusDetails bus = snapshot.getValue(InfoBusDetails.class);
                        if (bus != null) {
                            String temp = String.valueOf(snapshot.child("busType").getValue());
                            //Toast.makeText(BusDetailsActivity.this, temp, Toast.LENGTH_SHORT).show();
                            if ("Up".equals(temp)) {
                                busList1.add(bus);
                            } else {
                                busList3.add(bus);
                            }
                        } else {
                            Toast.makeText(ListBusDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar1.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                    progressBar2.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                    progressBar1.setVisibility(View.INVISIBLE);
                    progressBar2.setVisibility(View.INVISIBLE);
                    busAdapter3 = new AdapterBusDetails(busList3);
                    recyclerView3.setAdapter(busAdapter3);
                    busAdapter = new AdapterBusDetails(busList1);
                    recyclerView.setAdapter(busAdapter);
                } else {
                    Toast.makeText(ListBusDetails.this, "No data found for this bus name", Toast.LENGTH_SHORT).show();
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