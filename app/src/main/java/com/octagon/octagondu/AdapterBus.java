package com.octagon.octagondu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterBus extends RecyclerView.Adapter<AdapterBus.ViewHolder> {
    private ArrayList<InfoBus> dataArrayList;
    private OnItemClickListener itemClickListener;

    public AdapterBus(ArrayList<InfoBus> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_bus, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InfoBus infoBus = dataArrayList.get(position);

        holder.busImage.setImageResource(infoBus.image);
        holder.busName.setText(infoBus.name);
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView busImage;
        TextView busName;

        public ViewHolder(View itemView) {
            super(itemView);
            busImage = itemView.findViewById(R.id.busImage);
            busName = itemView.findViewById(R.id.busName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            itemClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}