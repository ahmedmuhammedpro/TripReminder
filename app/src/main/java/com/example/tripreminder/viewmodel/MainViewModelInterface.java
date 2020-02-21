package com.example.tripreminder.viewmodel;

import androidx.lifecycle.LiveData;

import com.example.tripreminder.model.Trip;

public interface MainViewModelInterface {

    LiveData<Trip> getAllTrips();
    void deleteTrip(Trip trip);
    void updateTrip(Trip trip);

}
