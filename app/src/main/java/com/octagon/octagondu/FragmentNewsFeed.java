package com.octagon.octagondu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
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

public class FragmentNewsFeed extends Fragment {
    private RecyclerView recyclerView;
    private AdapterNewsFeed adapter;
    private CreatePost createPost;
    private SwipeRefreshLayout swipeRefreshLayout;
    TextView noPostsTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        createPost = new CreatePost();
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null ) {
            actionBar.setTitle("News");
        }
        noPostsTextView = view.findViewById(R.id.noPostsTextView);
        TextView textView = view.findViewById(R.id.createAPost);
        refresh();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                if(mAuth.getCurrentUser() == null) {
                    showCustomToast("Create A Account First!");
                    Intent intent = new Intent(getActivity(), SignInUser.class);
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent(getActivity(), CreatePost.class);
                startActivity(intent);
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view_feed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
                            if (String.valueOf(snapshot.child("status").getValue()).equals("1")) {
                                postList.add(posts);
                            }
                        } else {
                            showCustomToast("Something went wrong");
                        }
                    }
                    if(postList.isEmpty()) {
                        showCustomToast("No posts found");
                        recyclerView.setVisibility(View.GONE);
                        noPostsTextView.setVisibility(View.VISIBLE);
                        return;
                    }
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
                    adapter = new AdapterNewsFeed(getContext(), postList);
                    adapter.setFlag("FEED");
                    recyclerView.setAdapter(adapter);
                } else {
                    showCustomToast("No posts found");
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
    private void showCustomToast(String message) {
        View layout = getLayoutInflater().inflate(R.layout.custom_toast, (ViewGroup) getView().findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.custom_toast_text);
        text.setText(message);

        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 160); // Adjust margins as needed
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}
