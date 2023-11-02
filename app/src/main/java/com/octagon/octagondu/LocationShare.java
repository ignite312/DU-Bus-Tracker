package com.octagon.octagondu;

import static com.octagon.octagondu.MainActivity.DUREGNUM;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocationShare extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Button updateLocationButton;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private String busName, busID;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_share);
        busName = getIntent().getStringExtra("BUSNAME");
        busID = getIntent().getStringExtra("ID");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

         /*Toolbar Setup*/
        MaterialToolbar detailsBusToolbar = findViewById(R.id.toolbar);
        detailsBusToolbar.setTitle("Sharing Locations for "+ busName);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            Drawable blackArrow = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_24);
            actionBar.setHomeAsUpIndicator(blackArrow);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        updateLocationButton = findViewById(R.id.click);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        updateLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationUpdates();
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    updateLocationMarker(location);
                }
            }
        };
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void updateLocationMarker(Location location) {
        if (location != null) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Location").child(getCurrentDateFormatted()).child(busName).child(busID).child("Locations").child(DUREGNUM);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        databaseReference.child("lastLocation").setValue("Dhaka");
                        databaseReference.child("lat").setValue(Double.toString(latitude));
                        databaseReference.child("lon").setValue(Double.toString(longitude));
                        databaseReference.child("time").setValue(getCurrentTime24HourFormat());
                        databaseReference.child("date").setValue(getCurrentDateFormatted());
                    } else {
                        databaseReference.child("regNum").setValue(DUREGNUM);
                        databaseReference.child("lastLocation").setValue("Dhaka");
                        databaseReference.child("lat").setValue(Double.toString(latitude));
                        databaseReference.child("lon").setValue(Double.toString(longitude));
                        databaseReference.child("time").setValue(getCurrentTime24HourFormat());
                        databaseReference.child("date").setValue(getCurrentDateFormatted());
                        databaseReference.child("busName").setValue(busName);
                        databaseReference.child("busTime").setValue(busID);
                        databaseReference.child("voteCountLocations").setValue(0);
                        update(3, DUREGNUM);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("My Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        googleMap.addMarker(markerOptions);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public String getCurrentDateFormatted() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    public String getCurrentTime24HourFormat() {
        Date currentTime = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return timeFormat.format(currentTime);
    }

    private void update(int dcc, String UID) {

        DatabaseReference ContributionCountRef = FirebaseDatabase.getInstance().getReference("UserInfo/" + UID + "/contributionCount");
        ContributionCountRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer value = mutableData.getValue(Integer.class);
                if (value == null) {
                    mutableData.setValue(dcc);
                } else {
                    mutableData.setValue(value + dcc);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                } else {
                    Integer updatedValue = dataSnapshot.getValue(Integer.class);
                }
            }
        });
    }
}
