package com.octagon.octagondu;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class DeveloperDetails extends AppCompatActivity {
    private ImageView imageView;
    private TextView name;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_details);
        imageView = findViewById(R.id.dev_emon_pp);
        name = findViewById(R.id.dev_emon_name);
        email = findViewById(R.id.dev_emon_email);
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

        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ProfileOthers.class);
            intent.putExtra("UID", "2020015640");
            startActivity(intent);
        });
        name.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ProfileOthers.class);
            intent.putExtra("UID", "2020015640");
            startActivity(intent);
        });
        email.setOnClickListener(view -> {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact.emonkhan@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            try {
                startActivity(emailIntent);
            } catch (ActivityNotFoundException e) {
            }
        });
    }
}