package com.example.tripreminder.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.tripreminder.R;
import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.model.Entities.TripLocation;
import com.example.tripreminder.utils.AudioPlayer;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.utils.TripNotification;

import java.util.Arrays;
import java.util.Vector;

public class TripAlertActivity extends AppCompatActivity {

    private AudioPlayer audioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create AudioPlayer object
        audioPlayer = new AudioPlayer(this);

        // Get intent and it's extras
        Intent intent = getIntent();
        String tripId = intent.getExtras().getString(Constants.TRIP_ID_KEY);
        String tripName = intent.getExtras().getString(Constants.TRIP_NAME_KEY);
        int tripStatus = intent.getExtras().getInt(Constants.TRIP_STATUS);
        String tripStartName = intent.getExtras().getString(Constants.TRIP_START_NAME);
        String tripEndName = intent.getExtras().getString(Constants.TRIP_END_NAME);
        double tripStartLatitude = intent.getExtras().getDouble(Constants.TRIP_START_LAT_KEY);
        double tripStartLongitude = intent.getExtras().getDouble(Constants.TRIP_START_LON_KEY);
        double tripEndLatitude = intent.getExtras().getDouble(Constants.TRIP_END_LAT_KEY);
        double tripEndLongitude = intent.getExtras().getDouble(Constants.TRIP_END_LON_KEY);
        String[] tripNotes = intent.getExtras().getStringArray(Constants.TRIP_NOTES_KEY);
        Vector<String> notesList = new Vector<>(Arrays.asList(tripNotes));
        String tripDate = intent.getExtras().getString(Constants.TRIP_DATE_KEY);
        int tripType = intent.getExtras().getInt(Constants.TRIP_TYPE);

        TripLocation startLocation = new TripLocation(tripStartLatitude, tripStartLongitude, tripStartName);
        TripLocation endLocation = new TripLocation(tripEndLatitude, tripEndLongitude, tripEndName);

        Trip trip = new Trip(tripId, tripStatus, tripName, startLocation,
                endLocation, notesList, tripDate, tripType);

        // Create TripNotification object
        TripNotification tripNotification = new TripNotification(this, trip);

        String alertDialogTitle = String
                .format(getResources().getString(R.string.trip_dialog_title), tripName);

        AlertDialog alertDialog = new AlertDialog.Builder(TripAlertActivity.this).create();
        alertDialog.setTitle(alertDialogTitle);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
                getResources().getString(R.string.dialog_snooze_button), (dialog, which) -> {
                    tripNotification.sendNotification();
                    alertDialog.dismiss();
                    finish();
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                getResources().getString(R.string.dialog_start_button), (dialog, which) -> {
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                    mapIntent.setData(Uri.parse("http://maps.google.com/maps?" +
                            "saddr=" + tripStartLatitude + "," + tripStartLongitude +
                            "&daddr=" + tripEndLatitude + "," + tripEndLongitude));

                    startActivity(mapIntent);
                    alertDialog.dismiss();
                    finish();
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                getResources().getString(R.string.dialog_cancel_button), (dialog, which) -> {
                    tripNotification.cancelNotification();
                    alertDialog.dismiss();
                    finish();
                });

        alertDialog.setCancelable(false);
        alertDialog.show();

        // Play audio
        audioPlayer.playAudio();

    }

    @Override
    protected void onResume() {
        super.onResume();
        audioPlayer.resumeAudio();
    }

    @Override
    protected void onStop() {
        super.onStop();
        audioPlayer.pauseAudio();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioPlayer.releaseMediaPlayer();
    }
}
