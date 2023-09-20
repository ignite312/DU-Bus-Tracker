package com.octagon.octagondu;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Find the "Update Profile" button
        Button updateProfileButton = findViewById(R.id.buttonUpdate);

        // Set an OnClickListener for the button
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the button is clicked, navigate to the UpdateProfileActivity
                Intent intent = new Intent(UserProfileActivity.this, UpdateProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
