//package com.octagon.octagondu;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.material.navigation.NavigationView;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//public class BusList extends AppCompatActivity {
//
//    private ListView recyclerView;
//    private BusAdapter busAdapter;
//    private List<String> busNames;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bus_list);
//        busNames = new ArrayList<>();
//        busNames.add("Khonika");
//        busNames.add("Falguni");
//        busNames.add("Choitaly");
//        busNames.add("Ullash");
//        busNames.add("Bosonto");
//        busNames.add("Isha Kha");
//        busNames.add("Anando");
//        busNames.add("Torongo");
//        busNames.add("Hemonto");
//        busNames.add("Boshonto");
//        busNames.add("Boishakhi");
//        busNames.add("Kinchit");
//        busNames.add("Srabon");
//        busNames.add("Moitree");
//        busNames.add("Wari");
//
//        recyclerView = findViewById(R.id.recyclerView2);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Sort the list alphabetically by name
//        Collections.sort(busNames, new Comparator<String>() {
//            @Override
//            public int compare(String name1, String name2) {
//                return name1.compareToIgnoreCase(name2);
//            }
//        });
//
//        // Add index before bus names
//        List<String> busNamesWithIndex = new ArrayList<>();
//        for (int i = 0; i < busNames.size(); i++) {
//            String busNameWithIndex = (i + 1) + ". " + busNames.get(i);
//            busNamesWithIndex.add(busNameWithIndex);
//        }
//
//        // Pass the Context and List to the BusAdapter constructor
//        busAdapter = new BusAdapter(this, busNamesWithIndex);
//        recyclerView.setAdapter(busAdapter);
//    }
//    private void showToast(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//}

package com.octagon.octagondu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class BusList extends AppCompatActivity {

    ListView listView;
    ListAdapter listAdapter;
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    ListData listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);

        listView = findViewById(R.id.listview);
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
            }else if(i % 5 == 4) {
                imageList[i] = R.drawable.bus5;
            }
        }

        for (int i = 0; i < imageList.length; i++) {
            listData = new ListData(nameList[i], imageList[i]);
            dataArrayList.add(listData);
        }

        listAdapter = new ListAdapter(BusList.this, dataArrayList);
        listView.setAdapter(listAdapter);
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), BusDetailsActivity.class);
                intent.putExtra("busName", nameList[i]);
                startActivity(intent);
            }
        });
    }
}
