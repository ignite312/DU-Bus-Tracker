package com.octagon.octagondu;

import static com.octagon.octagondu.MainActivity.DUREGNUM;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FragmentProfileMy extends Fragment {
    private RecyclerView recyclerView;
    private AdapterNewsFeed adapter;
    private String UID;
    private ImageView image;
    private TextView name, dept, session, rules, email, about, nickName, posts, contribution;
    TextView noPostsTextView;
    Button editProfile;
    private SwipeRefreshLayout swipeRefreshLayout;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_my, container, false);
        name = view.findViewById(R.id.othersFullName);
        dept = view.findViewById(R.id.othersDept);
        session = view.findViewById(R.id.othersSession);
        rules = view.findViewById(R.id.othersRules);
        email = view.findViewById(R.id.othersEmail);
        about = view.findViewById(R.id.othersAboutMe);
        image = view.findViewById(R.id.otherImage);
        nickName = view.findViewById(R.id.othersNickName);
        posts = view.findViewById(R.id.totalPost);
        contribution = view.findViewById(R.id.totalContribution);
        noPostsTextView = view.findViewById(R.id.noPostsTextView);


        editProfile = view.findViewById(R.id.editProfile);
        editProfile.setOnClickListener(view1 -> {
            showCustomToast("Disabled Temporarily");
        });

        /*swipper*/
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
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

        UID = DUREGNUM;
        recyclerView = view.findViewById(R.id.profileOthersRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DatabaseReference ViewUserInfoRef = FirebaseDatabase.getInstance().getReference("UserInfo").child(UID);
        ViewUserInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    name.setText(String.valueOf(dataSnapshot.child("fullName").getValue()));
                    dept.setText((CharSequence) dataSnapshot.child("department").getValue());
                    session.setText((CharSequence) dataSnapshot.child("session").getValue());
                    rules.setText((CharSequence) dataSnapshot.child("userType").getValue());
                    email.setText((CharSequence) dataSnapshot.child("email").getValue());
                    about.setText((CharSequence) dataSnapshot.child("about").getValue());
                    nickName.setText((CharSequence) dataSnapshot.child("nickName").getValue());
                    posts.setText((String.valueOf(dataSnapshot.child("postCount").getValue())));
                    contribution.setText((String.valueOf(dataSnapshot.child("contributionCount").getValue())));
                    if (Integer.parseInt(String.valueOf(dataSnapshot.child("contributionCount").getValue())) <= 0)
                        contribution.setText((String.valueOf(dataSnapshot.child("contributionCount").getValue())));
                    else
                        contribution.setText("+" + dataSnapshot.child("contributionCount").getValue());
                    try {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        StorageReference imagesRef = storageRef.child((String) Objects.requireNonNull(dataSnapshot.child("userImage").getValue()));

                        File localFile = new File(requireContext().getCacheDir(), DUREGNUM + ".png");
                        if (localFile.exists()) {
//                            showToast("Image Loaded from File");
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            image.setImageBitmap(bitmap);
                        } else {
                            imagesRef.getFile(localFile)
                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                            image.setImageBitmap(bitmap);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showToast("Error: " + e.getMessage());
                                        }
                                    });
                        }
                    } catch (Exception e) {
                        showToast(e.getMessage());
                    }
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
        ViewUserInfoRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    posts.setText((String.valueOf(dataSnapshot.child("postCount").getValue())));
                    contribution.setText((String.valueOf(dataSnapshot.child("contributionCount").getValue())));
                    if (Integer.parseInt(String.valueOf(dataSnapshot.child("contributionCount").getValue())) <= 0) {
                        contribution.setText((String.valueOf(dataSnapshot.child("contributionCount").getValue())));
                        if(Integer.parseInt(String.valueOf(dataSnapshot.child("contributionCount").getValue())) < 0) {
                            contribution.setTextColor(Color.parseColor("#FF0000"));
                        }
                    }
                    else
                        contribution.setText("+" + dataSnapshot.child("contributionCount").getValue());
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });

        refresh();
        return view;
    }

    private void refresh() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Feed/Posts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<InfoNewsFeed> postList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    Boolean ok = false;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        InfoNewsFeed posts = snapshot.getValue(InfoNewsFeed.class);
                        if (posts != null) {
                            if (String.valueOf(snapshot.child("userId").getValue()).equals(DUREGNUM)) {
                                ok = true;
                                postList.add(posts);
                            }
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
                    adapter.setFlag("PM");
                    recyclerView.setAdapter(adapter);
                    if (ok) {
                        noPostsTextView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        noPostsTextView.setVisibility(View.VISIBLE);
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

    private void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_toast, (ViewGroup) getView().findViewById(R.id.toast_layout_root));

        TextView text = view.findViewById(R.id.custom_toast_text);
        text.setText(message);

        Toast toast = new Toast(requireContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 160); // Adjust margins as needed
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }


    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}