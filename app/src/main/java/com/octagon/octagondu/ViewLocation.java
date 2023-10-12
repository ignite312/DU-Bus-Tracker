package com.octagon.octagondu;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewLocation extends FragmentActivity {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);

        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                mMap = googleMap;

                // Retrieve latitude and longitude from the intent
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    double latitude = extras.getDouble("latitude");
                    double longitude = extras.getDouble("longitude");

                    // Create a LatLng object with the coordinates
                    LatLng location = new LatLng(latitude, longitude);

                    // Add a marker for the location
                    mMap.addMarker(new MarkerOptions().position(location).title("Location Marker"));

                    // Move the camera to the location
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                }
            });
        }
    }
}
