package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginAdmin extends AppCompatActivity {
    private DatabaseReference databaseReference;
    Spinner spinnerBusName;
    TextInputEditText username, password;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);
        spinnerBusName = findViewById(R.id.spinnerBusName);
        username = findViewById(R.id.username1);
        password = findViewById(R.id.password1);
        button = findViewById(R.id.btn_login1);
        /*Toolbar*/
        MaterialToolbar detailsBusToolbar = findViewById(R.id.toolbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            Drawable blackArrow = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_24);
            actionBar.setHomeAsUpIndicator(blackArrow);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                if(mAuth.getCurrentUser() == null) {
                    showCustomToast("Create A Account First!");
                    Intent intent = new Intent(getApplicationContext(), SignInUser.class);
                    startActivity(intent);
                    return;
                }
                String busName = spinnerBusName.getSelectedItem().toString();
                Intent intent = new Intent(getApplicationContext(), DataEntry.class);
                intent.putExtra("BUS_NAME_EXTRA", busName);
                intent.putExtra("FLAG", "1");
                startActivity(intent);
//                String busName = spinnerBusName.getSelectedItem().toString();
//                String id = username.getText().toString();
//                String pass = password.getText().toString();
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                databaseReference = database.getReference("LoginAdmin").child("Bus Name").child(busName);
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            Boolean ok = true;
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                AdminInformation bus = snapshot.getValue(AdminInformation.class);
//                                if (bus != null) {
//                                    String id_now = String.valueOf(snapshot.child("user").getValue());
//                                    String pass_now = String.valueOf(snapshot.child("pass").getValue());
//                                    if (id.equals(id_now) && pass.equals(pass_now)) {
//                                        ok = false;
//                                        Intent intent = new Intent(getApplicationContext(), DataEntry.class);
//                                        intent.putExtra("BUS_NAME_EXTRA", busName);
//                                        intent.putExtra("FLAG", "1");
//                                        startActivity(intent);
//                                    }
//                                } else {
//                                    showToast("Something went wrong");
//                                }
//                            }
//                            if(ok) {
//                                showToast("Incorrect User Id and Password");
//                            }
//                        } else {
//                            Toast.makeText(LoginAdmin.this, "No data found for this bus name", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Log.e("Firebase", "Error fetching data", databaseError.toException());
//                    }
//                });
            }
        });
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView text = (TextView) layout.findViewById(R.id.custom_toast_text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 160); // Adjust margins as needed
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}