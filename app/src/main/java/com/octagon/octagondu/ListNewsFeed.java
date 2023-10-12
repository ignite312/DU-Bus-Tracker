package com.octagon.octagondu;

import android.annotation.SuppressLint;
import android.content.Context;  // Import Context class

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ListNewsFeed extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private AdapterNewsFeed adapter;
    private TextView textView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);
        textView = findViewById(R.id.createPost);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreatePost.class);
                startActivity(intent);
            }
        });

        // Get the context of the activity
        Context context = this;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String id = "1";
        databaseReference = database.getReference("Feed").child("Posts");

        recyclerView = findViewById(R.id.recycler_view_feed);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<InfoNewsFeed> PostList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        InfoNewsFeed posts = snapshot.getValue(InfoNewsFeed.class);
                        if (posts != null) {
                            PostList.add(posts);
                        } else {
                            Toast.makeText(ListNewsFeed.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    adapter = new AdapterNewsFeed(context, PostList);  // Pass the context here
                    recyclerView.setAdapter(adapter);
                    //Toast.makeText(Feeds.this, "data found for this bus name", Toast.LENGTH_SHORT).show();
                } else {
                    // No data found for the given bus name
                    Toast.makeText(ListNewsFeed.this, "No data found for this bus name", Toast.LENGTH_SHORT).show();
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
