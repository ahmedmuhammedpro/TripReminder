package com.example.tripreminder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tripreminder.model.Trip;

public class MainViewModel extends ViewModel implements MainViewModelInterface {


    @Override
    public LiveData<Trip> getAllTrips() {
        return null;
    }

    @Override
    public void deleteTrip(Trip trip) {

    }

    @Override
    public void updateTrip(Trip trip) {

    }
}
