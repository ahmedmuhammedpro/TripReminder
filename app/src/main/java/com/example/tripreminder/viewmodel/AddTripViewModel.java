package com.example.tripreminder.viewmodel;

import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.repositories.TripRepositoryImp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddTripViewModel extends ViewModel {
    TripRepositoryImp tripRepositoryImp = new TripRepositoryImp();

    public AddTripViewModel() {

    }

    public MutableLiveData<Trip> addTrip(Trip trip) {
        return tripRepositoryImp.addTrip(trip);
    }

}
