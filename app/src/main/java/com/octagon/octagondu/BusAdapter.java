package com.octagon.octagondu;
// BusAdapter.java

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {

    private List<String> busNames;
    private Context context;

    public BusAdapter(Context context, List<String> busNames) {
        this.context = context;
        this.busNames = busNames;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_list_item, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        String busName = busNames.get(position);
        holder.tvBusName.setText(busName);
        // Set onClickListener for each bus name item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BusDetailsActivity.class);
                intent.putExtra("busName", busName); // Pass the clicked bus name
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return busNames.size();
    }

    public static class BusViewHolder extends RecyclerView.ViewHolder {
        TextView tvBusName;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBusName = itemView.findViewById(R.id.tvBusName);
        }
    }
}
