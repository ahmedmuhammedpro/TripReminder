package com.example.tripreminder.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import android.util.Log;

import com.example.tripreminder.R;
import com.example.tripreminder.services.FloatingBubbleService;
import com.example.tripreminder.model.Entities.Trip;
import com.example.tripreminder.utils.AudioPlayer;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.utils.TripNotification;
import com.example.tripreminder.viewmodel.MainViewModel;

public class TripAlertActivity extends AppCompatActivity {
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private AudioPlayer audioPlayer;
    private MainViewModel mainViewModel;
    Intent intent;
    String[] notes = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create AudioPlayer object
        audioPlayer = new AudioPlayer(this);

        // Get intent and it's extras
        intent = getIntent();

        Trip trip = (Trip) intent.getExtras().getSerializable(Constants.TRIP_OB_KEY);
        trip.setUserID(MainActivity.userId);
        if (trip.getNotes() != null) {
            String[] notesArray = new String[trip.getNotes().size()];
            notes =  trip.getNotes().toArray(notesArray);
        }

        // View model
        mainViewModel = new MainViewModel();

        // Create TripNotification object
        TripNotification tripNotification = new TripNotification(this, trip);

        String alertDialogTitle = String
                .format(getResources().getString(R.string.trip_dialog_title), trip.getTripName());

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

                    // Update trip to be done
                    mainViewModel.updateTripStatus(trip.getTripId(), Trip.DONE);
                    tripNotification.cancelNotification();

                    //check permission overlay first
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this.getApplicationContext())) {
                        //If the draw over permission is not available open the settings screen
                        //to grant the permission.
                        Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + this.getPackageName()));
                        startActivityForResult(permissionIntent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                    } else {
                        String s = "http://maps.google.com/dir/" +
                                "&sadd=" + trip.getStartLocation().getLatitude() + "," +
                                trip.getStartLocation().getLongitude() +
                                "&daddr=" + trip.getEndLocation().getLatitude() + "," +
                                trip.getEndLocation().getLongitude();

                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
                        startActivity(mapIntent);
                        //start bubble service
                        initializeFloatingBubble();
                    }
                    alertDialog.dismiss();
                    finish();
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                getResources().getString(R.string.dialog_cancel_button), (dialog, which) -> {
                    tripNotification.cancelNotification();
                    mainViewModel.updateTripStatus(trip.getTripId(), Trip.CANCELED);
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

  //setup bubble service
    private void initializeFloatingBubble() {
        Log.i("bubble","start service ");
        Intent intentBubble = new Intent(getApplicationContext(), FloatingBubbleService.class);
        intentBubble.putExtra(Constants.TRIP_NOTES_KEY,notes);
        this.startService(intentBubble);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            if ( grantResults.length >0 &&grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                finishActivity(CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                initializeFloatingBubble();

            } else { //Permission is not available
                Toast.makeText(this.getApplicationContext(), "Draw over other app permission not available. Closing the application", Toast.LENGTH_SHORT).show();
                // getActivity().finish();
            }
        }
    }
}
