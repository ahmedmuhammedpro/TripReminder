package com.example.tripreminder.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.repositories.TripRepositoryImp;

public class BottomSheetViewModel extends ViewModel {

    TripRepositoryImp tripRepositoryImp = TripRepositoryImp.getInstance();

    public MutableLiveData<Trip> deleteTrip(String tripID){
        return tripRepositoryImp.deleteTrip(tripID);
    }

    public MutableLiveData<Trip> updateTripStatus(String tripId, int status) {
        return tripRepositoryImp.updateTripStatus(tripId, status);
    }
}
