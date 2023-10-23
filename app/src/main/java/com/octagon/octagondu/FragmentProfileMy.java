package com.octagon.octagondu;

import static com.octagon.octagondu.MainActivity.DUREGNUM;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentProfileMy extends Fragment {
    private RecyclerView recyclerView;
    private AdapterNewsFeed adapter;
    private FragmentPostCreate fragmentPostCreate;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_profile_my, container, false);
        recyclerView = view.findViewById(R.id.profileOthersRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refresh();
        return view;
    }
    private void refresh() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Feed/Posts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<InfoNewsFeed> PostList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        InfoNewsFeed posts = snapshot.getValue(InfoNewsFeed.class);
                        if (posts != null) {
                            if(String.valueOf(snapshot.child("userId").getValue()).equals(DUREGNUM)) {
                                PostList.add(posts);
                            }
                        } else {
                            showToast("Something went wrong");
                        }
                    }
                    adapter = new AdapterNewsFeed(getContext(), PostList);
                    recyclerView.setAdapter(adapter);
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
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}