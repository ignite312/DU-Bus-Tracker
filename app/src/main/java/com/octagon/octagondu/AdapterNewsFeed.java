package com.octagon.octagondu;

import static com.octagon.octagondu.MainActivity.DUREGNUM;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

import android.graphics.Bitmap;


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterNewsFeed extends RecyclerView.Adapter<AdapterNewsFeed.PostViewHolder> {
    private List<InfoNewsFeed> postList;
    private Context context;
    Map<Integer, Boolean> booleanMap = new HashMap<>();
    FirebaseStorage storage;
    String flag;
    PopupWindow popupWindow;


    public AdapterNewsFeed(Context context, List<InfoNewsFeed> postList) {
        this.context = context;
        this.postList = postList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFlag(String flag) {
        this.flag = flag;
        notifyDataSetChanged();
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
        holder.bind(infoNewsFeed, position);
    }

    public void removePost(InfoNewsFeed post) {
        postList.remove(post);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView userNameTextView;
        private TextView departmentNameSessionTextView;
        private TextView userTypeTextView;
        private TextView busNameTextView;
        private TextView postDateTextView;
        private TextView postTitleTextView;
        private TextView postDescTextView;
        private TextView voteCountTextView;
        private ImageView upvoteImageView;
        private ImageView downVoteImageView;
        CircleImageView circleImageView;
        private ImageView commentImageView;
        private ImageView threeDotImageView;
        private  TextView approval;
        private FrameLayout approvalFrame;

        public PostViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.username);
            departmentNameSessionTextView = itemView.findViewById(R.id.deptName);
            busNameTextView = itemView.findViewById(R.id.busNameUserType);
            postDateTextView = itemView.findViewById(R.id.postDate);
            postTitleTextView = itemView.findViewById(R.id.Title);
            postDescTextView = itemView.findViewById(R.id.desc);
            voteCountTextView = itemView.findViewById(R.id.voteCount);
            upvoteImageView = itemView.findViewById(R.id.upvoteImageView);
            downVoteImageView = itemView.findViewById(R.id.downVoteImageView);
            userTypeTextView = itemView.findViewById(R.id.userType);
            circleImageView = itemView.findViewById(R.id.photo);
            commentImageView = itemView.findViewById(R.id.commentImageViewFeed);
            threeDotImageView = itemView.findViewById(R.id.threeDotImageView);
            approval = itemView.findViewById(R.id.approval);
            approvalFrame  = itemView.findViewById(R.id.approvalFrame);
        }

        @SuppressLint("SetTextI18n")
        public void bind(InfoNewsFeed infoNewsFeed, int position) {
            if (flag.equals("FEED") || flag.equals("SC") || flag.equals("AD") || flag.equals("ADN")) {
                departmentNameSessionTextView.setVisibility(View.VISIBLE);
                userTypeTextView.setVisibility(View.VISIBLE);
            }
            if (flag.equals("PM") || flag.equals("PO")) {
                departmentNameSessionTextView.setVisibility(View.GONE);
                userTypeTextView.setVisibility(View.GONE);
            }
            if(flag.equals("PM")) {
                approval.setVisibility(View.VISIBLE);
                approvalFrame.setVisibility(View.VISIBLE);
                if (infoNewsFeed.getStatus().equals("0")) {
                    approval.setTextColor(Color.parseColor("#0000FF"));
                    approval.setText("Pending");
                }
            }
            if(flag.equals("SC")) {
                threeDotImageView.setVisibility(View.GONE);
            }
            DatabaseReference ViewUserInfoRef = FirebaseDatabase.getInstance().getReference("UserInfo").child(infoNewsFeed.getUserId());
            ViewUserInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        userNameTextView.setText(String.valueOf(dataSnapshot.child("fullName").getValue()));
                        departmentNameSessionTextView.setText(dataSnapshot.child("department").getValue() + " " +
                                dataSnapshot.child("session").getValue());
                        userTypeTextView.setText("● " + dataSnapshot.child("userType").getValue());
                        busNameTextView.setText(infoNewsFeed.getBusName());
                        try {
                            storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference();
                            StorageReference imagesRef = storageRef.child((String) Objects.requireNonNull(dataSnapshot.child("userImage").getValue()));

                            /*Using Picasso
                            DatabaseReference imagesRef  = FirebaseDatabase.getInstance().getReference("image");
                            imagesRef.addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(
                                                @NonNull DataSnapshot dataSnapshot) {
                                            // getting a DataSnapshot for the
                                            // location at the specified relative
                                            // path and getting in the link variable
                                            String link = dataSnapshot.getValue(
                                                    String.class);

                                            // loading that data into rImage
                                            // variable which is ImageView
                                            Picasso.get().load(link).into(circleImageView);
                                        }

                                        // this will called when any problem
                                        // occurs in getting data
                                        @Override
                                        public void onCancelled(
                                                @NonNull DatabaseError databaseError) {
                                            showToast("Hehe");
                                        }
                                    });
                             */
                            File localFile = new File(context.getCacheDir(), infoNewsFeed.getUserId() + ".png");
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
                                                showCustomToast("Error: " + e.getMessage());
                                            }
                                        });
                            }

                        } catch (Exception e) {
                            showCustomToast(e.getMessage());
                        }
                    } else {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error fetching data", databaseError.toException());
                }
            });
            /*Time Difference now and when posted*/
            String timeDifference = getTimeDifference(infoNewsFeed.getTime() + " " + infoNewsFeed.getDate(), "HH:mm:ss dd MMM yyyy");
            postDateTextView.setText(timeDifference);
            postDateTextView.setOnClickListener(View -> {
                if (booleanMap.get(position) == null) {
                    postDateTextView.setText(convertTimeToAMPM(infoNewsFeed.getTime()) + ", " + convertDateToDay(infoNewsFeed.getDate()));
                    booleanMap.put(position, false);
                } else {
                    if (Boolean.TRUE.equals(booleanMap.get(position))) {
                        postDateTextView.setText(convertTimeToAMPM(infoNewsFeed.getTime()) + ", " + convertDateToDay(infoNewsFeed.getDate()));
                        booleanMap.put(position, false);
                    } else {
                        postDateTextView.setText(getTimeDifference(infoNewsFeed.getTime() + " " + infoNewsFeed.getDate(), "HH:mm:ss dd MMM yyyy"));
                        booleanMap.put(position, true);
                    }
                }
            });
            postTitleTextView.setText(infoNewsFeed.getTitle());
            postDescTextView.setText(infoNewsFeed.getDesc());

            DatabaseReference UpDownSymbolRef = FirebaseDatabase.getInstance().getReference("Feed/PostReactions/" + infoNewsFeed.getPostId() + "/" + DUREGNUM);
            if(flag.equals("SC") || flag.equals("ADN"))UpDownSymbolRef = FirebaseDatabase.getInstance().getReference("Notice/" + infoNewsFeed.getBusName() + "/PostReactions/" + infoNewsFeed.getPostId() + "/" + DUREGNUM);
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
                    showCustomToast("Firebase Error fetching data");
                }
            });
            DatabaseReference VoteCountRef = FirebaseDatabase.getInstance().getReference("Feed/Posts").child(infoNewsFeed.getPostId()).child("/totalVote");
            if(flag.equals("SC") || flag.equals("ADN"))VoteCountRef = FirebaseDatabase.getInstance().getReference("Notice/" + infoNewsFeed.getBusName() + "/Posts").child(infoNewsFeed.getPostId()).child("/totalVote");
            VoteCountRef.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String t_totalVote = String.valueOf(dataSnapshot.getValue());
                        if (Integer.parseInt(t_totalVote) <= 0)
                            voteCountTextView.setText(t_totalVote);
                        else voteCountTextView.setText("+" + t_totalVote);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showCustomToast("Firebase Error fetching data");
                }
            });
            upvoteImageView.setOnClickListener(view -> {
                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() == null) {
                    showCustomToast("Create A Account First!");
                    Intent intent = new Intent(context, SignInUser.class);
                    context.startActivity(intent);
                    return;
                }
                DatabaseReference UpDownSymbolRef2 = FirebaseDatabase.getInstance().getReference("Feed/PostReactions/" + infoNewsFeed.getPostId() + "/" + DUREGNUM);
                if(flag.equals("SC") || flag.equals("ADN"))UpDownSymbolRef2 = FirebaseDatabase.getInstance().getReference("Notice/" + infoNewsFeed.getBusName() + "/PostReactions/" + infoNewsFeed.getPostId() + "/" + DUREGNUM);
                DatabaseReference finalUpDownSymbolRef = UpDownSymbolRef2;
                UpDownSymbolRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String _react = String.valueOf(dataSnapshot.getValue());
                            if (_react.equals("00")) {
                                finalUpDownSymbolRef.setValue("10");
                                update(1, 1, infoNewsFeed.getUserId(), infoNewsFeed.getPostId(), infoNewsFeed.getBusName());
                            } else if (_react.equals("10")) {
                                finalUpDownSymbolRef.setValue("00");
                                update(-1, -1, infoNewsFeed.getUserId(), infoNewsFeed.getPostId(), infoNewsFeed.getBusName());
                            } else if (_react.equals("01")) {
                                finalUpDownSymbolRef.setValue("10");
                                update(2, 2, infoNewsFeed.getUserId(), infoNewsFeed.getPostId(), infoNewsFeed.getBusName());
                            }
                        } else {
                            finalUpDownSymbolRef.setValue("10");
                            update(1, 1, infoNewsFeed.getUserId(), infoNewsFeed.getPostId(), infoNewsFeed.getBusName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Error fetching data", databaseError.toException());
                    }
                });
            });

            downVoteImageView.setOnClickListener(view -> {
                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() == null) {
                    showCustomToast("Create A Account First!");
                    Intent intent = new Intent(context, SignInUser.class);
                    context.startActivity(intent);
                    return;
                }
                DatabaseReference UpDownSymbolRef2 = FirebaseDatabase.getInstance().getReference("Feed/PostReactions/" + infoNewsFeed.getPostId() + "/" + DUREGNUM);
                if(flag.equals("SC") || flag.equals("ADN"))UpDownSymbolRef2 = FirebaseDatabase.getInstance().getReference("Notice/" + infoNewsFeed.getBusName() + "/PostReactions/" + infoNewsFeed.getPostId() + "/" + DUREGNUM);
                DatabaseReference finalUpDownSymbolRef = UpDownSymbolRef2;
                UpDownSymbolRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String _react = String.valueOf(dataSnapshot.getValue());
                            if (_react.equals("00")) {
                                finalUpDownSymbolRef.setValue("01");
                                update(-1, -1, infoNewsFeed.getUserId(), infoNewsFeed.getPostId(), infoNewsFeed.getBusName());
                            } else if (_react.equals("10")) {
                                finalUpDownSymbolRef.setValue("01");
                                update(-2, -2, infoNewsFeed.getUserId(), infoNewsFeed.getPostId(), infoNewsFeed.getBusName());
                            } else if (_react.equals("01")) {
                                finalUpDownSymbolRef.setValue("00");
                                update(1, 1, infoNewsFeed.getUserId(), infoNewsFeed.getPostId(), infoNewsFeed.getBusName());
                            }
                        } else {
                            finalUpDownSymbolRef.setValue("01");
                            update(-1, -1, infoNewsFeed.getUserId(), infoNewsFeed.getPostId(), infoNewsFeed.getBusName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Error fetching data", databaseError.toException());
                    }
                });
            });

            commentImageView.setOnClickListener(view -> {
                showCustomToast("Coming Soon!");
            });

            threeDotImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Use a LayoutInflater to inflate the custom layout
                    LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View customDialogView = inflater.inflate(R.layout.custom_dialog_layout, null);

                    // Calculate the screen dimensions
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    ((Activity) view.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                    // Create a PopupWindow to display the custom layout
                    popupWindow = new PopupWindow(customDialogView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

                    // Center the PopupWindow on the screen
                    int width = (int) (displayMetrics.widthPixels * 0.8); // Adjust as needed
                    int height = WindowManager.LayoutParams.WRAP_CONTENT;
                    popupWindow.setWidth(width);
                    popupWindow.setHeight(height);

                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                    Button btnUpdate = customDialogView.findViewById(R.id.btnUpdate);
                    Button btnDelete = customDialogView.findViewById(R.id.btnDelete);
                    Button btnCancel = customDialogView.findViewById(R.id.btnCancel);
                    Button btnApprove = customDialogView.findViewById(R.id.btnApprove);
                    Button btnReport = customDialogView.findViewById(R.id.btnReport);
                    View viewUpdate = customDialogView.findViewById(R.id.updateLine);
                    View viewDelete = customDialogView.findViewById(R.id.deleteLine);
                    View viewApprove = customDialogView.findViewById(R.id.approveLine);
                    View viewReport = customDialogView.findViewById(R.id.reportline);
                    if(flag.equals("FEED") || flag.equals("PO")) {
                        btnReport.setVisibility(View.VISIBLE);
                        viewReport.setVisibility(View.VISIBLE);
                        btnUpdate.setVisibility(View.GONE);
                        viewUpdate.setVisibility(View.GONE);
                        btnDelete.setVisibility(View.GONE);
                        viewDelete.setVisibility(View.GONE);
                        btnApprove.setVisibility(View.GONE);
                        viewApprove.setVisibility(View.GONE);
                    }
                    if(flag.equals("AD")) {
                        btnReport.setVisibility(View.GONE);
                        viewReport.setVisibility(View.GONE);
                        btnUpdate.setVisibility(View.GONE);
                        viewUpdate.setVisibility(View.GONE);
                        btnDelete.setVisibility(View.VISIBLE);
                        viewDelete.setVisibility(View.VISIBLE);
                        btnApprove.setVisibility(View.VISIBLE);
                        viewApprove.setVisibility(View.VISIBLE);
                    }
                    if(flag.equals("ADN")) {
                        btnUpdate.setVisibility(View.VISIBLE);
                        viewUpdate.setVisibility(View.VISIBLE);
                        btnApprove.setVisibility(View.GONE);
                        viewApprove.setVisibility(View.GONE);
                    }
                    btnUpdate.setOnClickListener(dialog -> {

                        Intent intent = new Intent(context, CreatePost.class);
                        if(flag.equals("ADN")) {
                            intent = new Intent(context, CreateNotice.class);
                        }
                        intent.putExtra("HT", infoNewsFeed.getHelpType());
                        intent.putExtra("BUSNAME", infoNewsFeed.getBusName());
                        intent.putExtra("TT", infoNewsFeed.getTitle());
                        intent.putExtra("DEC", infoNewsFeed.getDesc());
                        if(flag.equals("ADN")) {
                            intent.putExtra("FLAG", "PM");
                        }else intent.putExtra("FLAG", "ADN");
                        intent.putExtra("POSTID", infoNewsFeed.getPostId());
                        intent.putExtra("VOTECNT", String.valueOf(infoNewsFeed.getTotalVote()));
                        context.startActivity(intent);
                        popupWindow.dismiss();
                    });

                    btnDelete.setOnClickListener(dialog -> {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Feed/Posts/" + infoNewsFeed.getPostId());
                        if(flag.equals("ADN"))databaseReference = FirebaseDatabase.getInstance().getReference("Notice/" + infoNewsFeed.getBusName() + "/Posts/" + infoNewsFeed.getPostId());
                        DatabaseReference finalDatabaseReference = databaseReference;
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    finalDatabaseReference.removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        showCustomToast("Post Deleted");
                                                        removePost(infoNewsFeed);

                                                    } else {
                                                        showCustomToast("Failed to Delete");
                                                    }
                                                }
                                            });
                                } else {
                                    showCustomToast("Data Not Found");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        popupWindow.dismiss();
                    });
                    btnCancel.setOnClickListener(dialog -> {
                        popupWindow.dismiss();
                    });
                    btnApprove.setOnClickListener(dialog -> {
                        showCustomToast("Approved!");
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Feed/Posts/" + infoNewsFeed.getPostId() + "/status");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    ref.setValue("1");
                                    removePost(infoNewsFeed);
                                } else {
                                    showCustomToast("Data Not Found");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        popupWindow.dismiss();
                    });
                    btnReport.setOnClickListener(dialog -> {
                        showCustomToast("Will Added Soon!");
                        popupWindow.dismiss();
                    });
                }
            });

            try{
                if (!(flag.equals("PM") || flag.equals("PO"))) {
                    View.OnClickListener commonOnClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ProfileOthers.class);
                            intent.putExtra("UID", infoNewsFeed.getUserId());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // Add this line
                            context.startActivity(intent);
                        }
                    };
                    userNameTextView.setOnClickListener(commonOnClickListener);
                    circleImageView.setOnClickListener(commonOnClickListener);
                }
            }catch (Exception e) {
                showToast(e.getMessage());
            }
        }
    }


    public String convertTimeToAMPM(String timeString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");

            Date date = inputFormat.parse(timeString);

            String timeInAMPM = outputFormat.format(date);

            return timeInAMPM;
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid Time Format";
        }
    }

    public static String convertDateToDay(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMM yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, MMM dd");

            Date date = inputFormat.parse(dateString);

            String formattedDate = outputFormat.format(date);

            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid Date Format";
        }
    }

    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

    private void update(int drc, int dcc, String UID, String POSTID, String busName) {
        DatabaseReference VoteCountRef = FirebaseDatabase.getInstance().getReference("Feed/Posts").child(POSTID).child("/totalVote");
        if(flag.equals("SC") || flag.equals("ADN"))VoteCountRef = FirebaseDatabase.getInstance().getReference("Notice/" + busName + "/Posts").child(POSTID).child("/totalVote");
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

    private void showCustomToast(String message) {
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
