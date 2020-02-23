package com.example.tripreminder.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.repositories.TripRepositoryImp;

public class BottomSheetViewModel extends ViewModel {

    TripRepositoryImp tripRepositoryImp = new TripRepositoryImp();

    public MutableLiveData<Trip> deleteTrip(String tripID){
        return tripRepositoryImp.deleteTrip(tripID);
    }
}
