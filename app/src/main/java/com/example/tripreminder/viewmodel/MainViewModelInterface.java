package com.example.tripreminder.viewmodel;

import androidx.lifecycle.LiveData;

import com.example.tripreminder.model.Entities.Trip;

import java.util.List;

public interface MainViewModelInterface {

    LiveData<List<Trip>> getAllTrips(String userId);
    void deleteTrip(Trip trip);
    void updateTrip(Trip trip);

}
