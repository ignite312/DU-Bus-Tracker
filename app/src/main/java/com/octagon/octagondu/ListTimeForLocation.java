package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ListTimeForLocation extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerViewUp;
    private AdapterBusDetails busAdapterUp;
    private ProgressBar progressBarUp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_time_for_location);
        String busName = getIntent().getStringExtra("BUSNAME");

        /*Toolbar*/
        MaterialToolbar detailsBusToolbar = findViewById(R.id.toolbar);
        detailsBusToolbar.setTitle("Locations for " + busName);
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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Bus Schedule").child(busName);

        recyclerViewUp = findViewById(R.id.recycler_view);
        recyclerViewUp.setLayoutManager(new LinearLayoutManager(this));
        busAdapterUp = new AdapterBusDetails(new ArrayList<>());
        recyclerViewUp.setAdapter(busAdapterUp);
        Comparator<InfoBusDetails> timeComparator = new Comparator<InfoBusDetails>() {
            @Override
            public int compare(InfoBusDetails bus1, InfoBusDetails bus2) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                try {
                    Date time1 = sdf.parse(bus1.getTime());
                    Date time2 = sdf.parse(bus2.getTime());
                    return time1.compareTo(time2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        };
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<InfoBusDetails> busListUp = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        InfoBusDetails bus = snapshot.getValue(InfoBusDetails.class);
                        if (bus != null) {
                            String temp = String.valueOf(snapshot.child("busType").getValue());
                            if ("Up".equals(temp)) {
                                busListUp.add(bus);
                            }
                        }
                    }
                    progressBarUp.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                    progressBarUp.setVisibility(View.INVISIBLE);
                    if (busListUp.isEmpty()) {
                        showCustomToast("No data found for this bus name");
                        finish();
                    } else {
                        /*Recycler View Up*/
                        Collections.sort(busListUp, timeComparator);
                        busAdapterUp = new AdapterBusDetails(busListUp);
                        recyclerViewUp.setAdapter(busAdapterUp);
                        busAdapterUp.setFlag("LC");
                    }
                } else {
                    showCustomToast("No data found for this bus name");
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
    }
    private void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.custom_toast_text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 160); // Adjust margins as needed
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}