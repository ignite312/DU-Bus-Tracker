package com.octagon.octagondu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentHome extends Fragment {
    private ListView listView;
    private List<String> allNames;
    private ArrayAdapter<String> adapter;
    private Animation slideIn, slideOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Define the initial list of bus names
        String[] busNamesArray = {
                "Anando", "Boishakhi", "Boshonto", "Choitaly", "Falguni",
                "Hemonto", "Isha Kha", "Khonika", "Kinchit", "Srabon",
                "Torongo", "Ullash"
        };
        allNames = new ArrayList<>(Arrays.asList(busNamesArray));

        // Initialize UI components using view.findViewById
        SearchView searchView = view.findViewById(R.id.searchView);
        listView = view.findViewById(R.id.listView);

        // Set up the ListView adapter
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, allNames);
        listView.setAdapter(adapter);

        // Set animation for showing and hiding the ListView
        slideIn = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in);
        slideOut = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out);

        // Initially hide the ListView
        listView.setVisibility(View.GONE);

        // Set a click listener for the SearchView
        searchView.setOnSearchClickListener(v -> {
            // Show the ListView with animation
            listView.setVisibility(View.VISIBLE);
            listView.startAnimation(slideIn);
        });

        // Set a query listener to filter bus names based on user input
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter bus names based on the user's input
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        // Set a close listener for the SearchView
        searchView.setOnCloseListener(() -> {
            // Hide the ListView with animation when the SearchView is closed
            listView.startAnimation(slideOut);
            listView.setVisibility(View.GONE);
            return false;
        });

        // Set an item click listener for the ListView
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // Get the selected bus name from the filtered list
            String selectedBusName = (String) parent.getItemAtPosition(position);

            // Create an Intent to start the TabSchedule activity
            Intent intent = new Intent(getActivity(), TabSchedule.class);

            // Put the parameters into the Intent
            intent.putExtra("BUSNAME", selectedBusName);
            intent.putExtra("FLAG", "SC");

            // Start the activity with the Intent
            startActivity(intent);
        });


        return view;
    }
}

