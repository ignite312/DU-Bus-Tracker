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
import android.view.ViewGroup;
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
        if (flag.equals("LC")) detailsBusToolbar.setTitle("Locations for " + busName);
        else detailsBusToolbar.setTitle("Schedule for " + busName);
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

        if (flag.equals("LC")) {
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

        if (flag.equals("SC") || flag.equals("AD")) {
            recyclerViewDown = findViewById(R.id.recycler_view3);
            recyclerViewDown.setLayoutManager(new LinearLayoutManager(this));
            busAdapterDown = new AdapterBusDetails(new ArrayList<>());
            recyclerViewDown.setAdapter(busAdapterDown);
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<InfoBusDetails> busListUp = new ArrayList<>();
                List<InfoBusDetails> busListDown = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        InfoBusDetails bus = snapshot.getValue(InfoBusDetails.class);
                        if (bus != null) {
                            String temp = String.valueOf(snapshot.child("busType").getValue());
                            if ("Up".equals(temp)) {
                                busListUp.add(bus);
                            } else {
                                busListDown.add(bus);
                            }
                        }
                    }
                    progressBarUp.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                    progressBarDown.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                    progressBarUp.setVisibility(View.INVISIBLE);
                    progressBarDown.setVisibility(View.INVISIBLE);
                    /*Recycler View Up*/
                    busAdapterUp = new AdapterBusDetails(busListUp);
                    recyclerViewUp.setAdapter(busAdapterUp);
                    busAdapterUp.setFlag(flag);

                    if (flag.equals("SC") || flag.equals("AD")) {
                        /*Recycler View Down Up*/
                        busAdapterDown = new AdapterBusDetails(busListDown);
                        recyclerViewDown.setAdapter(busAdapterDown);
                        busAdapterDown.setFlag(flag);
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

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView text = (TextView) layout.findViewById(R.id.custom_toast_text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 160); // Adjust margins as needed
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}