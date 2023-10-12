package com.octagon.octagondu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterBusLocation extends RecyclerView.Adapter<AdapterBusLocation.LocationViewHolder> {
    private List<InfoBusLocation> locationList;

    public AdapterBusLocation(List<InfoBusLocation> locationList) {
        this.locationList = locationList;
    }
    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus_location, parent, false);
        return new LocationViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, @SuppressLint("RecyclerView") int position) {
        InfoBusLocation location = locationList.get(position);
        holder.bind(location);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get data for the clicked item
                InfoBusLocation clickedLocation = locationList.get(position);

                double latitude = Double.parseDouble(clickedLocation.getLat());
                double longitude = Double.parseDouble(clickedLocation.getLon());

                Intent intent = new Intent(view.getContext(), LocationOthers.class); // Use view.getContext() to get the context
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                view.getContext().startActivity(intent);

                // Show the toast message
//                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void setList(List<InfoBusLocation> locationList) {
        this.locationList = locationList;
        notifyDataSetChanged();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        private TextView name;
        private TextView dept;
        private TextView lastLocation;
        private TextView lastTime;
        private TextView lastDate;
        private TextView countdown;

        public LocationViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.photo);
            name = itemView.findViewById(R.id.username);
            dept = itemView.findViewById(R.id.deptName);
            lastLocation = itemView.findViewById(R.id.lastLocation);
            lastTime = itemView.findViewById(R.id.lastTime);
            lastDate = itemView.findViewById(R.id.lastDate);
            countdown = itemView.findViewById(R.id.countdown);
        }
        public void bind(InfoBusLocation location) {
            circleImageView.setImageResource(location.getPicture());
            name.setText(location.getName());
            dept.setText(location.getDept());
            lastLocation.setText("Last Location: " + location.getLastLocation());
            lastTime.setText("Last Time: " + location.getTime());
            lastDate.setText("Date: " + location.getDate());
            String inputDate = location.getDate()+ " " + location.getTime();
            String inputFormat = "dd MMM yyyy HH:mm:ss";
            String timeDifference = getTimeDifference(inputDate, inputFormat);
            countdown.setText(timeDifference);
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

}
