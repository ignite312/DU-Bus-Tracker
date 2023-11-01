package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class BusDashboard extends AppCompatActivity {
    MaterialToolbar detailsBusToolbar;
    String busName;
    CardView cardView1, cardView2, notice;
    TextView noPostsTextView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_dashboard);
        busName = getIntent().getStringExtra("BUSNAME");
        cardView1 = findViewById(R.id.card1);
        cardView2 = findViewById(R.id.card2);
        notice = findViewById(R.id.notice);

        noPostsTextView = findViewById(R.id.noPostsTextView);
        recyclerView = findViewById(R.id.recycler_view_ad);
        recyclerView = findViewById(R.id.recycler_view_ad);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        /*swipper*/
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        /*Toolbar*/
        detailsBusToolbar = findViewById(R.id.toolbar);
        detailsBusToolbar.setTitle(busName + " Dashboard");
        MaterialToolbar detailsBusToolbar = findViewById(R.id.toolbar);
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
        cardView1.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), TabSchedule.class);
            intent.putExtra("busName", busName);
            intent.putExtra("flag", "AD");
            startActivity(intent);
        });
        cardView2.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), DataEntry.class);
            intent.putExtra("busName", busName);
            intent.putExtra("flag", "AD");
            startActivity(intent);
        });
        notice.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CreateNotice.class);
            intent.putExtra("busName", busName);
            intent.putExtra("flag", "AD");
            startActivity(intent);
        });
        refresh();
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
    private void refresh() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Feed/Posts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<InfoNewsFeed> postList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    noPostsTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        InfoNewsFeed posts = snapshot.getValue(InfoNewsFeed.class);
                        if (posts != null) {
                            String temp = String.valueOf(snapshot.child("busName").getValue());
                            if(temp.equals(busName)) {
                                postList.add(posts);
                            }
                        } else {
                            showCustomToast("Something went wrong");
                        }
                    }
                    if(postList.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        noPostsTextView.setVisibility(View.VISIBLE);
                    }else {
                        /*Sort by latest Time*/
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                        Collections.sort(postList, (post1, post2) -> {
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
                        AdapterNewsFeed adapter = new AdapterNewsFeed(getApplicationContext(), postList);
                        adapter.setFlag("AD");
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    noPostsTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
    }
}