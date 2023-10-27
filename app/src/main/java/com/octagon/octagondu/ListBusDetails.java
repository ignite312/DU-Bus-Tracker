package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
    private RecyclerView recyclerViewUp, recyclerViewDown;
    private AdapterBusDetails busAdapterUp, busAdapterDown;
    private ProgressBar progressBarUp, progressBarDown;
    MaterialToolbar detailsBusToolbar;
    TextView textView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bus_details);
        String busName = getIntent().getStringExtra("busName");
        String flag = getIntent().getStringExtra("flag");

        /*Toolbar*/
        detailsBusToolbar = findViewById(R.id.toolbar);
        if(flag.equals("1"))detailsBusToolbar.setTitle("Locations for "+busName);
        else detailsBusToolbar.setTitle("Schedule for "+busName);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            Drawable blackArrow = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_24);
            actionBar.setHomeAsUpIndicator(blackArrow);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        progressBarUp = findViewById(R.id.progress_bar1);
        progressBarDown = findViewById(R.id.progress_bar2);
        textView = findViewById(R.id.downSc);

        if(flag.equals("1")) {
            textView.setVisibility(View.GONE);
            progressBarDown.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            progressBarDown.setVisibility(View.INVISIBLE);
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Bus Schedule").child(busName);

        recyclerViewUp = findViewById(R.id.recycler_view);
        recyclerViewUp.setLayoutManager(new LinearLayoutManager(this));
        busAdapterUp = new AdapterBusDetails(new ArrayList<>());
        recyclerViewUp.setAdapter(busAdapterUp);

        if(flag.equals("0")) {
            recyclerViewDown = findViewById(R.id.recycler_view3);
            recyclerViewDown.setLayoutManager(new LinearLayoutManager(this));
            busAdapterDown = new AdapterBusDetails(new ArrayList<>());
            recyclerViewDown.setAdapter(busAdapterDown);
        }

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
                            if ("Up".equals(temp)) {
                                busList1.add(bus);
                            } else {
                                busList3.add(bus);
                            }
                        } else {
                            Toast.makeText(ListBusDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBarUp.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                    progressBarDown.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                    progressBarUp.setVisibility(View.INVISIBLE);
                    progressBarDown.setVisibility(View.INVISIBLE);
                    /*Recycler View Up*/
                    busAdapterUp = new AdapterBusDetails(busList1);
                    recyclerViewUp.setAdapter(busAdapterUp);
                    if(flag.equals("1")) busAdapterUp.setFlag("1");
                    else busAdapterUp.setFlag("0");

                    if(flag.equals("0")) {
                        /*Recycler View Down Up*/
                        busAdapterDown = new AdapterBusDetails(busList3);
                        if(flag.equals("1")) busAdapterDown.setFlag("1");
                        else busAdapterDown.setFlag("0");
                        recyclerViewDown.setAdapter(busAdapterDown);
                    }
                } else {
                    Toast.makeText(ListBusDetails.this, "No data found for this bus name", Toast.LENGTH_SHORT).show();
                    finish();
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