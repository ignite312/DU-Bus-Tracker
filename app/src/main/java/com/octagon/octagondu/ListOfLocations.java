package com.octagon.octagondu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListOfLocations extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterBusLocation adapterBusLocation;
    private final int delay = 10000; // 5 seconds
    private String busName, ID, busTime;
    MaterialToolbar detailsBusToolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    TextView noLocationsTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bus_location);
        busName = getIntent().getStringExtra("BUSNAME");
        ID = getIntent().getStringExtra("ID");
        busTime = getIntent().getStringExtra("BUSTIME");
        /*Toolbar*/
        detailsBusToolbar = findViewById(R.id.toolbar);
        detailsBusToolbar.setTitle("Locations for " + busName + " " + busTime);
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


        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutLocation);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        TextView textView = findViewById(R.id.shareYourLocation);
        noLocationsTextView = findViewById(R.id.noLocationTextView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListOfLocations.this, LocationShare.class);
                intent.putExtra("BUSNAME", busName);
                intent.putExtra("ID", ID);
                intent.putExtra("BUSTIME", busTime);
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.locationRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterBusLocation = new AdapterBusLocation(ListOfLocations.this, new ArrayList<>());
        recyclerView.setAdapter(adapterBusLocation);
        refresh();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private void refresh() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Location").child(getCurrentDateFormatted()).child(busName).child(ID).child("Locations");
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
                    /*Sort by latest Time*/
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                    Collections.sort(list, (post1, post2) -> {
                        try {
                            Date time1 = timeFormat.parse(post1.getTime());
                            Date time2 = timeFormat.parse(post2.getTime());

                            Date date1 = dateFormat.parse(post1.getDate());
                            Date date2 = dateFormat.parse(post2.getDate());
                            int dateComparison = date2.compareTo(date1);

                            if (dateComparison == 0) {
                                int timeComparison = time2.compareTo(time1);
                                return timeComparison;
                            } else {
                                return dateComparison;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    });
                    adapterBusLocation = new AdapterBusLocation(ListOfLocations.this, list);
                    recyclerView.setAdapter(adapterBusLocation);
                    recyclerView.setVisibility(View.VISIBLE);
                    noLocationsTextView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    noLocationsTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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