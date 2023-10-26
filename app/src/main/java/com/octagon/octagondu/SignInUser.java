package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SignInUser extends AppCompatActivity {
    FirebaseAuth mAuth;
    private Button goButton, signUpButton;
    private TextInputEditText editUserName, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            getReg();
        }
        setContentView(R.layout.activity_sign_in_user);
        editUserName = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        goButton = findViewById(R.id.goButton);
        signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInUser.this, SignUpUser.class);
                startActivity(intent);
            }
        });
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editUserName.getText().toString();
                String password = editPassword.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    showToast("Enter Email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast("Enter Password");
                    return;
                }
                authenticate(email, password);
            }
        });
    }

    private void authenticate(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    getReg();
                    showToast("Login Successful");
                } else {
                    showToast("Authentication failed.");
                }
            }
        });
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
                                    Intent intent = new Intent(SignInUser.this, MainActivity.class);
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
