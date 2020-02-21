package com.example.tripreminder.viewmodel;

import androidx.lifecycle.LiveData;

import com.example.tripreminder.model.Trip;

import java.util.List;

public interface MainViewModelInterface {

    LiveData<List<Trip>> getAllTrips();
    void deleteTrip(Trip trip);
    void updateTrip(Trip trip);

}
