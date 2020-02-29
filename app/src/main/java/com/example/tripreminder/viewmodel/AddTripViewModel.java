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

    /*private void addTripToWorkManager(Trip trip) {
        long milliseconds = getDifferenceMilliseconds(trip.getTripDate());
        Data.Builder dataBuilder = new Data.Builder();
        dataBuilder.putString(Constants.TRIP_ID_KEY, trip.getTripId());
        dataBuilder.putString(Constants.TRIP_NAME_KEY, trip.getTripName());
        dataBuilder.putString(Constants.TRIP_START_LOCATION_KEY, trip.getStartLocation().getLocationName());
        dataBuilder.putString(Constants.TRIP_END_LOCATION_KEY, trip.getEndLocation().getLocationName());

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(TripWorker.class)
                .setInputData(dataBuilder.build())
                .setInitialDelay(milliseconds, TimeUnit.MILLISECONDS)
                .build();

        workManager.enqueueUniqueWork(trip.getTripId(), ExistingWorkPolicy.APPEND, request);
    }

    private long getDifferenceMilliseconds(String tripDate) {
        String[] strings = tripDate.split("-");
        int day = Integer.parseInt(strings[0]);
        int month = Integer.parseInt(strings[1]) - 1;
        int year = Integer.parseInt(strings[2]);
        int hour = Integer.parseInt(strings[3]);
        int minute = Integer.parseInt(strings[4]);
        int seconds = (int) ((System.currentTimeMillis() / 1000) % 60);

        GregorianCalendar c1 = new GregorianCalendar(year, month, day, hour, minute, seconds);
        GregorianCalendar c2 = new GregorianCalendar();

        return c1.getTimeInMillis() - c2.getTimeInMillis();
    }*/


}
