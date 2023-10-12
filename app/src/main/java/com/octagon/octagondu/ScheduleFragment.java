package com.octagon.octagondu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment {
    RecyclerView recyclerView;
    ListAdapter listAdapter;
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    ListData listData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_schedule_fragment, container, false);

        recyclerView = view.findViewById(R.id.scheduleRecycleView);
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
            if (i % 5 == 0) {
                imageList[i] = R.drawable.bus;
            } else if(i % 5 == 1) {
                imageList[i] = R.drawable.bus2;
            }else if(i % 5 == 2) {
                imageList[i] = R.drawable.bus3;
            }else if(i % 5 == 3) {
                imageList[i] = R.drawable.bus4;
            }else {
                imageList[i] = R.drawable.bus5;
            }
        }
        for (int i = 0; i < imageList.length; i++) {
            listData = new ListData(nameList[i], imageList[i]);
            dataArrayList.add(listData);
        }

        // Initialize and set the adapter for the RecyclerView
        listAdapter = new ListAdapter(dataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(listAdapter);

        // Set item click listener for RecyclerView items
        listAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click here
                Intent intent = new Intent(getActivity(), BusDetailsActivity.class);
                intent.putExtra("busName", dataArrayList.get(position).getName());
                startActivity(intent);
            }
        });

        return view;
    }
}
