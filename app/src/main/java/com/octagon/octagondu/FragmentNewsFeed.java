package com.octagon.octagondu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    private FragmentPostCreate fragmentPostCreate;
    private SwipeRefreshLayout swipeRefreshLayout;
    TextView noPostsTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        fragmentPostCreate = new FragmentPostCreate();
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
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreatePosFragment();
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view_feed);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refresh();
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

    private void openCreatePosFragment() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, fragmentPostCreate)
                    .addToBackStack(null)
                    .commit();
        }
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
                            postList.add(posts);
                        } else {
                            showToast("Something went wrong");
                        }
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
                    showToast("No posts found");
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
