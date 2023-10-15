package com.octagon.octagondu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.atomic.AtomicInteger;

public class CreatePost extends AppCompatActivity {
        Button button;
        Spinner spinner;
        TextView textView;

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_create_post);
                button = findViewById(R.id.post);
                spinner = findViewById(R.id.postType);
                textView = findViewById(R.id.body);

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Feed/Count");
                final AtomicInteger cnt = new AtomicInteger(0);
                ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                                // Check if dataSnapshot exists and has a value
                                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                                        // Cast the value to an Integer
                                        Integer post = dataSnapshot.getValue(Integer.class);

                                        // Check if the cast was successful before using the value
                                        if (post != null) {
                                                cnt.set(post); // Update the AtomicInteger's value
                                        } else {
                                                // Handle the case where the value couldn't be cast to Integer
                                        }
                                } else {
                                        // Handle the case where the dataSnapshot is null or doesn't exist
                                }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                        }
                });
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                String helptype = spinner.getSelectedItem().toString();
                                String body = textView.getText().toString();
                                String id = String.valueOf(cnt.get()+1);
                                InfoNewsFeed post = new InfoNewsFeed(R.drawable.bus, "Konika", "Emon", "CSE", "18 Sept.", body, 0, "LoginAdmin");
                                if(!body.isEmpty()) {
                                        databaseReference.child("Feed").child("Count").setValue(cnt.get()+1);
                                        databaseReference.child("Feed").child("Posts").child(id).setValue(post);
                                        Intent intent = new Intent(getApplicationContext(), FragmentNewsFeed.class);
                                        startActivity(intent);
                                }
                        }
                });
        }
}