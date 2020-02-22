package com.example.tripreminder.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tripreminder.model.Trip;

import java.util.List;

public class MainViewModel extends ViewModel implements MainViewModelInterface {

    @Override
    public LiveData<List<Trip>> getAllTrips() {
        return new MutableLiveData<>();
    }

    @Override
    public void deleteTrip(Trip trip) {

    }

    @Override
    public void updateTrip(Trip trip) {

    }
}
