package com.octagon.octagondu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentFindBusLocation extends Fragment {
    RecyclerView recyclerView;
    AdapterBus adapterBus;
    ArrayList<InfoBus> dataArrayList = new ArrayList<>();
    InfoBus infoBus;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_find_bus_location, container, false);

        recyclerView = view.findViewById(R.id.findBusList);
        dataArrayList.clear();
        String[] nameList = {
                "Khonika",
                "Falguni",
                "Choitaly",
                "Ullash",
                "Bosonto",
                "Isha Kha",
                "Anando",
                "Torongo",
                "Hemonto",
                "Boshonto",
                "Boishakhi",
                "Kinchit",
                "Srabon",
                "Moitree",
                "Wari"
        };
        int[] imageList = new int[15];
        for (int i = 0; i < 15; i++) {
            imageList[i] = R.drawable.baseline_share_location_24;
        }
        for (int i = 0; i < imageList.length; i++) {
            infoBus = new InfoBus(nameList[i], imageList[i]);
            dataArrayList.add(infoBus);
        }

        // Initialize and set the adapter for the RecyclerView
        adapterBus = new AdapterBus(dataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapterBus);

        // Set item click listener for RecyclerView items
        adapterBus.setOnItemClickListener(new AdapterBus.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click here
                Intent intent = new Intent(getActivity(), ListBusDetails.class);
                intent.putExtra("busName", dataArrayList.get(position).getName());
                startActivity(intent);
            }
        });

        return view;
    }
}
