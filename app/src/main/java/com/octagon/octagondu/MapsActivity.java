package com.octagon.octagondu;

import android.Manifest;
import com.google.android.gms.maps.SupportMapFragment;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private double latitude;
    private double longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Button getLocationButton = findViewById(R.id.click);
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLocationUpdates();
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
//                            Toast.makeText(MapsActivity.this, "hehe1", Toast.LENGTH_SHORT).show();
                            latitude = Double.parseDouble(lat);
                            longitude = Double.parseDouble(lon);
                            onLocationDataReceived(latitude, longitude);
                        } catch (NumberFormatException e) {
                            // Handle the case when parsing fails (e.g., invalid format)
                            // You can log an error or take appropriate action here
                        }
                    } else {
                        // Handle the case where the value couldn't be cast to Integer
                    }
                } else {
                    // Handle the case where the dataSnapshot is null or doesn't exist
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
    }
    private void onLocationDataReceived(double newLatitude, double newLongitude) {
        latitude = newLatitude;
        longitude = newLongitude;
        updateMap();
    }
    private void updateMap() {
//        Toast.makeText(MapsActivity.this, Double.toString(longitude), Toast.LENGTH_SHORT).show();
        if (googleMap != null) {
            googleMap.clear(); // Clear existing markers
            if (latitude != 0 && longitude != 0) {

                LatLng location = new LatLng(latitude, longitude);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                googleMap.addMarker(new MarkerOptions().position(location).title("Sajid"));
            }
        }
    }
    public void onMapReady(GoogleMap googleMap) {
//        Toast.makeText(MapsActivity.this, "hehe2", Toast.LENGTH_SHORT).show();
        this.googleMap = googleMap;
        // Call updateMap here to display the initial location data
        updateMap();
    }
    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Toast.makeText(MapsActivity.this, "Location captured: Lat=" + latitude + ", Long=" + longitude, Toast.LENGTH_SHORT).show();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("Location").child("lat").setValue(Double.toString(latitude));
                                databaseReference.child("Location").child("long").setValue(Double.toString(longitude));
                            } else {
                                Toast.makeText(MapsActivity.this, "Location not available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
