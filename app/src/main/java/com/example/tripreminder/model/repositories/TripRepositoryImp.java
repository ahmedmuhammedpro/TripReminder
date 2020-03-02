package com.example.tripreminder.model.repositories;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.Firestore.TripFirestoreHandler;
import com.example.tripreminder.utils.LocationPermissions;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Vector;


public class TripRepositoryImp {

    private static TripRepositoryImp tripRepositoryImp =null;
    private TripRepositoryImp(){
    }

    public static TripRepositoryImp getInstance(){
        if(tripRepositoryImp == null)
            tripRepositoryImp = new TripRepositoryImp();
        return tripRepositoryImp;
    }

    TripFirestoreHandler tripFirestoreHandler = TripFirestoreHandler.getInstance();

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
    public MutableLiveData<Trip> updateTripStatus(String tripId,int tripStatus){
        return tripFirestoreHandler.updateTripStatus(tripId,tripStatus);
    }
    public LiveData<List<Trip>> getPastTrips(String userId){
        return tripFirestoreHandler.getPastTrips(userId);
    }
}
