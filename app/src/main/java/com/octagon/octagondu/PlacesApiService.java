package com.octagon.octagondu;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// PlacesApiService.java
public interface PlacesApiService {
    @GET("place/nearbysearch/json")
    Call<PlaceResponse> getNearbyPlaces(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String apiKey
    );
}
