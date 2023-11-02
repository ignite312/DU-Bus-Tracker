package com.octagon.octagondu;

import static com.octagon.octagondu.MainActivity.DUREGNUM;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreatePost extends AppCompatActivity {
    Spinner spinnerHelp, spinnerBusType;
    TextView textViewTitle, textViewDesc;
    Button button;
    String helpType, busName, title, desc, flag, postID;
    int voteCNT;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        spinnerHelp = findViewById(R.id.postType);
        spinnerBusType = findViewById(R.id.busName);
        textViewTitle = findViewById(R.id.postTitle);
        textViewDesc = findViewById(R.id.body);
        button = findViewById(R.id.go);
        helpType = getIntent().getStringExtra("HT");
        busName = getIntent().getStringExtra("BUSNAME");
        title = getIntent().getStringExtra("TT");
        desc = getIntent().getStringExtra("DEC");
        flag = getIntent().getStringExtra("FLAG");
        postID = getIntent().getStringExtra("POSTID");
        if(flag != null) {
            voteCNT = Integer.parseInt(getIntent().getStringExtra("VOTECNT"));
            textViewTitle.setText(title);
            textViewDesc.setText(desc);

            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    android.R.id.text1
            );
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapter1.add(helpType);
            spinnerHelp.setAdapter(adapter1);

            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    android.R.id.text1
            );
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapter2.add(busName);
            spinnerBusType.setAdapter(adapter2);
            button.setText("Update");
        }
        /*Toolbar Setup*/
        MaterialToolbar detailsBusToolbar = findViewById(R.id.toolbar);
        if(flag != null)detailsBusToolbar.setTitle("Update Post");
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
                String helpType = spinnerHelp.getSelectedItem().toString();
                String busType = spinnerBusType.getSelectedItem().toString();
                title = textViewTitle.getText().toString();
                desc = textViewDesc.getText().toString();
                String time = getCurrentTime24HourFormat();
                String date = getCurrentDateFormatted();
                if (!title.isEmpty() || !desc.isEmpty()) {
                    if(flag != null) {
                        InfoNewsFeed post = new InfoNewsFeed(DUREGNUM, busType, helpType, title, desc, voteCNT, time, date, "0", String.valueOf(postID));
                        FirebaseDatabase.getInstance().getReference("Feed/Posts").child(String.valueOf(postID)).setValue(post);
                        showCustomToast("Post Updated");
                        finish();
                        return;
                    }
                    DatabaseReference PostCountRef = FirebaseDatabase.getInstance().getReference("Feed/PostCount");
                    PostCountRef.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            Integer value = mutableData.getValue(Integer.class);
                            if (value == null) {
                                mutableData.setValue(1);
                            } else {
                                mutableData.setValue(value + 1);
                            }
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                            if (databaseError != null) {
                                showCustomToast(databaseError.getMessage());
                            } else {
                                Integer updatedValue = dataSnapshot.getValue(Integer.class);
                                InfoNewsFeed post = new InfoNewsFeed(DUREGNUM, busType, helpType, title, desc, 0, time, date, "0", String.valueOf(updatedValue));
                                FirebaseDatabase.getInstance().getReference("Feed/Posts").child(String.valueOf(updatedValue)).setValue(post);
                                update(2, DUREGNUM);
                                showCustomToast("Posted");
                                finish();
                            }
                        }
                    });

                } else showCustomToast("Something is empty");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(CreatePost.this, message, Toast.LENGTH_SHORT).show();
    }

    public String getCurrentDateFormatted() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    public String getCurrentTime24HourFormat() {
        Date currentTime = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return timeFormat.format(currentTime);
    }
    private void update(int dcc, String UID) {
        DatabaseReference UserPostCount = FirebaseDatabase.getInstance().getReference("UserInfo/" + DUREGNUM + "/postCount");
        UserPostCount.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer value = mutableData.getValue(Integer.class);
                if (value == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue(value + 1);
                }
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    showCustomToast(databaseError.getMessage());
                } else {
                    Integer updatedValue = dataSnapshot.getValue(Integer.class);
                }
            }
        });

        DatabaseReference ContributionCountRef = FirebaseDatabase.getInstance().getReference("UserInfo/" + UID + "/contributionCount");
        ContributionCountRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer value = mutableData.getValue(Integer.class);
                if (value == null) {
                    mutableData.setValue(dcc);
                } else {
                    mutableData.setValue(value + dcc);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    showCustomToast(databaseError.getMessage());
                } else {
                    Integer updatedValue = dataSnapshot.getValue(Integer.class);
                }
            }
        });
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
