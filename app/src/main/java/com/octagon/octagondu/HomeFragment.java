package com.octagon.octagondu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);

//        // Initialize Retrofit
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://maps.googleapis.com/maps/api/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//// Create an instance of the service interface
//        PlacesApiService service = retrofit.create(PlacesApiService.class);
//
//// Make the nearby search request
//        Call<PlaceResponse> call = service.getNearbyPlaces("23.8332, 90.3829", 1000, "", "AIzaSyCiZDfdN01zCLURmF-APohotLgaH3W-wLA");
//
//        call.enqueue(new Callback<PlaceResponse>() {
//            @Override
//            public void onResponse(Call<PlaceResponse> call, Response<PlaceResponse> response) {
//                if (response.isSuccessful()) {
//                    PlaceResponse placeResponse = response.body();
//                    List<Place> places = placeResponse.getResults();
//                    if (!places.isEmpty()) {
//                        // You can access the nearest place name here
//                        String nearestPlaceName = places.get(0).getName();
//                        Toast.makeText(requireContext(), "Nearest Place: " + nearestPlaceName, Toast.LENGTH_SHORT).show();
//                    } else {
//                        // No places found
//                    }
//                } else {
//                    // Handle API request failure
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PlaceResponse> call, Throwable t) {
//                // Handle network or other errors
//            }
//        });

        return view;
    }
}