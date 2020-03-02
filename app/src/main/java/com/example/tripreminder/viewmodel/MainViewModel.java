package com.example.tripreminder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.repositories.TripRepositoryImp;

import java.util.List;
import java.util.Vector;

public class MainViewModel extends ViewModel implements MainViewModelInterface {

    TripRepositoryImp tripRepositoryImp = TripRepositoryImp.getInstance();

    @Override
    public LiveData<List<Trip>> getAllTrips(String userId) {
         return tripRepositoryImp.getUserTrips(userId);
    }

    @Override
    public MutableLiveData<Trip> deleteTrip(String tripID){
        return tripRepositoryImp.deleteTrip(tripID);
    }

    @Override
    public MutableLiveData<Trip> updateTrip(Trip trip) {
        return tripRepositoryImp.updateTrip(trip);
    }

    public MutableLiveData<Trip> updateTripStatus(String tripId, int status) {
        return tripRepositoryImp.updateTripStatus(tripId, status);
    }

    @Override
    public MutableLiveData<Vector<String>> getTripNotes(String tripId){
        return tripRepositoryImp.getTripNotes(tripId);
    }
    @Override
    public LiveData<List<Trip>> getpastTrips(String userId){
        return tripRepositoryImp.getPastTrips(userId);
    }
}
