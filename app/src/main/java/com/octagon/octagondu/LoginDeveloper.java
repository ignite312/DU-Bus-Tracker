package com.octagon.octagondu;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginDeveloper extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_developer);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DataEntry.class);
                intent.putExtra("BUS_NAME_EXTRA", "FuckYou");
                intent.putExtra("FLAG", "0");
                startActivity(intent);
//                progressBar.setVisibility(view.VISIBLE);
//                String email, password;
//                email = String.valueOf(editTextEmail.getText());
//                password = String.valueOf(editTextPassword.getText());
//                if (TextUtils.isEmpty(email)) {
//                    Toast.makeText(LoginDeveloper.this, "Enter Email", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (TextUtils.isEmpty(password)) {
//                    Toast.makeText(LoginDeveloper.this, "Enter Password", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        progressBar.setVisibility(View.GONE);
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(), DataEntry.class);
//                            startActivity(intent);
//                            finish();
//                        } else {
//                            Toast.makeText(LoginDeveloper.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}