package com.octagon.octagondu;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class ProfileOthers extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterNewsFeed adapter;
    private String UID;
    private ImageView image;
    private TextView name, dept, session, rules, email, about, nickName, posts, contribution;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_others);
        name = findViewById(R.id.othersFullName);
        dept = findViewById(R.id.othersDept);
        session  = findViewById(R.id.othersSession);
        rules = findViewById(R.id.othersRules);
        email = findViewById(R.id.othersEmail);
        about = findViewById(R.id.othersAboutMe);
        image = findViewById(R.id.otherImage);
        nickName = findViewById(R.id.othersNickName);
        posts = findViewById(R.id.totalPost);
        contribution = findViewById(R.id.totalContribution);


        UID = getIntent().getStringExtra("UID");
        recyclerView = findViewById(R.id.profileOthersRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refresh();

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
                    if (Integer.parseInt(String.valueOf(dataSnapshot.child("contributionCount").getValue())) <= 0)                     contribution.setText((String.valueOf(dataSnapshot.child("contributionCount").getValue())));
                    else contribution.setText("+" + dataSnapshot.child("contributionCount").getValue());
                    try {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        StorageReference imagesRef = storageRef.child((String) Objects.requireNonNull(dataSnapshot.child("userImage").getValue()));

                        File localFile = new File(getCacheDir(), UID + ".png");
                        if (localFile.exists()) {
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
                    else contribution.setText("+" + dataSnapshot.child("contributionCount").getValue());
                } else {
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
    }

    private void refresh() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Feed/Posts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<InfoNewsFeed> postList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        InfoNewsFeed post = snapshot.getValue(InfoNewsFeed.class);
                        if (post != null) {
                            if (String.valueOf(snapshot.child("userId").getValue()).equals(UID)) {
                                postList.add(post);
                            }
                        } else {
                            showToast("Something went wrong");
                        }
                    }
                    adapter = new AdapterNewsFeed(ProfileOthers.this, postList);
                    adapter.setFlag("PO");
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
