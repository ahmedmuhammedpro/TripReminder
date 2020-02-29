package com.example.tripreminder.viewmodel.workmanager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.view.activities.TripAlertActivity;

public class TripWorker extends Worker {

    public TripWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Getting bound data with work manager request
        Data data = getInputData();
        String tripId = data.getString(Constants.TRIP_ID_KEY);
        String tripName = data.getString(Constants.TRIP_NAME_KEY);
        String tripStartLocationName = data.getString(Constants.TRIP_START_LOCATION_KEY);
        String tripEndLocationName = data.getString(Constants.TRIP_END_LOCATION_KEY);
        String[] tripNotes = data.getStringArray(Constants.TRIP_NOTES_KEY);
        Intent intent = new Intent(getApplicationContext(), TripAlertActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.TRIP_ID_KEY, tripId);
        intent.putExtra(Constants.TRIP_NAME_KEY, tripName);
        intent.putExtra(Constants.TRIP_START_LOCATION_KEY, tripStartLocationName);
        intent.putExtra(Constants.TRIP_END_LOCATION_KEY, tripEndLocationName);
        intent.putExtra(Constants.TRIP_NOTES_KEY,tripNotes);
        getApplicationContext().startActivity(intent);
        return Result.success();
    }

}
