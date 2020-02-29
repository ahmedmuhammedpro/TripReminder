package com.example.tripreminder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.repositories.TripRepositoryImp;

import java.util.List;
import java.util.Vector;

public class MainViewModel extends ViewModel implements MainViewModelInterface {

    TripRepositoryImp tripRepositoryImp = new TripRepositoryImp();

    @Override
    public LiveData<List<Trip>> getAllTrips(String userId) {
         return tripRepositoryImp.getUserTrips(userId);
    }

    @Override
    public void deleteTrip(Trip trip) {

    }

    @Override
    public void updateTrip(Trip trip) {

    }
    @Override
    public MutableLiveData<Vector<String>> getTripNotes(String tripId){
        return tripRepositoryImp.getTripNotes(tripId);
    }
}
