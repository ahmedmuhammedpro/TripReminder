package com.example.tripreminder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tripreminder.model.Entities.Trip;

import java.util.List;
import java.util.Vector;

public interface MainViewModelInterface {

    LiveData<List<Trip>> getAllTrips(String userId);

    MutableLiveData<Trip> updateTripStatus(String tripId, int status);

    MutableLiveData<Trip> deleteTrip(String tripID);

    MutableLiveData<Trip> updateTrip(Trip trip);

    MutableLiveData<Vector<String>> getTripNotes(String tripId);

    LiveData<List<Trip>> getpastTrips(String userId);

    void setBottomNavigationSelectedIcon();
}
