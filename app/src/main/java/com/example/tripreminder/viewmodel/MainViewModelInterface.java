package com.example.tripreminder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tripreminder.model.Entities.Trip;

import java.util.List;
import java.util.Vector;

public interface MainViewModelInterface {

    LiveData<List<Trip>> getAllTrips(String userId);

    void deleteTrip(Trip trip);

    void updateTrip(Trip trip);

    public MutableLiveData<Vector<String>> getTripNotes(String tripId);
}
