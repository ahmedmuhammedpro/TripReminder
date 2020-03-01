package com.example.tripreminder.viewmodel.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.utils.Constants;

import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class WorkManagerViewModel {

    private Context context;
    private WorkManager workManager;

    public WorkManagerViewModel(Context context) {
        this.context = context;
        workManager = WorkManager.getInstance(context);
    }

    public void addTripToWorkManager(Trip trip) {
        long milliseconds = getDifferenceMilliseconds(trip.getTripDate());

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(TripWorker.class)
                .setInputData(getDataBuilder(trip).build())
                .setInitialDelay(10000, TimeUnit.MILLISECONDS)
                .build();

        workManager.enqueueUniqueWork(trip.getTripId(), ExistingWorkPolicy.APPEND, request);
    }

    public void editRequest(Trip trip) {
        long milliseconds = getDifferenceMilliseconds(trip.getTripDate());

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(TripWorker.class)
                .setInputData(getDataBuilder(trip).build())
                .setInitialDelay(milliseconds, TimeUnit.MILLISECONDS)
                .build();

        workManager.enqueueUniqueWork(trip.getTripId(), ExistingWorkPolicy.REPLACE, request);
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
    }

    private Data.Builder getDataBuilder(Trip trip) {
        Data.Builder dataBuilder = new Data.Builder();

        dataBuilder.putString(Constants.TRIP_ID_KEY, trip.getTripId());
        dataBuilder.putString(Constants.TRIP_NAME_KEY, trip.getTripName());
        dataBuilder.putInt(Constants.TRIP_STATUS, trip.getTripStatus());
        dataBuilder.putString(Constants.TRIP_START_NAME, trip.getStartLocation().getLocationName());
        dataBuilder.putString(Constants.TRIP_END_NAME, trip.getEndLocation().getLocationName());
        dataBuilder.putDouble(Constants.TRIP_START_LAT_KEY, trip.getStartLocation().getLatitude());
        dataBuilder.putDouble(Constants.TRIP_START_LON_KEY, trip.getStartLocation().getLongitude());
        dataBuilder.putDouble(Constants.TRIP_END_LAT_KEY, trip.getEndLocation().getLatitude());
        dataBuilder.putDouble(Constants.TRIP_END_LON_KEY, trip.getEndLocation().getLongitude());

        if (trip.getNotes() != null) {
            String[] notesArray = new String[trip.getNotes().size()];
            trip.getNotes().toArray(notesArray);
            dataBuilder.putStringArray(Constants.TRIP_NOTES_KEY, notesArray);
        } else {
            dataBuilder.putStringArray(Constants.TRIP_NOTES_KEY, new String[]{});
        }

        dataBuilder.putString(Constants.TRIP_DATE_KEY, trip.getTripDate());
        dataBuilder.putInt(Constants.TRIP_TYPE, trip.getTripType());

        return dataBuilder;
    }
}
