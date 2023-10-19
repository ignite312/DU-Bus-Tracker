package com.octagon.octagondu;

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

public class FragmentNewsFeed extends Fragment {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private AdapterNewsFeed adapter;
    private FragmentPostCreate fragmentPostCreate;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_news_feed, container, false);

        fragmentPostCreate = new FragmentPostCreate();
        TextView textView = view.findViewById(R.id.createAPost);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreatePosFragment();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Feed/Posts");

        recyclerView = view.findViewById(R.id.recycler_view_feed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));  // Use getActivity() to get the activity's context

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    showToast("mara");
                    adapter = new AdapterNewsFeed(getContext(), PostList); // Pass the context here
                    recyclerView.setAdapter(adapter);
                } else {
//                    showToast("No data found for this bus name");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });

        return view;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void openCreatePosFragment() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, fragmentPostCreate)
                    .addToBackStack(null) // Optional: Add transaction to the back stack
                    .commit();
        }
    }
}
