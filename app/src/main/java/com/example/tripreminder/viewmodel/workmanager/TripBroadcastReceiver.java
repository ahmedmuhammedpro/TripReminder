package com.example.tripreminder.viewmodel.workmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.view.activities.TripAlertActivity;

public class TripBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!TextUtils.isEmpty(action) && action.equals(Constants.TRIP_ALERT_ACTION)) {
            String tripName = intent.getExtras().getString(Constants.TRIP_NAME_KEY);
            Intent tripAlertIntent = new Intent(context, TripAlertActivity.class);
            tripAlertIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            tripAlertIntent.putExtra(Constants.TRIP_NAME_KEY, tripName);
            context.startActivity(tripAlertIntent);
        }
    }
}
