package com.example.tripreminder.viewmodel;

import android.content.Context;
import android.util.Log;

import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.repositories.TripRepositoryImp;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.viewmodel.workmanager.TripWorker;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class AddTripViewModel extends ViewModel {
    TripRepositoryImp tripRepositoryImp = new TripRepositoryImp();
    private WorkManager workManager;
    private Context context;

    public AddTripViewModel() {

    }

    public void setContext(Context context) {
        this.context = context;
        workManager = WorkManager.getInstance(context);
    }

    public MutableLiveData<Trip> addTrip(Trip trip) {
        addTripToWorkManager(trip);
        return tripRepositoryImp.addTrip(trip);
    }

    private void addTripToWorkManager(Trip trip) {
        long milliseconds = getDifferenceMilliseconds(trip.getTripDate());
        Data.Builder dataBuilder = new Data.Builder();
        dataBuilder.putString(Constants.TRIP_ID_KEY, trip.getTripId());
        dataBuilder.putString(Constants.TRIP_NAME_KEY, trip.getTripName());
        dataBuilder.putString(Constants.TRIP_START_LOCATION_KEY, trip.getStartLocation().getLocationName());
        dataBuilder.putString(Constants.TRIP_END_LOCATION_KEY, trip.getEndLocation().getLocationName());
        if(trip.getNotes().size() >0) {
            Object[] objects = trip.getNotes().toArray();
            String[] notes = Arrays.copyOf(objects,
                    objects.length,
                    String[].class);
            dataBuilder.putStringArray(Constants.TRIP_NOTES_KEY, notes);
        }
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(TripWorker.class)
                .setInputData(dataBuilder.build())
                .setInitialDelay(milliseconds, TimeUnit.MILLISECONDS)
                .build();

        workManager.enqueue(request);
    }

    private long getDifferenceMilliseconds(String tripDate) {
        String[] strings = tripDate.split("-");
        int day = Integer.parseInt(strings[0]);
        int month = Integer.parseInt(strings[1]) - 1;
        int year = Integer.parseInt(strings[2]);
        int hour = Integer.parseInt(strings[3]);
        int minute = Integer.parseInt(strings[4]);
        long seconds = (System.currentTimeMillis() / 1000) % 60;

        GregorianCalendar c1 = new GregorianCalendar(year, month, day, hour, minute, (int) seconds);
        GregorianCalendar c2 = new GregorianCalendar();
        Log.i("ahmed", "did => " + (c1.getTimeInMillis() - c2.getTimeInMillis()));
        return c1.getTimeInMillis() - c2.getTimeInMillis();
    }


}
