package com.octagon.octagondu;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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
                upvoteImageView.setOnClickListener(view -> {
//              Toast.makeText(context, "Upvoted position: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                upvoteImageView.setImageResource(R.drawable.arrow_upward_green);
                voteCountTextView.setText("-78");
            });
        }
    }
}
