package com.octagon.octagondu;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private double latitude;
    private double longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_map_fragment, container, false);
        Button getLocationButton = view.findViewById(R.id.click);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String busName = "khonika";
                String busTime = "6:00";

                int regNum = 69696;
                String name = "Imran Hashmi";
                String dept = "CSE";
                int picture = R.drawable.hridoy;

                String lastLocation = "India";
                String time = "9:00";
                String date = "1 April";
                String lat = "20.5937";
                String lon = "78.9629";
                InfoBusLocation info = new InfoBusLocation(regNum, name, dept, picture, lastLocation, time, date, lat, lon);
                if(true) {
                    databaseReference.child("Location").child(busName).child(busTime).child(String.valueOf(regNum)).setValue(info);
                }
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Location");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if dataSnapshot exists and has a value
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    // Cast the value to an Integer
                    String lat = dataSnapshot.child("lat").getValue(String.class);
                    String lon = dataSnapshot.child("long").getValue(String.class);
                    // Check if the cast was successful before using the value
                    if (lat != null && lon != null) {
                        try {
                            latitude = Double.parseDouble(lat);
                            longitude = Double.parseDouble(lon);
                            onLocationDataReceived(latitude, longitude);
                        } catch (NumberFormatException e) {
                            // Handle the case where parsing fails
                        }
                    } else {
                        // Handle the case where values are missing
                    }
                } else {
                    // Handle the case where the dataSnapshot is null or doesn't exist
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity()); // Use requireActivity() to get the activity context
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment); // Corrected fragment manager call
        mapFragment.getMapAsync(this); // Set the OnMapReadyCallback to this
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        // Call updateMap here to display the initial location data
        updateMap();
    }
    private void onLocationDataReceived(double newLatitude, double newLongitude) {
        latitude = newLatitude;
        longitude = newLongitude;
        updateMap();
    }
    private void updateMap() {
        if (googleMap != null) {
            googleMap.clear(); // Clear existing markers
            if (latitude != 0 && longitude != 0) {
                LatLng location = new LatLng(latitude, longitude);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                googleMap.addMarker(new MarkerOptions().position(location).title("Sajid"));
            }
        }
    }
    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Toast.makeText(requireContext(), "Location captured: Lat=" + latitude + ", Long=" + longitude, Toast.LENGTH_SHORT).show();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("Location").child("lat").setValue(Double.toString(latitude));
                                databaseReference.child("Location").child("long").setValue(Double.toString(longitude));
                            } else {
                                Toast.makeText(requireContext(), "Location not available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
