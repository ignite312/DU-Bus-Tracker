package com.octagon.octagondu;

import static com.octagon.octagondu.MainActivity.DUREGNUM;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostCreate extends AppCompatActivity {
    Spinner spinnerHelp, spinnerBusType;
    TextView textViewTitle, textViewDesc;
    Button button;
    FragmentNewsFeed fragmentNewsFeed;
    private ActionBar actionBar;
    private boolean backArrowVisible = true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_create);
        spinnerHelp = findViewById(R.id.postType);
        spinnerBusType = findViewById(R.id.busName);
        textViewTitle = findViewById(R.id.postTitle);
        textViewDesc = findViewById(R.id.body);
        button = findViewById(R.id.go);
        /*Toolbar Setup*/
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
                String title = textViewTitle.getText().toString();
                String desc = textViewDesc.getText().toString();
                String time = getCurrentTime24HourFormat();
                String date = getCurrentDateFormatted();
                if (!title.isEmpty() || !desc.isEmpty()) {
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
                                // Handle the error
                            } else {
                                Integer updatedValue = dataSnapshot.getValue(Integer.class);
                                InfoNewsFeed post = new InfoNewsFeed(DUREGNUM, busType, helpType, title, desc, 0, time, date, "0", String.valueOf(updatedValue));
                                FirebaseDatabase.getInstance().getReference("Feed/Posts").child(String.valueOf(updatedValue)).setValue(post);
                                update(2, DUREGNUM);
                                showToast("Posted");
                                openFeedFragment();
                            }
                        }
                    });

                } else showToast("Something is empty");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(PostCreate.this, message, Toast.LENGTH_SHORT).show();
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

    private void openFeedFragment() {
        if (PostCreate.this != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, fragmentNewsFeed)
                    .addToBackStack(null)
                    .commit();
        }
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
                    // Handle the error
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
                    // Handle the error
                } else {
                    Integer updatedValue = dataSnapshot.getValue(Integer.class);
                }
            }
        });
    }
}
