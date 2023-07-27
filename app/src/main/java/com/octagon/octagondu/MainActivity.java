package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    DatabaseReference reference;
    TextView textView;
    Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        textView = findViewById(R.id.uni);
//        reference = FirebaseDatabase.getInstance().getReference("Bus Name");
//        reference.child("Khonika").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if(task.isSuccessful()) {
//                    if(task.getResult().exists()) {
//                        DataSnapshot snapshot = task.getResult();
//                        String uni = String.valueOf(snapshot.child("type").getValue());
//                        String year = String.valueOf(snapshot.child("number").getValue());
//                        textView.setText("University " + uni);
//                        textView = findViewById(R.id.year);
//                        textView.setText("Session : " + year);
//                    }else {
//                        Toast.makeText(MainActivity.this, "Bus Name not found", Toast.LENGTH_SHORT).show();
//                    }
//                    Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(MainActivity.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        button = findViewById(R.id.DataEntry);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
        button = findViewById(R.id.schedule);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BusList.class);
                startActivity(intent);
            }
        });
    }
}