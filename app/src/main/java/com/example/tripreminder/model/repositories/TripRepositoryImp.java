package com.example.tripreminder.model.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.Firestore.TripFirestoreHandler;

import java.util.List;


public class TripRepositoryImp {
    TripFirestoreHandler tripFirestoreHandler = new TripFirestoreHandler();

    public MutableLiveData<Trip> addTrip(Trip trip){
        return tripFirestoreHandler.addTrip(trip);
    }
    public MutableLiveData<Trip> deleteTrip(String tripID){
        return tripFirestoreHandler.deleteTrip(tripID);
    }

    public LiveData<List<Trip>> getUserTrips(String userId){
        return tripFirestoreHandler.getUserTrips(userId);
    }
}
