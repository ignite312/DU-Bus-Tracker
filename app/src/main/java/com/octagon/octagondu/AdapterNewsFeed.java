package com.octagon.octagondu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        private ImageView userImage;
        private TextView busNameTextView;
        private TextView userTypeTextView;
        private TextView postedByTextView;
        private TextView departmentNameTextView;
        private TextView postDateTextView;
        private TextView postContentTextView;
        private ImageView upvoteImageView;
        private TextView upvoteCountTextView;

        public PostViewHolder(View itemView) {
            super(itemView);
            busNameTextView = itemView.findViewById(R.id.username);
            userTypeTextView = itemView.findViewById(R.id.userType);
            postedByTextView = itemView.findViewById(R.id.username);
            departmentNameTextView = itemView.findViewById(R.id.deptName);
            postDateTextView = itemView.findViewById(R.id.postDate);
            postContentTextView = itemView.findViewById(R.id.mainPost);
            upvoteCountTextView = itemView.findViewById(R.id.voteCount);

            upvoteImageView = itemView.findViewById(R.id.upvoteImageView);
            upvoteImageView.setOnClickListener(view -> {
                Toast.makeText(context, "Upvoted position: " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                upvoteImageView.setImageResource(R.drawable.arrow_upward_green);
            });
        }

        public void bind(InfoNewsFeed infoNewsFeed) {
            // userTypeTextView.setText(infoNewsFeed.getUserType());
            // postedByTextView.setText(infoNewsFeed.getPostedBy());
            // departmentNameTextView.setText(infoNewsFeed.getDept());
            // postDateTextView.setText(infoNewsFeed.getDate());
            // postContentTextView.setText(infoNewsFeed.getDesc());
            // upvoteCountTextView.setText(String.valueOf(infoNewsFeed.getCnt()));
        }
    }
}
