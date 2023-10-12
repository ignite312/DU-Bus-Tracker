package com.octagon.octagondu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);
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
        private ImageView busImage;
        private TextView busNameTextView;
        private  TextView userTypeTextView;
        private TextView postedByTextView;
        private TextView departmentNameTextView;
        private TextView postDateTextView;
        private TextView postContentTextView;
        private ImageView upvoteImageView;
        private TextView upvoteCountTextView;

        public PostViewHolder(View itemView) {
            super(itemView);
            busImage = itemView.findViewById(R.id.busImage);
            busNameTextView = itemView.findViewById(R.id.busName);
            userTypeTextView = itemView.findViewById(R.id.userTypeLabel);
            postedByTextView = itemView.findViewById(R.id.postedBy);
            departmentNameTextView = itemView.findViewById(R.id.departmentName);
            postDateTextView = itemView.findViewById(R.id.postDate);
            postContentTextView = itemView.findViewById(R.id.postContent);
            upvoteImageView = itemView.findViewById(R.id.upvoteImageView);
            upvoteCountTextView = itemView.findViewById(R.id.upvoteCount);
        }

        public void bind(InfoNewsFeed infoNewsFeed) {
            busImage.setImageResource(infoNewsFeed.getImage());
            busNameTextView.setText(infoNewsFeed.getBusName());
            userTypeTextView.setText(infoNewsFeed.getUserType());
            postedByTextView.setText(infoNewsFeed.getPosted_by());
            departmentNameTextView.setText(infoNewsFeed.getDept());
            postDateTextView.setText(infoNewsFeed.getDate());
            postContentTextView.setText(infoNewsFeed.getDesc());
            upvoteCountTextView.setText(String.valueOf(infoNewsFeed.getCnt()));
        }
    }
}
