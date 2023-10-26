package com.octagon.octagondu;

import static com.octagon.octagondu.MainActivity.DUREGNUM;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentProfileMy extends Fragment {
    private RecyclerView recyclerView;
    private AdapterNewsFeed adapter;
    private String UID;
    private ImageView image;
    private TextView name, dept, session, rules, email, about, nickName, posts, contribution;

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
                    if (Integer.parseInt(String.valueOf(dataSnapshot.child("contributionCount").getValue())) <= 0)
                        contribution.setText((String.valueOf(dataSnapshot.child("contributionCount").getValue())));
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
                List<InfoNewsFeed> PostList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        InfoNewsFeed posts = snapshot.getValue(InfoNewsFeed.class);
                        if (posts != null) {
                            if (String.valueOf(snapshot.child("userId").getValue()).equals(DUREGNUM)) {
                                PostList.add(posts);
                            }
                        } else {
                            showToast("Something went wrong");
                        }
                    }
                    adapter = new AdapterNewsFeed(getContext(), PostList);
                    adapter.setFlag("PM");
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