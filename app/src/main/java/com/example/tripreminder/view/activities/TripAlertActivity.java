package com.example.tripreminder.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.tripreminder.R;
import com.example.tripreminder.utils.AudioPlayer;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.utils.TripNotification;

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
        String tripStartLocationName = intent.getExtras().getString(Constants.TRIP_START_LOCATION_KEY);
        String tripEndLocationName = intent.getExtras().getString(Constants.TRIP_END_LOCATION_KEY);

        // Create TripNotification object
        TripNotification tripNotification = new TripNotification(this, tripId, tripName,
                tripStartLocationName, tripEndLocationName);

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
                    mapIntent.setData(Uri.parse("http://maps.google.com/maps?saddr=30.0566096,31.3213528&daddr=30.0141926,31.2791772"));
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
