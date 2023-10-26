package com.octagon.octagondu;

import static com.octagon.octagondu.MainActivity.DUREGNUM;

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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
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
    public void onBindViewHolder(@NonNull LocationViewHolder holder, @SuppressLint("RecyclerView") int position) {
        InfoBusLocation location = locationList.get(position);
        holder.bind(location, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoBusLocation clickedLocation = locationList.get(position);

                double latitude = Double.parseDouble(clickedLocation.getLat());
                double longitude = Double.parseDouble(clickedLocation.getLon());

                Intent intent = new Intent(view.getContext(), LocationView.class); // Use view.getContext() to get the context
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
        private TextView departmentNameSessionTextView;
        private TextView lastLocation;
        private TextView lastTime;
        private TextView lastDate;
        private TextView countdown;
        private TextView voteCountLocation;
        private ImageView upvoteImageView;
        private ImageView downVoteImageView;
        private ImageView reportProfile;

        public LocationViewHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.photoLocation);
            userName = itemView.findViewById(R.id.usernameLocation);
            userType = itemView.findViewById(R.id.UserTypeLocation);
            departmentNameSessionTextView = itemView.findViewById(R.id.deptNameSessionLocation);
            lastLocation = itemView.findViewById(R.id.lastLocation);
            lastTime = itemView.findViewById(R.id.lastTime);
            lastDate = itemView.findViewById(R.id.lastDate);
            countdown = itemView.findViewById(R.id.timeDiff);
            voteCountLocation = itemView.findViewById(R.id.voteCountLocation);
            upvoteImageView = itemView.findViewById(R.id.upvoteLocation);
            downVoteImageView = itemView.findViewById(R.id.downVoteLocation);
            reportProfile = itemView.findViewById(R.id.reportProfileLocaton);
        }

        @SuppressLint("SetTextI18n")
        public void bind(InfoBusLocation infoBusLocation, int position) {
            DatabaseReference ViewUserInfoRef = FirebaseDatabase.getInstance().getReference("UserInfo").child(infoBusLocation.getRegNum());
            ViewUserInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        userName.setText(String.valueOf(dataSnapshot.child("fullName").getValue()));
                        userType.setText("● " + dataSnapshot.child("userType").getValue());
                        departmentNameSessionTextView.setText(dataSnapshot.child("department").getValue() + " " +
                                dataSnapshot.child("session").getValue());
                        try {
                            storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference();
                            StorageReference imagesRef = storageRef.child((String) Objects.requireNonNull(dataSnapshot.child("userImage").getValue()));

                            File localFile = new File(context.getCacheDir(), infoBusLocation.getRegNum() + ".png");
                            if (localFile.exists()) {
//                                showToast("Image Loaded from File");
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                circleImageView.setImageBitmap(bitmap);
                            } else {
                                imagesRef.getFile(localFile)
                                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                circleImageView.setImageBitmap(bitmap);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                showToast("Error: " + e.getMessage());
                                            }
                                        });
                            }
//
                        } catch (Exception e) {
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

            lastDate.setText("Date: " + infoBusLocation.getDate());

            DatabaseReference DynamicChangeRef = FirebaseDatabase.getInstance().getReference("Location").child(infoBusLocation.getDate()).child(infoBusLocation.getBusName()).child(infoBusLocation.getBusTime()).child("/Locations/").child(infoBusLocation.getRegNum());

            DynamicChangeRef.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        lastLocation.setText("Last Place: Near " + dataSnapshot.child("lastLocation").getValue());
                        lastTime.setText("Last Seen: " + dataSnapshot.child("time").getValue());
                        countdown.setText(getTimeDifference(dataSnapshot.child("time").getValue() + " " + infoBusLocation.getDate(), "HH:mm:ss dd MMM yyyy"));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error fetching data", databaseError.toException());
                }
            });

            DatabaseReference UpDownSymbolRef = FirebaseDatabase.getInstance().getReference("Location").child(infoBusLocation.getDate()).child(infoBusLocation.getBusName()).child(infoBusLocation.getBusTime()).child("/LocationReactions/").child(infoBusLocation.getRegNum()).child(DUREGNUM);
            UpDownSymbolRef.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String react = String.valueOf(dataSnapshot.getValue());
                        if (react.equals("10")) {
                            upvoteImageView.setImageResource(R.drawable.arrow_upward_green);
                            downVoteImageView.setImageResource(R.drawable.arrow_downward);
                        } else if (react.equals("01")) {
                            upvoteImageView.setImageResource(R.drawable.arrow_upward);
                            downVoteImageView.setImageResource(R.drawable.arrow_downward_red);
                        } else {
                            upvoteImageView.setImageResource(R.drawable.arrow_upward);
                            downVoteImageView.setImageResource(R.drawable.arrow_downward);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showToast("Firebase Error fetching data");
                }
            });

            DatabaseReference VoteCountRef = FirebaseDatabase.getInstance().getReference("Location").child(infoBusLocation.getDate()).child(infoBusLocation.getBusName()).child(infoBusLocation.getBusTime()).child("/Locations/").child(infoBusLocation.getRegNum()).child("voteCountLocations");
            VoteCountRef.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String t_totalVote = String.valueOf(dataSnapshot.getValue());
                        if (Integer.parseInt(t_totalVote) <= 0)
                            voteCountLocation.setText(t_totalVote);
                        else voteCountLocation.setText("+" + t_totalVote);
                    } else {
                        voteCountLocation.setText("0");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showToast("Firebase Error fetching data");
                }
            });

            upvoteImageView.setOnClickListener(view -> {
                UpDownSymbolRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String _react = String.valueOf(dataSnapshot.getValue());
                            if (_react.equals("00")) {
                                UpDownSymbolRef.setValue("10");
                                update(1, 1, infoBusLocation.getRegNum(), VoteCountRef);
                            } else if (_react.equals("10")) {
                                UpDownSymbolRef.setValue("00");
                                update(-1, -1, infoBusLocation.getRegNum(), VoteCountRef);
                            } else if (_react.equals("01")) {
                                UpDownSymbolRef.setValue("10");
                                update(2, 2, infoBusLocation.getRegNum(), VoteCountRef);
                            }
                        } else {
                            UpDownSymbolRef.setValue("10");
                            update(1, 1, infoBusLocation.getRegNum(), VoteCountRef);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Error fetching data", databaseError.toException());
                    }
                });
            });

            downVoteImageView.setOnClickListener(view -> {
                UpDownSymbolRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String _react = String.valueOf(dataSnapshot.getValue());
                            if (_react.equals("00")) {
                                UpDownSymbolRef.setValue("01");
                                update(-1, -1, infoBusLocation.getRegNum(), VoteCountRef);
                            } else if (_react.equals("10")) {
                                UpDownSymbolRef.setValue("01");
                                update(-2, -2, infoBusLocation.getRegNum(), VoteCountRef);
                            } else if (_react.equals("01")) {
                                UpDownSymbolRef.setValue("00");
                                update(1, 1, infoBusLocation.getRegNum(), VoteCountRef);
                            }
                        } else {
                            UpDownSymbolRef.setValue("01");
                            update(-1, -1, infoBusLocation.getRegNum(), VoteCountRef);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Error fetching data", databaseError.toException());
                    }
                });
            });
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

    private void update(int drc, int dcc, String UID, DatabaseReference VoteCountRef) {
        VoteCountRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer value = mutableData.getValue(Integer.class);
                if (value == null) {
                    mutableData.setValue(drc);
                } else {
                    mutableData.setValue(value + drc);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                } else {
                    Integer updatedValue = dataSnapshot.getValue(Integer.class);
                }
            }
        });
        DatabaseReference ContributionCountRef = FirebaseDatabase.getInstance().getReference("UserInfo/" + UID + "/contributionCount");
        ContributionCountRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer value = mutableData.getValue(Integer.class);
                if (value == null) {
                    mutableData.setValue(dcc);
                } else {
                    mutableData.setValue(value + dcc);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                } else {
                    Integer updatedValue = dataSnapshot.getValue(Integer.class);
                }
            }
        });
    }
}
