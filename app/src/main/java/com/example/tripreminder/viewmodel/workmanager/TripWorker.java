package com.example.tripreminder.viewmodel.workmanager;

import android.content.Context;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class TripWorker extends Worker {

    public TripWorker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }
    
    @Override
    public Result doWork() {

        return Result.success();
    }

}
