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

    private Context context;

    public TripWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        // Getting bound data with work manager request
        Data data = getInputData();
        Log.i("ahmed", "entered");
        Intent intent = new Intent(context, TripAlertActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.TRIP_ID_KEY, data.getString(Constants.TRIP_ID_KEY));
        intent.putExtra(Constants.TRIP_NAME_KEY, data.getString(Constants.TRIP_NAME_KEY));
        intent.putExtra(Constants.TRIP_STATUS, data.getInt(Constants.TRIP_STATUS, 1));
        intent.putExtra(Constants.TRIP_START_NAME, data.getString(Constants.TRIP_START_NAME));
        intent.putExtra(Constants.TRIP_END_NAME, data.getString(Constants.TRIP_END_NAME));
        intent.putExtra(Constants.TRIP_START_LAT_KEY, data.getDouble(Constants.TRIP_START_LAT_KEY, 0));
        intent.putExtra(Constants.TRIP_START_LON_KEY, data.getDouble(Constants.TRIP_START_LON_KEY, 0));
        intent.putExtra(Constants.TRIP_END_LAT_KEY, data.getDouble(Constants.TRIP_END_LAT_KEY, 0));
        intent.putExtra(Constants.TRIP_END_LON_KEY, data.getDouble(Constants.TRIP_END_LON_KEY, 0));
        intent.putExtra(Constants.TRIP_NOTES_KEY, data.getStringArray(Constants.TRIP_NOTES_KEY));
        intent.putExtra(Constants.TRIP_DATE_KEY, data.getString(Constants.TRIP_DATE_KEY));
        intent.putExtra(Constants.TRIP_TYPE, data.getInt(Constants.TRIP_TYPE, 1));

        Log.i("ahmed", "entered");
        context.startActivity(intent);
        return Result.success();
    }

}
