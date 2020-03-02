package com.example.tripreminder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.repositories.TripRepositoryImp;

import java.util.List;
import java.util.Vector;

public class PastTripsViewModel extends ViewModel {

    TripRepositoryImp tripRepositoryImp = TripRepositoryImp.getInstance();
    public LiveData<List<Trip>> getPastTrips(String userId){
        return  tripRepositoryImp.getPastTrips(userId);
    }

    public MutableLiveData<Vector<String>> getTripNotes(String tripId){
        return tripRepositoryImp.getTripNotes(tripId);
    }
}
