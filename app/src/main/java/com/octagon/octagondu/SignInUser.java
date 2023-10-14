package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SignInUser extends AppCompatActivity {
    private DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    private Button goButton, signUpButton;
    private TextInputEditText editUserName,editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_user);
        editUserName=findViewById(R.id.editUsername);
        editPassword=findViewById(R.id.editPassword);
        goButton = findViewById(R.id.goButton);
        signUpButton = findViewById(R.id.signUpButton);
        mAuth = FirebaseAuth.getInstance();

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
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            showToast("Login Successful");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            showToast("Authentication failed.");
                        }
                    }
                });
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
