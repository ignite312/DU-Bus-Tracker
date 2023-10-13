package com.octagon.octagondu;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AdapterBusDetails extends RecyclerView.Adapter<AdapterBusDetails.BusViewHolder> {
    private List<InfoBusDetails> busList;
    private String flag;
    public AdapterBusDetails(List<InfoBusDetails> busList) {
        this.busList = busList;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus_details, parent, false);
        return new BusViewHolder(itemView);
    }
    public void setFlag(String flag) {
        this.flag = flag;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }
    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        InfoBusDetails bus = busList.get(position);
        holder.bind(bus);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), flag, Toast.LENGTH_SHORT).show();
                if(flag.equals("1")) {
                    Intent intent = new Intent(view.getContext(), ListBusLocation.class);
                    view.getContext().startActivity(intent);
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Options")
                            .setMessage("Choose an option:")
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(view.getContext(), BusDetailsUpdate.class);
                                    intent.putExtra("busId", bus.getBusId());
                                    intent.putExtra("busName", bus.getBusName());
                                    intent.putExtra("busType", bus.getBusType());
                                    intent.putExtra("destinationLocation", bus.getDestinationLocation());
                                    intent.putExtra("startLocation", bus.getStartLocation());
                                    intent.putExtra("time", bus.getTime());
                                    view.getContext().startActivity(intent);
                                }
                            })
                            .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String busNameToDelete = bus.getBusName();
                                    String timeToDelete = bus.getTime(); // You should obtain this value similarly to how you did in the submit button click handler.

                                    // Check if the bus name and time are not empty
                                    if (!busNameToDelete.isEmpty() && !timeToDelete.isEmpty()) {
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                                        // Construct the path to the data based on bus name and time
                                        String pathToDelete = "Bus Name/" + busNameToDelete + "/" + timeToDelete;

                                        // Check if data exists at the specified path
                                        databaseReference.child(pathToDelete).addListenerForSingleValueEvent(new ValueEventListener() {
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    // Data exists, proceed with deletion
                                                    databaseReference.child(pathToDelete).removeValue()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        // Data deleted successfully
                                                                        Toast.makeText(view.getContext(), "Data Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                                        int position = busList.indexOf(bus);
                                                                        if (position != -1) {
                                                                            deleteItem(position);
                                                                        }
                                                                    } else {
                                                                        // Failed to delete data
                                                                        Toast.makeText(view.getContext(), "Failed to Delete Data", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    // Data does not exist for the specified bus name and time
                                                    Toast.makeText(view.getContext(), "Data Not Found for Bus Name: " + busNameToDelete, Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                // Handle errors here, if needed
                                            }
                                        });
                                    } else {
                                        Toast.makeText(view.getContext(), "Please select a bus name and provide a time.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // This is the "Cancel" button, which closes the dialog without any action
                                    dialog.dismiss(); // Close the dialog
                                }
                            })
                            .setCancelable(true) // Make the dialog cancelable
                            .show();
                }
            }
        });
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
            busIdTextView = itemView.findViewById(R.id.text_view_bus_id);
            startLocationTextView = itemView.findViewById(R.id.text_view_start_location);
            destinationLocationTextView = itemView.findViewById(R.id.text_view_destination_location);
            time = itemView.findViewById(R.id.text_view_bus_time);
        }

        public void bind(InfoBusDetails bus) {
            busIdTextView.setText("Bus Number: " + bus.getBusId());
            startLocationTextView.setText("Start Location: " + bus.getStartLocation());
            destinationLocationTextView.setText("Destination Location: " + bus.getDestinationLocation());
            time.setText("Departure Time: " + bus.getTime());
        }
    }

    public void deleteItem(int position) {
        busList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }
}