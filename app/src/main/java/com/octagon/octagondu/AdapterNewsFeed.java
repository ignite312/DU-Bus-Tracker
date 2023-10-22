package com.octagon.octagondu;

import static com.octagon.octagondu.MainActivity.DUREGNUM;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.graphics.Bitmap;


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterNewsFeed extends RecyclerView.Adapter<AdapterNewsFeed.PostViewHolder> {
    private List<InfoNewsFeed> postList;
    private Context context;
    Map<Integer, Boolean> booleanMap = new HashMap<>();
    FirebaseStorage storage;

    public AdapterNewsFeed(Context context, List<InfoNewsFeed> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_feed, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        InfoNewsFeed infoNewsFeed = postList.get(position);
        holder.bind(infoNewsFeed, position);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView userNameTextView;
        private TextView departmentNameSessionTextView;
        private TextView userTypeTextView;
        private TextView busNameTextView;
        private TextView postDateTextView;
        private TextView postTitleTextView;
        private TextView postDescTextView;
        private TextView voteCountTextView;
        private ImageView upvoteImageView;
        private ImageView downVoteImageView;
        CircleImageView circleImageView;

        public PostViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.username);
            departmentNameSessionTextView = itemView.findViewById(R.id.deptName);
            busNameTextView = itemView.findViewById(R.id.busNameUserType);
            postDateTextView = itemView.findViewById(R.id.postDate);
            postTitleTextView = itemView.findViewById(R.id.Title);
            postDescTextView = itemView.findViewById(R.id.desc);
            voteCountTextView = itemView.findViewById(R.id.voteCount);
            upvoteImageView = itemView.findViewById(R.id.upvoteImageView);
            downVoteImageView = itemView.findViewById(R.id.downVoteImageView);
            userTypeTextView = itemView.findViewById(R.id.userType);
            circleImageView = itemView.findViewById(R.id.photo);
        }

        @SuppressLint("SetTextI18n")
        public void bind(InfoNewsFeed infoNewsFeed, int position) {
            DatabaseReference ViewUserInfoRef = FirebaseDatabase.getInstance().getReference("UserInfo").child(infoNewsFeed.getUserId());
            ViewUserInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        userNameTextView.setText(String.valueOf(dataSnapshot.child("fullName").getValue()));
                        departmentNameSessionTextView.setText(dataSnapshot.child("department").getValue() + " " +
                                dataSnapshot.child("session").getValue());
                        userTypeTextView.setText("● " + dataSnapshot.child("userType").getValue());
                        busNameTextView.setText(infoNewsFeed.getBusName());
                        try {
                            storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference();
                            StorageReference imagesRef = storageRef.child((String) Objects.requireNonNull(dataSnapshot.child("userImage").getValue()));

                            File localFile = File.createTempFile("tempFile", ".png");
                            imagesRef.getFile(localFile)
                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                            circleImageView.setImageBitmap(bitmap);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showToast("Error: " + e.getMessage());
                                        }
                                    });
//
                        } catch (Exception e) {
                            showToast(e.getMessage());
                        }
                    } else {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error fetching data", databaseError.toException());
                }
            });
            /*Time Difference now and when posted*/
            String timeDifference = getTimeDifference(infoNewsFeed.getTime() + " " + infoNewsFeed.getDate(), "HH:mm:ss dd MMM yyyy");
            postDateTextView.setText(timeDifference);
            postDateTextView.setOnClickListener(View -> {
                if (booleanMap.get(position) == null) {
                    postDateTextView.setText(infoNewsFeed.getTime() + " " + infoNewsFeed.getDate());
                    booleanMap.put(position, false);
                } else {
                    if (Boolean.TRUE.equals(booleanMap.get(position))) {
                        postDateTextView.setText(infoNewsFeed.getTime() + " " + infoNewsFeed.getDate());
                        booleanMap.put(position, false);
                    } else {
                        postDateTextView.setText(getTimeDifference(infoNewsFeed.getTime() + " " + infoNewsFeed.getDate(), "HH:mm:ss dd MMM yyyy"));
                        booleanMap.put(position, true);
                    }
                }
            });
            postTitleTextView.setText(infoNewsFeed.getTitle());
            postDescTextView.setText(infoNewsFeed.getDesc());

            DatabaseReference UpDownSymbolRef = FirebaseDatabase.getInstance().getReference("Feed/PostReactions/" + infoNewsFeed.getPostId() + "/" + DUREGNUM);
            UpDownSymbolRef.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String react = String.valueOf(dataSnapshot.getValue());
                        if (react.equals("10")) {
                            upvoteImageView.setImageResource(R.drawable.arrow_upward_green);
                            downVoteImageView.setImageResource(R.drawable.arrow_downward);
                        } else if (react.equals("01")) {
                            upvoteImageView.setImageResource(R.drawable.arrow_upward);
                            downVoteImageView.setImageResource(R.drawable.arrow_downward_red);
                        } else {
                            upvoteImageView.setImageResource(R.drawable.arrow_upward);
                            downVoteImageView.setImageResource(R.drawable.arrow_downward);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showToast("Firebase Error fetching data");
                }
            });
            DatabaseReference VoteCountRef = FirebaseDatabase.getInstance().getReference("Feed/Posts").child(infoNewsFeed.getPostId()).child("/totalVote");
            VoteCountRef.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String t_totalVote = String.valueOf(dataSnapshot.getValue());
                        if (Integer.parseInt(t_totalVote) <= 0) voteCountTextView.setText(t_totalVote);
                        else voteCountTextView.setText("+" + t_totalVote);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showToast("Firebase Error fetching data");
                }
            });
            upvoteImageView.setOnClickListener(view -> {
                final int[] totalVoteCount = {0};
                VoteCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String t_totalVote = String.valueOf(dataSnapshot.getValue());
                            totalVoteCount[0] = Integer.parseInt(t_totalVote);
                            UpDownSymbolRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    DatabaseReference cnt_ref_main = FirebaseDatabase.getInstance().getReference("Feed/" + "/Posts/" + infoNewsFeed.getPostId() + "/vote");
                                    if (dataSnapshot.exists()) {
                                        String _react = String.valueOf(dataSnapshot.getValue());
                                        if (_react.equals("00")) {
                                            UpDownSymbolRef.setValue("10");
                                            VoteCountRef.setValue(totalVoteCount[0] + 1);
                                        } else if (_react.equals("10")) {
                                            UpDownSymbolRef.setValue("00");
                                            VoteCountRef.setValue(totalVoteCount[0] - 1);
                                        } else if (_react.equals("01")) {
                                            UpDownSymbolRef.setValue("10");
                                            VoteCountRef.setValue(totalVoteCount[0] + 2);
                                        }
                                    } else {
                                        UpDownSymbolRef.setValue("10");
                                        VoteCountRef.setValue(totalVoteCount[0] + 1);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("Firebase", "Error fetching data", databaseError.toException());
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Error fetching data", databaseError.toException());
                    }
                });
            });

            downVoteImageView.setOnClickListener(view -> {
                final int[] totalVoteCount = {0};
                VoteCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String t_totalVote = String.valueOf(dataSnapshot.getValue());
                            totalVoteCount[0] = Integer.parseInt(t_totalVote);
                            UpDownSymbolRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    DatabaseReference cnt_ref_main = FirebaseDatabase.getInstance().getReference("Feed/" + "/Posts/" + infoNewsFeed.getPostId() + "/vote");
                                    if (dataSnapshot.exists()) {
                                        String _react = String.valueOf(dataSnapshot.getValue());
                                        if (_react.equals("00")) {
                                            UpDownSymbolRef.setValue("01");
                                            VoteCountRef.setValue(totalVoteCount[0] - 1);
                                        } else if (_react.equals("10")) {
                                            UpDownSymbolRef.setValue("01");
                                            VoteCountRef.setValue(totalVoteCount[0] - 2);
                                        } else if (_react.equals("01")) {
                                            UpDownSymbolRef.setValue("00");
                                            VoteCountRef.setValue(totalVoteCount[0] + 1);
                                        }
                                    } else {
                                        UpDownSymbolRef.setValue("01");
                                        VoteCountRef.setValue(totalVoteCount[0] - 1);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("Firebase", "Error fetching data", databaseError.toException());
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Error fetching data", databaseError.toException());
                    }
                });
            });

        }
    }

    public String getTimeDifference(String inputDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

        try {
            Date date = sdf.parse(inputDate);
            long currentTimeMillis = System.currentTimeMillis();
            long inputTimeMillis = date.getTime();

            long diffMillis = currentTimeMillis - inputTimeMillis;

            long seconds = TimeUnit.MILLISECONDS.toSeconds(diffMillis);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis);
            long hours = TimeUnit.MILLISECONDS.toHours(diffMillis);
            long days = TimeUnit.MILLISECONDS.toDays(diffMillis);
            long weeks = days / 7;
            long months = days / 30;

            if (months > 0) {
                return months + " months ago";
            } else if (weeks > 0) {
                return weeks + " weeks ago";
            } else if (days > 0) {
                return days + " days ago";
            } else if (hours > 0) {
                return hours + " hours ago";
            } else if (minutes > 0) {
                return minutes + " minutes ago";
            } else if (seconds > 0) {
                return seconds + " seconds ago";
            } else {
                return "just now";
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid date format";
        }
    }

    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
