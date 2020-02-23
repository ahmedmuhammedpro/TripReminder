package com.example.tripreminder.viewmodel.workmanager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.tripreminder.utils.Constants;

public class TripWorker extends Worker {

    public TripWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("ahmed", "entered");
        Data data = getInputData();
        String tripName = data.getString(Constants.TRIP_NAME_KEY);
        Intent intent = new Intent();
        intent.setAction(Constants.TRIP_ALERT_ACTION);
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra(Constants.TRIP_NAME_KEY, tripName);
        getApplicationContext().sendBroadcast(intent);
        return Result.success();
    }

}
