package com.octagon.octagondu;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        recyclerView = findViewById(R.id.locationRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterBusLocation = new AdapterBusLocation(ListBusLocation.this, new ArrayList<>());
        recyclerView.setAdapter(adapterBusLocation);

        databaseReference = database.getReference("Location").child(getCurrentDateFormatted()).child(busName).child(busTime).child("Locations");
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
                    showToast("No data found for this bus name");
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
        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return dateFormat.format(currentDate);
    }
    public String getCurrentTime24HourFormat() {
        Date currentTime = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return timeFormat.format(currentTime);
    }
}