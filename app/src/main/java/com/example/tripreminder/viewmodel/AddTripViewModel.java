package com.example.tripreminder.viewmodel;

import android.content.Context;

import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.repositories.TripRepositoryImp;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.viewmodel.workmanager.TripWorker;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class AddTripViewModel extends ViewModel {
    TripRepositoryImp tripRepositoryImp = new TripRepositoryImp();
    private WorkManager workManager;
    private Context context;

    public AddTripViewModel(){

    }

    public void setContext(Context context) {
        this.context = context;
        workManager = WorkManager.getInstance(context);
    }

    public MutableLiveData<Trip> addTrip(Trip trip){
       // addTripToWorkManager(trip);
        return tripRepositoryImp.addTrip(trip);
    }

    private void addTripToWorkManager(Trip trip) {
        Data.Builder dataBuilder = new Data.Builder();
        dataBuilder.putString(Constants.TRIP_ID_KEY, trip.getTripId());
        dataBuilder.putString(Constants.TRIP_NAME_KEY, trip.getTripName());
        dataBuilder.putString(Constants.TRIP_START_LOCATION_KEY, trip.getStartLocation().getLocationName());
        dataBuilder.putString(Constants.TRIP_END_LOCATION_KEY, trip.getEndLocation().getLocationName());

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(TripWorker.class)
                .setInputData(dataBuilder.build())
                .setInitialDelay(5, TimeUnit.SECONDS)
                .build();

        workManager.enqueue(request);
    }
}
