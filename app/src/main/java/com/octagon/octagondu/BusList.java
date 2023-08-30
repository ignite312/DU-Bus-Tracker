package com.octagon.octagondu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BusList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BusAdapter busAdapter;
    private List<String> busNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);
        busNames = new ArrayList<>();
        busNames.add("Khonika");
        busNames.add("Falguni");
        busNames.add("Choitaly");
        busNames.add("Ullash");
        busNames.add("Bosonto");
        busNames.add("Isha Kha");
        busNames.add("Anando");
        busNames.add("Torongo");
        busNames.add("Hemonto");
        busNames.add("Boshonto");
        busNames.add("Boishakhi");
        busNames.add("Kinchit");
        busNames.add("Srabon");
        busNames.add("Moitree");
        busNames.add("Wari");

        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sort the list alphabetically by name
        Collections.sort(busNames, new Comparator<String>() {
            @Override
            public int compare(String name1, String name2) {
                return name1.compareToIgnoreCase(name2);
            }
        });

        // Add index before bus names
        List<String> busNamesWithIndex = new ArrayList<>();
        for (int i = 0; i < busNames.size(); i++) {
            String busNameWithIndex = (i + 1) + ". " + busNames.get(i);
            busNamesWithIndex.add(busNameWithIndex);
        }

        // Pass the Context and List to the BusAdapter constructor
        busAdapter = new BusAdapter(this, busNamesWithIndex);
        recyclerView.setAdapter(busAdapter);
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}