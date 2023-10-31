package com.octagon.octagondu;

import static com.octagon.octagondu.MainActivity.DUREGNUM;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
