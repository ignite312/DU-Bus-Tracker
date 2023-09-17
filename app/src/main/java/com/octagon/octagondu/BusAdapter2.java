package com.octagon.octagondu;

// BusAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BusAdapter2 extends RecyclerView.Adapter<BusAdapter2.BusViewHolder> {
    private List<BusInformation> busList;

    public BusAdapter2(List<BusInformation> busList) {
        this.busList = busList;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus, parent, false);
        return new BusViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        BusInformation bus = busList.get(position);
        holder.bind(bus);
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public class BusViewHolder extends RecyclerView.ViewHolder {
        private TextView busTypeTextView;
        private TextView busIdTextView;
        private TextView startLocationTextView;
        private TextView destinationLocationTextView;
        private TextView time;

        public BusViewHolder(View itemView) {
            super(itemView);
            busTypeTextView = itemView.findViewById(R.id.text_view_bus_type);
            busIdTextView = itemView.findViewById(R.id.text_view_bus_id);
            startLocationTextView = itemView.findViewById(R.id.text_view_start_location);
            destinationLocationTextView = itemView.findViewById(R.id.text_view_destination_location);
            time = itemView.findViewById(R.id.text_view_bus_time);
        }

        public void bind(BusInformation bus) {
            busTypeTextView.setText("Bus Type: " + bus.getBusType());
            busIdTextView.setText("Bus Number: " + bus.getBusId());
            startLocationTextView.setText("Start Location: " + bus.getStartLocation());
            destinationLocationTextView.setText("Destination Location: " + bus.getDestinationLocation());
            time.setText("Departure Time: " + bus.getTime());
        }
    }
}
