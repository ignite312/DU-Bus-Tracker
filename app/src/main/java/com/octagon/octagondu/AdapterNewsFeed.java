package com.octagon.octagondu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterNewsFeed extends RecyclerView.Adapter<AdapterNewsFeed.PostViewHolder> {
    private List<InfoNewsFeed> postList;
    private Context context;

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
        holder.bind(infoNewsFeed);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView userNameTextView;
        private TextView departmentNameTextView;
        private TextView busNameTextView;
        private TextView postDateTextView;
        private TextView postTitleTextView;
        private TextView postDescTextView;
        private TextView voteCountTextView;
        private ImageView upvoteImageView;
        private ImageView downVoteImageView;

        public PostViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.username);
            departmentNameTextView = itemView.findViewById(R.id.deptName);
            busNameTextView = itemView.findViewById(R.id.busNameUserType);
            postDateTextView = itemView.findViewById(R.id.postDate);
            postTitleTextView = itemView.findViewById(R.id.Title);
            postDescTextView = itemView.findViewById(R.id.desc);
            voteCountTextView = itemView.findViewById(R.id.voteCount);
            upvoteImageView = itemView.findViewById(R.id.upvoteImageView);
            downVoteImageView = itemView.findViewById(R.id.downVoteImageView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(InfoNewsFeed infoNewsFeed) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserInfo").child(infoNewsFeed.getUserId());
            reference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        userNameTextView.setText(String.valueOf(dataSnapshot.child("fullName").getValue()));
                        departmentNameTextView.setText(dataSnapshot.child("department").getValue() + " " +
                                dataSnapshot.child("session").getValue());
                        busNameTextView.setText(dataSnapshot.child("userType").getValue() + " " + infoNewsFeed.getBusName());
                    } else {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error fetching data", databaseError.toException());
                }
            });
            postDateTextView.setText(infoNewsFeed.getDate() + " " + infoNewsFeed.getTime());
            postTitleTextView.setText(infoNewsFeed.getTitle());
            postDescTextView.setText(infoNewsFeed.getDesc());
            voteCountTextView.setText(String.valueOf(infoNewsFeed.getVote()));

            reference = FirebaseDatabase.getInstance().getReference("Interaction/" + infoNewsFeed.getPostId() + "/People/" + infoNewsFeed.getUserId());
            reference.addValueEventListener(new ValueEventListener() {
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
                    } else {

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error fetching data", databaseError.toException());
                }
            });
            upvoteImageView.setOnClickListener(view -> {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Interaction/" + infoNewsFeed.getPostId() + "/People/" + infoNewsFeed.getUserId());
                DatabaseReference cnt_ref = FirebaseDatabase.getInstance().getReference("Interaction/" + infoNewsFeed.getPostId() + "/Count");
                final int[] count = {0};
                cnt_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String t_cnt = String.valueOf(dataSnapshot.getValue());
                            count[0] = Integer.parseInt(t_cnt);
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Interaction/" + infoNewsFeed.getPostId() + "/People/" + infoNewsFeed.getUserId());
                                    DatabaseReference cnt_ref = FirebaseDatabase.getInstance().getReference("Interaction/" + infoNewsFeed.getPostId() + "/Count");
                                    DatabaseReference cnt_ref_main = FirebaseDatabase.getInstance().getReference("Feed/" + "/Posts/" + infoNewsFeed.getPostId() + "/vote");
                                    if (dataSnapshot.exists()) {
                                        String _react = String.valueOf(dataSnapshot.getValue());
                                        if (_react.equals("00")) {
                                            reff.setValue("10");
                                            cnt_ref_main.setValue(count[0] + 1);
                                            cnt_ref.setValue(count[0] + 1);
                                        } else if (_react.equals("10")) {
                                            reff.setValue("00");
                                            cnt_ref.setValue(count[0] - 1);
                                            cnt_ref_main.setValue(count[0] - 1);
                                        } else if (_react.equals("01")) {
                                            reff.setValue("10");
                                            cnt_ref.setValue(count[0] + 2);
                                            cnt_ref_main.setValue(count[0] + 2);
                                        }
                                    } else {
                                        reff.setValue("10");
                                        cnt_ref_main.setValue(count[0] + 1);
                                        cnt_ref.setValue(count[0] + 1);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("Firebase", "Error fetching data", databaseError.toException());
                                }
                            });
                        } else {
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Error fetching data", databaseError.toException());
                    }
                });
            });

            downVoteImageView.setOnClickListener(view -> {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Interaction/" + infoNewsFeed.getPostId() + "/People/" + infoNewsFeed.getUserId());
                DatabaseReference cnt_ref = FirebaseDatabase.getInstance().getReference("Interaction/" + infoNewsFeed.getPostId() + "/Count");
                final int[] count = {0};
                DatabaseReference ref_cnt = FirebaseDatabase.getInstance().getReference("Interaction/" + infoNewsFeed.getPostId() + "/Count");
                ref_cnt.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String t_cnt = String.valueOf(dataSnapshot.getValue());
                            count[0] = Integer.parseInt(t_cnt);
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Interaction/" + infoNewsFeed.getPostId() + "/People/" + infoNewsFeed.getUserId());
                                    DatabaseReference cnt_ref = FirebaseDatabase.getInstance().getReference("Interaction/" + infoNewsFeed.getPostId() + "/Count");
                                    DatabaseReference cnt_ref_main = FirebaseDatabase.getInstance().getReference("Feed/" + "/Posts/" + infoNewsFeed.getPostId() + "/vote");
                                    if (dataSnapshot.exists()) {
                                        String _react = String.valueOf(dataSnapshot.getValue());
                                        if (_react.equals("00")) {
                                            reff.setValue("01");
                                            cnt_ref.setValue(count[0] - 1);
                                            cnt_ref_main.setValue(count[0] - 1);
                                        } else if (_react.equals("10")) {
                                            reff.setValue("01");
                                            cnt_ref.setValue(count[0] - 2);
                                            cnt_ref_main.setValue(count[0] - 2);
                                        } else if (_react.equals("01")) {
                                            reff.setValue("00");
                                            cnt_ref.setValue(count[0] + 1);
                                            cnt_ref_main.setValue(count[0] + 1);
                                        }
                                    } else {
                                        reff.setValue("01");
                                        cnt_ref.setValue(count[0] - 1);
                                        cnt_ref_main.setValue(count[0] - 1);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("Firebase", "Error fetching data", databaseError.toException());
                                }
                            });
                        } else {
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
    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
