package com.example.tripreminder.viewmodel.workmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.Entities.TripLocation;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.utils.TripNotification;
import com.example.tripreminder.view.activities.TripAlertActivity;

import java.util.Arrays;
import java.util.Vector;

public class TripWorker extends Worker {

    private Context context;

    public TripWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("ahmed", "entered");

        // Getting bound data with work manager request
        Data data = getInputData();

        String tripId = data.getString(Constants.TRIP_ID_KEY);
        String tripName = data.getString(Constants.TRIP_NAME_KEY);
        int tripStatus = data.getInt(Constants.TRIP_STATUS, 1);

        // Start location
        String startLocationName = data.getString(Constants.TRIP_START_NAME);
        double startLat = data.getDouble(Constants.TRIP_START_LAT_KEY, 0);
        double startLon = data.getDouble(Constants.TRIP_START_LON_KEY, 0);
        TripLocation startLocation = new TripLocation(startLat, startLon, startLocationName);

        // End location
        String endLocationName = data.getString(Constants.TRIP_END_NAME);
        double endLat = data.getDouble(Constants.TRIP_END_LAT_KEY, 0);
        double endLon = data.getDouble(Constants.TRIP_END_LON_KEY, 0);
        TripLocation endLocation = new TripLocation(endLat, endLon, endLocationName);

        // Notes
        Vector<String> notes;
        String[] notesArray = data.getStringArray(Constants.TRIP_NOTES_KEY);
        if (notesArray != null) {
            notes = new Vector<>(Arrays.asList(notesArray));
        } else {
            notes = new Vector<>();
        }

        String tripDate = data.getString(Constants.TRIP_DATE_KEY);
        int tripType = data.getInt(Constants.TRIP_TYPE, 1);

        Trip trip = new Trip(tripId, tripStatus, tripName, startLocation,
                endLocation, notes, tripDate, tripType);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            TripNotification tripNotification = new TripNotification(context, trip);
            tripNotification.sendNotification();
        } else {
            Intent intent = new Intent(context, TripAlertActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constants.TRIP_OB_KEY, trip);
            context.startActivity(intent);
        }

        return Result.success();
    }

}
