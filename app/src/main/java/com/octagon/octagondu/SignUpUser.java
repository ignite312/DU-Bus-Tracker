package com.octagon.octagondu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SignUpUser extends AppCompatActivity {
    private TextInputEditText editFullname;
    private TextInputEditText editRegNum;
    private TextInputEditText editDept;
    private TextInputEditText editSession;
    private TextInputEditText editEmailId;
    private TextInputEditText editPhoneNumber;
    private TextInputEditText editPassword;
    private Button signUpButton;
    private TextView alreadyHaveAccount;
    int selectedImageResourceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);

        editFullname = findViewById(R.id.editFullname);
        editRegNum = findViewById(R.id.editRegNum);
        editDept = findViewById(R.id.editDept);
        editSession = findViewById(R.id.editSession);
        editEmailId = findViewById(R.id.editEmailId);
        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editPassword = findViewById(R.id.editPassword);
        signUpButton = findViewById(R.id.signUpButton);
        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);

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
                selectedImageResourceId= (int) imageSpinner.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing when nothing is selected
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(String.valueOf(selectedImageResourceId));
                // Retrieve data from input fields
                String fullName = editFullname.getText().toString();
                String regNum = editRegNum.getText().toString();
                String department = editDept.getText().toString();
                String session = editSession.getText().toString();
                String email = editEmailId.getText().toString();
                String phoneNumber = editPhoneNumber.getText().toString();
                String password = editPassword.getText().toString();
                int contributionCount = 0;
                String userType = "User";
                if (!(fullName.isEmpty() || regNum.isEmpty() || department.isEmpty() || session.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty())) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    InfoUser user = new InfoUser(fullName, regNum, department, session, email, phoneNumber, contributionCount, userType, String.valueOf(selectedImageResourceId));
                    databaseReference.child("UserInfo").child(regNum).setValue(user);
                    Toast.makeText(SignUpUser.this, "Signup Complete", Toast.LENGTH_SHORT).show();
                    clearEditTextFields();
                    Intent intent = new Intent(getApplicationContext(), SignInUser.class);
                    startActivity(intent);
                } else {
                    showToast("Something went wrong");
                }
            }
        });
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SignInUser activity
                Intent intent = new Intent(SignUpUser.this, SignInUser.class);
                startActivity(intent);
            }
        });
    }
    private void clearEditTextFields() {
        editFullname.setText("");
        editRegNum.setText("");
        editDept.setText("");
        editSession.setText("");
        editEmailId.setText("");
        editPhoneNumber.setText("");
        editPassword.setText("");
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
