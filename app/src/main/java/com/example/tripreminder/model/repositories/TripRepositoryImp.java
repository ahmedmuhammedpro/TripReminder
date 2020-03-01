package com.example.tripreminder.model.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.Firestore.TripFirestoreHandler;

import java.util.List;
import java.util.Vector;


public class TripRepositoryImp {
    TripFirestoreHandler tripFirestoreHandler = new TripFirestoreHandler();

    public MutableLiveData<Trip> addTrip(Trip trip){
        return tripFirestoreHandler.addTrip(trip);
    }
    public MutableLiveData<Trip> deleteTrip(String tripID){
        return tripFirestoreHandler.deleteTrip(tripID);
    }
    public MutableLiveData<Trip> updateTrip(Trip trip){
        return tripFirestoreHandler.updateTrip(trip);
    }
    public LiveData<List<Trip>> getUserTrips(String userId){
        return tripFirestoreHandler.getUserTrips(userId);
    }

    public MutableLiveData<Vector<String>> getTripNotes(String tripId) {
        return  tripFirestoreHandler.getTripNotes(tripId);
    }
    public MutableLiveData<Trip> updateTripStatus(String tripId, int tripStatus) {
        return tripFirestoreHandler.updateTripStatus(tripId,tripStatus);
    }
}
