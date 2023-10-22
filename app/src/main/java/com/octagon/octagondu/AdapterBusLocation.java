package com.octagon.octagondu;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterBusLocation extends RecyclerView.Adapter<AdapterBusLocation.LocationViewHolder> {
    private List<InfoBusLocation> locationList;
    FirebaseStorage storage;
    private Context context;

    public AdapterBusLocation(Context context, List<InfoBusLocation> locationList) {
        this.locationList = locationList;
        this.context = context;
    }
    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus_location, parent, false);
        return new LocationViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull AdapterBusLocation.LocationViewHolder holder, @SuppressLint("RecyclerView") int position) {
        InfoBusLocation location = locationList.get(position);
        holder.bind(location, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get data for the clicked item
                InfoBusLocation clickedLocation = locationList.get(position);

                double latitude = Double.parseDouble(clickedLocation.getLat());
                double longitude = Double.parseDouble(clickedLocation.getLon());

                Intent intent = new Intent(view.getContext(), LocationOthers.class); // Use view.getContext() to get the context
                intent.putExtra("LAT", latitude);
                intent.putExtra("LON", longitude);
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void setList(List<InfoBusLocation> locationList) {
        this.locationList = locationList;
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        private TextView userName;
        private TextView userType;
        private TextView deptNameSession;
        private TextView lastLocation;
        private TextView lastTime;
        private TextView lastDate;
        private TextView countdown;
        private TextView voteCount;
        private ImageView upvoteImageView;
        private ImageView downVoteImageView;
        private ImageView reportProfile;

        public LocationViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.photoLocation);
            userName = itemView.findViewById(R.id.usernameLocation);
            userType = itemView.findViewById(R.id.UserTypeLocation);
            deptNameSession = itemView.findViewById(R.id.deptNameSessionLocation);
            lastLocation = itemView.findViewById(R.id.lastLocation);
            lastTime = itemView.findViewById(R.id.lastTime);
            lastDate = itemView.findViewById(R.id.lastDate);
            countdown = itemView.findViewById(R.id.timeDiff);
            voteCount = itemView.findViewById(R.id.voteCountLocation);
            upvoteImageView = itemView.findViewById(R.id.upvoteLocation);
            downVoteImageView = itemView.findViewById(R.id.downVoteLocation);
            reportProfile = itemView.findViewById(R.id.reportProfileLocaton);
        }
        public void bind(InfoBusLocation infoBusLocation, int position) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserInfo").child(infoBusLocation.getRegNum());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        userName.setText(String.valueOf(dataSnapshot.child("fullName").getValue()));
                        userType.setText("● " + dataSnapshot.child("userType").getValue());
                        deptNameSession.setText(dataSnapshot.child("department").getValue() + " " +
                                dataSnapshot.child("session").getValue());
                        try {
                            storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference();
                            StorageReference imagesRef = storageRef.child((String) Objects.requireNonNull(dataSnapshot.child("userImage").getValue()));

                            File localFile = File.createTempFile("tempFile", ".png");
                            imagesRef.getFile(localFile)
                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                            circleImageView.setImageBitmap(bitmap); // Use setImageBitmap
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showToast("Error: " + e.getMessage());
                                        }
                                    });
//
                        }catch (Exception e) {
                            showToast(e.getMessage());
                        }
                    } else {
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error fetching data", databaseError.toException());
                }
            });

            DatabaseReference reference_dynamic_change = FirebaseDatabase.getInstance().getReference("UserInfo").child(infoBusLocation.getRegNum());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        userName.setText(String.valueOf(dataSnapshot.child("fullName").getValue()));
                        userType.setText("● " + dataSnapshot.child("userType").getValue());
                        deptNameSession.setText(dataSnapshot.child("department").getValue() + " " +
                                dataSnapshot.child("session").getValue());
                        try {
                            storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference();
                            StorageReference imagesRef = storageRef.child((String) Objects.requireNonNull(dataSnapshot.child("userImage").getValue()));

                            File localFile = File.createTempFile("tempFile", ".png");
                            imagesRef.getFile(localFile)
                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                            circleImageView.setImageBitmap(bitmap); // Use setImageBitmap
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showToast("Error: " + e.getMessage());
                                        }
                                    });
//
                        }catch (Exception e) {
                            showToast(e.getMessage());
                        }
                    } else {
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error fetching data", databaseError.toException());
                }
            });



            String timeDifference = getTimeDifference(infoBusLocation.getTime() + " " + infoBusLocation.getDate(), "HH:mm:ss dd MMM yyyy");
            countdown.setText(timeDifference);
            lastDate.setText(infoBusLocation.getDate());
            lastTime.setText(infoBusLocation.getTime());
            if(infoBusLocation.getVoteCount() == null) {
                voteCount.setText("0");
            }else voteCount.setText(String.valueOf(infoBusLocation.getVoteCount()));
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
    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
