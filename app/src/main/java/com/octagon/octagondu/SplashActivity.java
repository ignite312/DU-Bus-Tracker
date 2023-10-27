package com.octagon.octagondu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, SignInUser.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getReg();
                }
            }, 0);
        }
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void getReg() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserInfo");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            InfoUser info = snapshot.getValue(InfoUser.class);
                            if (info != null) {
                                String t_email = String.valueOf(snapshot.child("email").getValue());
                                if (t_email.equals(email)) {
                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                    intent.putExtra("DUREGNUM", String.valueOf(snapshot.child("regNum").getValue()));
                                    showToast("Hey " + snapshot.child("nickName").getValue());
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
