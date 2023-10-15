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
    private TextInputEditText editUserName,editPassword;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String userReg = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_user);
        editUserName=findViewById(R.id.editUsername);
        editPassword=findViewById(R.id.editPassword);
        goButton = findViewById(R.id.goButton);
        signUpButton = findViewById(R.id.signUpButton);
        mAuth = FirebaseAuth.getInstance();


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
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
                if (TextUtils.isEmpty(email)) {
                    showToast("Enter Email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast("Enter Password");
                    return;
                }
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("UserInfo");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean ok = false;
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                InfoUser info = snapshot.getValue(InfoUser.class);
                                if (info != null) {
                                    String t_email = String.valueOf(snapshot.child("email").getValue());
                                    if(t_email.equals(email)) {
                                        userReg = String.valueOf(snapshot.child("regNum").getValue());
                                        authenticate(email, password, userReg);
                                        ok = true;
                                        break;
                                    }
                                    if(ok)break;
                                } else {
                                    showToast("Something went wrong");
                                }
                            }
                            if(!ok) {
                                showToast("No data found");
                            }
                        } else {
                            showToast("Error");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Error fetching data", databaseError.toException());
                    }
                });
            }
        });
    }

    private void authenticate(String email, String password, String userReg) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    showToast("Login Successful");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("userId", userReg);
                    startActivity(intent);
                    finish();
                } else {
                    showToast("Authentication failed.");
                }
            }
        });
    }
    private void initView() {

    }

    public void onClick(View view) {

    }
    private void loginValidation() {

    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
