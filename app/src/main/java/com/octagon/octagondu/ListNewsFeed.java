package com.octagon.octagondu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListNewsFeed extends Fragment {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private AdapterNewsFeed adapter;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_feeds, container, false);

        // Initialize the TextView
//        textView = view.findViewById(R.id.createPost);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Use getActivity() to get the activity's context
//                Intent intent = new Intent(getActivity(), CreatePost.class);
//                startActivity(intent);
//            }
//        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Feed").child("Posts");

        recyclerView = view.findViewById(R.id.recycler_view_feed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));  // Use getActivity() to get the activity's context

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
                            showToast("Something went wrong");
                        }
                    }
                    adapter = new AdapterNewsFeed(getContext(), PostList); // Pass the context here
                    recyclerView.setAdapter(adapter);
                    // Toast.makeText(getContext(), "data found for this bus name", Toast.LENGTH_SHORT).show();
                } else {
                    // No data found for the given bus name
                    showToast("No data found for this bus name");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });

        return view;
    }

    // Create a method to show Toast messages
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
