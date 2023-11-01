package com.octagon.octagondu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TabUpScFragment extends Fragment {
    View rootView;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerViewUp;
    private AdapterBusDetails busAdapterUp;
    private ProgressBar progressBarUp;
    String busName = "Khonika", flag = "SC";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_up_sc_fragment, container, false);

        /*Got the Bus Name and Flag*/
        Bundle args = getArguments();
        if (args != null) {
            busName = args.getString("busName");
            flag = args.getString("flag");
        }
        progressBarUp = rootView.findViewById(R.id.progress_barUp);
        recyclerViewUp = rootView.findViewById(R.id.recycler_viewUp);
        recyclerViewUp.setLayoutManager(new LinearLayoutManager(getContext()));
        busAdapterUp = new AdapterBusDetails(new ArrayList<>());
        recyclerViewUp.setAdapter(busAdapterUp);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Bus Schedule").child(busName);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<InfoBusDetails> busListUp = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        InfoBusDetails bus = snapshot.getValue(InfoBusDetails.class);
                        if (bus != null) {
                            String temp = String.valueOf(snapshot.child("busType").getValue());
                            if ("Up".equals(temp)) {
                                busListUp.add(bus);
                            }
                        }
                    }
                    if (busListUp.isEmpty()) {
                        showCustomToast("Up Schedule Empty");
                    } else {
                        busAdapterUp = new AdapterBusDetails(busListUp);
                        recyclerViewUp.setAdapter(busAdapterUp);
                        busAdapterUp.setFlag(flag);
                    }
                } else {
                    showCustomToast("No data found for this bus name");
                }
                progressBarUp.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                progressBarUp.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return rootView;
    }
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) rootView.findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.custom_toast_text);
        text.setText(message);

        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 160); // Adjust margins as needed
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
