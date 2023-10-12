package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin extends AppCompatActivity {
    private DatabaseReference databaseReference;
    Spinner spinnerBusName;
    TextInputEditText username, password;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        spinnerBusName = findViewById(R.id.spinnerBusName);
        username = findViewById(R.id.username1);
        password = findViewById(R.id.password1);
        button = findViewById(R.id.btn_login1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String busName = spinnerBusName.getSelectedItem().toString();
                String id = username.getText().toString();
                String pass = password.getText().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                databaseReference = database.getReference("Admin").child("Bus Name").child(busName);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Boolean ok = true;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                AdminInformation bus = snapshot.getValue(AdminInformation.class);
                                if (bus != null) {
                                    String id_now = String.valueOf(snapshot.child("user").getValue());
                                    String pass_now = String.valueOf(snapshot.child("pass").getValue());
                                    if (id.equals(id_now) && pass.equals(pass_now)) {
                                        ok = false;
                                        Intent intent = new Intent(getApplicationContext(), Input.class);
                                        intent.putExtra("BUS_NAME_EXTRA", busName);
                                        startActivity(intent);
                                    }
                                } else {
                                    showToast("Something went wrong");
                                }
                            }
                            if(ok) {
                                showToast("Incorrect User Id and Password");
                            }
                        } else {
                            Toast.makeText(Admin.this, "No data found for this bus name", Toast.LENGTH_SHORT).show();
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
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}