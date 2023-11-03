package com.octagon.octagondu;

import static com.octagon.octagondu.MainActivity.DUREGNUM;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocationShare extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Button updateLocationButton;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private String busName, busID, busTime;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_share);
        busName = getIntent().getStringExtra("BUSNAME");
        busID = getIntent().getStringExtra("ID");
        busTime = getIntent().getStringExtra("BUSTIME");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        /*Toolbar Setup*/
        MaterialToolbar detailsBusToolbar = findViewById(R.id.toolbar);
        detailsBusToolbar.setTitle("Sharing Location For " + busName + " " + busTime);
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
                showConfirmationDialog();
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
            double du_tsc_lat = 23.732663;
            double du_tsc_long = 90.395564;
            double du_cur_lat = 23.727621;
            double du_cur_long = 90.400465;
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
                        if (haversine(du_tsc_lat, du_tsc_long, latitude, longitude) <= 1.00) {
                            databaseReference.child("lastLocation").setValue("Near TSC");
                            showToast("Reached DU");
                            finish();
                        }
                        if (haversine(du_cur_lat, du_cur_long, latitude, longitude) <= 1.00) {
                            databaseReference.child("lastLocation").setValue("Near Curzon");
                            showToast("Reached DU");
                            finish();
                        }
                    } else {
                        databaseReference.child("regNum").setValue(DUREGNUM);
                        databaseReference.child("lastLocation").setValue("Dhaka");
                        databaseReference.child("lat").setValue(Double.toString(latitude));
                        databaseReference.child("lon").setValue(Double.toString(longitude));
                        databaseReference.child("time").setValue(getCurrentTime24HourFormat());
                        databaseReference.child("date").setValue(getCurrentDateFormatted());
                        databaseReference.child("busName").setValue(busName);
                        databaseReference.child("busTime").setValue(busTime);
                        databaseReference.child("voteCountLocations").setValue(0);
                        if (haversine(du_tsc_lat, du_tsc_long, latitude, longitude) <= 1.00) {
                            databaseReference.child("lastLocation").setValue("Near TSC");
                            showToast("Reached DU");
                            finish();
                        }
                        if (haversine(du_cur_lat, du_cur_long, latitude, longitude) <= 1.00) {
                            databaseReference.child("lastLocation").setValue("Near Curzon");
                            showToast("Reached DU");
                            finish();
                        }
                        //update(3, DUREGNUM);
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

    public double haversine(double lat1, double lon1, double lat2, double lon2) {
        // Convert latitude and longitude from degrees to radians
        double deg2rad = Math.PI / 180.0;
        lat1 *= deg2rad;
        lon1 *= deg2rad;
        lat2 *= deg2rad;
        lon2 *= deg2rad;

        // Radius of the Earth in kilometers
        double radius = 6371.0; // Earth's mean radius

        // Haversine formula
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;
        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.sin(dlon / 2) * Math.sin(dlon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = radius * c;

        return distance;
    }

    /*
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
     */
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you are in " + busName + " " + busTime + "?");
        builder.setMessage("Please be cautious when sharing your location. If you are not on the bus, kindly refrain from sharing your location. By sharing your location, you can help other students and contribute to our community.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startLocationUpdates();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
