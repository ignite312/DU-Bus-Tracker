package com.octagon.octagondu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

    @SuppressLint("NotifyDataSetChanged")
    public void setFlag(String flag) {
        this.flag = flag;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        InfoBusDetails bus = busList.get(position);
        holder.bind(bus, flag);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag.equals("LC")) {
                    FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    if (mAuth.getCurrentUser() == null) {
                        showCustomToast("Create An Account First!", view.getContext());
                        Intent intent = new Intent(view.getContext(), SignInUser.class);
                        view.getContext().startActivity(intent);
                        return;
                    }
                    Intent intent = new Intent(view.getContext(), ListBusLocation.class);
                    intent.putExtra("BUSNAME", bus.getBusName());
                    intent.putExtra("BUSTIME", bus.getTime());
                    view.getContext().startActivity(intent);
                }else if (flag.equals("AD")) {
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
                                    String timeToDelete = bus.getTime();
                                    if (!busNameToDelete.isEmpty() && !timeToDelete.isEmpty()) {
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                                        String pathToDelete = "Bus Schedule/" + busNameToDelete + "/" + timeToDelete;

                                        databaseReference.child(pathToDelete).addListenerForSingleValueEvent(new ValueEventListener() {
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    // Data exists, proceed with deletion
                                                    databaseReference.child(pathToDelete).removeValue()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        showCustomToast("Data Deleted Successfully", view.getContext());
                                                                        int position = busList.indexOf(bus);
                                                                        if (position != -1) {
                                                                            deleteItem(position);
                                                                        }
                                                                    } else {
                                                                        showCustomToast("Failed to Delete Data", view.getContext());
                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    showCustomToast("Data Not Found for Bus Name: " + busNameToDelete, view.getContext());
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                // Handle errors here, if needed
                                            }
                                        });
                                    }
                                }
                            })
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setCancelable(true)
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
        ImageView imageView;

        public BusViewHolder(View itemView) {
            super(itemView);
            busIdTextView = itemView.findViewById(R.id.text_view_bus_id);
            startLocationTextView = itemView.findViewById(R.id.text_view_start_location);
            destinationLocationTextView = itemView.findViewById(R.id.text_view_destination_location);
            time = itemView.findViewById(R.id.text_view_bus_time);
            imageView = itemView.findViewById(R.id.upOrDown);
        }

        public void bind(InfoBusDetails bus, String flag) {
            busIdTextView.setText("Bus Number: " + bus.getBusId());
            startLocationTextView.setText("Start Location: " + bus.getStartLocation());
            destinationLocationTextView.setText("Destination Location: " + bus.getDestinationLocation());
            time.setText("Departure Time: " + bus.getTime());
            if (bus.getBusType().equals("Up")) imageView.setImageResource(R.drawable.uptime);
            else imageView.setImageResource(R.drawable.downtime);
        }
    }

    public void deleteItem(int position) {
        busList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    private void showCustomToast(String message, Context context) {
        View layout = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
        TextView text = layout.findViewById(R.id.custom_toast_text);
        text.setText(message);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 160); // Adjust margins as needed
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
