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

public class FragmentLocation extends Fragment {
    RecyclerView recyclerView;
    AdapterBus adapterBus;
    ArrayList<InfoBus> dataArrayList = new ArrayList<>();
    InfoBus infoBus;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        recyclerView = view.findViewById(R.id.findBusList);
        dataArrayList.clear();
        String[] nameList = {
                "Khonika",
                "Torongo",
                "Kinchit",
                "Choitaly",
                "Srabon",
                "Boishakhi",
                "Boshonto",
                "Falguni",
                "Ullash",
                "Isha Kha",
                "Anando",
                "Hemonto",
        };
        int[] imageList = new int[12];
        for (int i = 0; i < 12; i++) {
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
        imageList[0] = R.drawable.khonika;
        for (int i = 0; i < imageList.length; i++) {
            infoBus = new InfoBus(nameList[i], imageList[i], R.drawable.location);
            dataArrayList.add(infoBus);
        }

        adapterBus = new AdapterBus(dataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapterBus);

        adapterBus.setOnItemClickListener(new AdapterBus.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), ListTimeForLocation.class);
                intent.putExtra("BUSNAME", dataArrayList.get(position).getName());
                startActivity(intent);
            }
        });

        return view;
    }
}
