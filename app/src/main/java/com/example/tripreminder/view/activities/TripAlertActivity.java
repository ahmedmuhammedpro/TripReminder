package com.example.tripreminder.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.tripreminder.R;
import com.example.tripreminder.services.FloatingBubbleService;
import com.example.tripreminder.utils.AudioPlayer;
import com.example.tripreminder.utils.Constants;
import com.example.tripreminder.utils.TripNotification;

public class TripAlertActivity extends AppCompatActivity {
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private AudioPlayer audioPlayer;
    Intent intent;
    String[] notes = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create AudioPlayer object
        audioPlayer = new AudioPlayer(this);

        // Get intent and it's extras
        intent = getIntent();
        String tripId = intent.getExtras().getString(Constants.TRIP_ID_KEY);
        String tripName = intent.getExtras().getString(Constants.TRIP_NAME_KEY);
        String tripStartLocationName = intent.getExtras().getString(Constants.TRIP_START_LOCATION_KEY);
        String tripEndLocationName = intent.getExtras().getString(Constants.TRIP_END_LOCATION_KEY);
         notes = intent.getStringArrayExtra(Constants.TRIP_NOTES_KEY);
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
                    //check permission overlay first
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this.getApplicationContext())) {
                        //If the draw over permission is not available open the settings screen
                        //to grant the permission.
                        Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + this.getPackageName()));
                        startActivityForResult(permissionIntent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                    } else {
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                        mapIntent.setData(Uri.parse("http://maps.google.com/maps?saddr=30.0566096,31.3213528&daddr=30.0141926,31.2791772"));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeFloatingBubble();
            } else { //Permission is not available
                Toast.makeText(this.getApplicationContext(), "Draw over other app permission not available. Closing the application", Toast.LENGTH_SHORT).show();
                // getActivity().finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
