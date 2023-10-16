package com.octagon.octagondu;

import static com.octagon.octagondu.MainActivity.userRegUnique;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FragmentPostCreate extends Fragment {
    Spinner spinnerHelp, spinnerBusType;
    TextView textViewTitle, textViewDesc;
    Button button;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    int Count = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_post_create, container, false);
        spinnerHelp =  view.findViewById(R.id.postType);
        spinnerBusType = view.findViewById(R.id.busName);
        textViewTitle = view.findViewById(R.id.postTitle);
        textViewDesc  = view.findViewById(R.id.body);
        button = view.findViewById(R.id.go);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Feed");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String cnt = String.valueOf(dataSnapshot.child("Count").getValue());
                    Count = Integer.parseInt(String.valueOf(cnt));
                } else {
                    showToast("Error");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
        databaseReference = firebaseDatabase.getReference();
        button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                String helpType = spinnerHelp.getSelectedItem().toString();
                                String busType = spinnerBusType.getSelectedItem().toString();
                                String title = textViewTitle.getText().toString();
                                String desc = textViewDesc.getText().toString();
                                String time = getCurrentTime24HourFormat();
                                String date = getCurrentDateFormatted();

                                InfoNewsFeed post = new InfoNewsFeed(userRegUnique, busType, helpType, title, desc, 0, time, date, 0, Count);
                                if(!title.isEmpty() || !desc.isEmpty()) {
                                    databaseReference.child("Feed").child("Posts").child("Pending").child(String.valueOf(Count)).setValue(post);
                                    databaseReference.child("Feed").child("Count").setValue(Count+1);
                                }
                        }
                });
        return view;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    public String getCurrentDateFormatted() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return dateFormat.format(currentDate);
    }
    public String getCurrentTime24HourFormat() {
        Date currentTime = new Date();

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        return timeFormat.format(currentTime);
    }
}