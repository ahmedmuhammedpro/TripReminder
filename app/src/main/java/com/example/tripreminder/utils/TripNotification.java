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
import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.view.activities.TripAlertActivity;

public class TripNotification {

    private Context context;
    private NotificationManager notificationManager;
    private Trip trip;

    public TripNotification(Context context, Trip trip) {
        this.context = context;
        this.trip = trip;
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
        intent.putExtra(Constants.TRIP_OB_KEY, trip);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, Constants.NOTIFICATION_ID,
                intent, PendingIntent.FLAG_ONE_SHOT);

        return new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle("upcoming trip")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentText("you trip " + trip.getTripName() + " is ready started!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
    }
}
