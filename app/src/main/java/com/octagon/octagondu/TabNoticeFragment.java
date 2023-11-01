package com.octagon.octagondu;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class TabNoticeFragment extends Fragment {
    private RecyclerView recyclerView;
    private AdapterNewsFeed adapter;
    private CreatePost createPost;
    private SwipeRefreshLayout swipeRefreshLayout;
    TextView noNoticeTextView;
    String busName = "Khonika", flag = "SC";
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_notice_fragment, container, false);
        /*Got the Bus Name and Flag*/
        Bundle args = getArguments();
        if (args != null) {
            busName = args.getString("busName");
            flag = args.getString("flag");
        }

        noNoticeTextView = view.findViewById(R.id.noNoticeTextView);
        refresh();
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        recyclerView = view.findViewById(R.id.recycler_view_notice);
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
    private void refresh() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notice/" + busName + "/Posts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<InfoNewsFeed> postList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    noNoticeTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        InfoNewsFeed posts = snapshot.getValue(InfoNewsFeed.class);
                        if (posts != null) {
                            postList.add(posts);
                        } else {
                            showCustomToast("Something went wrong");
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
                    adapter.setFlag(flag);
                    recyclerView.setAdapter(adapter);
                } else {
                    showCustomToast("No Notice Currently");
                    recyclerView.setVisibility(View.GONE);
                    noNoticeTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
    }
}
