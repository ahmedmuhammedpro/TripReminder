package com.example.tripreminder.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.tripreminder.R;
import com.example.tripreminder.view.activities.TripAlertActivity;

public class TripNotification {

    private Context context;
    private NotificationManager notificationManager;
    private String tripId, tripName, tripStartLocationName, tripEndLocationName;

    public TripNotification(Context context, String tripId, String tripName,
                            String tripStartLocationName, String tripEndLocationName) {
        this.context = context;
        this.tripId = tripId;
        this.tripName = tripName;
        this.tripStartLocationName = tripStartLocationName;
        this.tripEndLocationName = tripEndLocationName;
        createNotificationChannel();
    }

    public void sendNotification() {
        notificationManager.notify(Constants.NOTIFICATION_ID, getNotificationBuilder().build());
    }

    public void cancelNotification() {
        notificationManager.cancel(Constants.NOTIFICATION_ID);
    }

    private void createNotificationChannel() {
        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    Constants.NOTIFICATION_CHANNEL_ID, "Upcoming trips",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setDescription("Upcoming trips notification");
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        Intent intent = new Intent(context, TripAlertActivity.class);
        intent.putExtra(Constants.TRIP_ID_KEY, tripId);
        intent.putExtra(Constants.TRIP_NAME_KEY, tripName);
        intent.putExtra(Constants.TRIP_START_LOCATION_KEY, tripStartLocationName);
        intent.putExtra(Constants.TRIP_END_LOCATION_KEY, tripEndLocationName);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, Constants.NOTIFICATION_ID,
                intent, PendingIntent.FLAG_ONE_SHOT);

        return new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle("upcoming trip")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentText("you trip " + tripName + " is ready started!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
    }
}
