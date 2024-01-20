package com.octagon.octagondu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class SignUpUser extends AppCompatActivity {
    private TextInputEditText editFullName, editNickName;
    private TextInputEditText editRegNum;
    private TextInputEditText editDept;
    private TextInputEditText editSession;
    private TextInputEditText editEmailId;
    private TextInputEditText editPhoneNumber;
    private TextInputEditText editPassword;
    private Button signUpButton;
    private TextView alreadyHaveAccount;
    private String selectedImageResourceId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);

        editFullName = findViewById(R.id.editFullname);
        editNickName = findViewById(R.id.editNickName);
        editRegNum = findViewById(R.id.editRegNum);
        editDept = findViewById(R.id.editDept);
        editSession = findViewById(R.id.editSession);
        editEmailId = findViewById(R.id.editEmailId);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editPassword = findViewById(R.id.editPassword);
        signUpButton = findViewById(R.id.signUpButton);
        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        Spinner imageSpinner = findViewById(R.id.spinnerProfilePic);

        List<Integer> imageResourceIds = new ArrayList<>();
        imageResourceIds.add(R.drawable.b1);
        imageResourceIds.add(R.drawable.b2);
        imageResourceIds.add(R.drawable.b3);
        imageResourceIds.add(R.drawable.b4);
        imageResourceIds.add(R.drawable.b5);
        imageResourceIds.add(R.drawable.b6);
        imageResourceIds.add(R.drawable.b7);
        imageResourceIds.add(R.drawable.b8);
        imageResourceIds.add(R.drawable.b9);
        imageResourceIds.add(R.drawable.b10);
        imageResourceIds.add(R.drawable.g1);
        imageResourceIds.add(R.drawable.g2);
        imageResourceIds.add(R.drawable.g3);
        imageResourceIds.add(R.drawable.g4);
        imageResourceIds.add(R.drawable.g5);
        imageResourceIds.add(R.drawable.g6);
        imageResourceIds.add(R.drawable.g7);
        imageResourceIds.add(R.drawable.g8);
        imageResourceIds.add(R.drawable.g9);
        imageResourceIds.add(R.drawable.g10);

        AdapterProfilePic adapter = new AdapterProfilePic(this, android.R.layout.simple_spinner_item, imageResourceIds);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageSpinner.setAdapter(adapter);
        imageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected image resource ID
                if (position < 10) {
                    selectedImageResourceId = "ProfilePic/Boys/" + "b" + String.valueOf(position + 1) + ".png";
                } else {
                    selectedImageResourceId = "ProfilePic/Girls/" + "g" + String.valueOf(position - 9) + ".png";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing when nothing is selected
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = editFullName.getText().toString();
                String regNum = editRegNum.getText().toString();
                String department = editDept.getText().toString();
                String session = editSession.getText().toString();
                String nickName = editNickName.getText().toString();
                String email = editEmailId.getText().toString();
                String phoneNumber = editPhoneNumber.getText().toString();
                String password = editPassword.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if (!(fullName.isEmpty() || regNum.isEmpty() || department.isEmpty() || session.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty())) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpUser.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        InfoUser user = new InfoUser(regNum, phoneNumber, fullName, department, session, "User", email, "", selectedImageResourceId, nickName, 0, 0);
                                        databaseReference.child("UserInfo").child(regNum).setValue(user);
                                        showToast("Signup successful!");
                                        FirebaseAuth.getInstance().signOut();
                                        progressBar.setVisibility(View.GONE);
                                        Intent intent = new Intent(SignUpUser.this, SignInUser.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        showToast("Error!");
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                } else {
                    showToast("Something is Empty");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SignInUser activity
                Intent intent = new Intent(SignUpUser.this, SignInUser.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
