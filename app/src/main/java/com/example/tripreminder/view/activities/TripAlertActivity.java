package com.example.tripreminder.view.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.example.tripreminder.R;
import com.example.tripreminder.utils.Constants;

public class TripAlertActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener =
            focusChange -> {
                if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                    mediaPlayer.start();
                } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                    releaseMediaPlayer();
                }
            };
    private MediaPlayer.OnCompletionListener onCompletionListener =
            mp -> {
                releaseMediaPlayer();
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_alert);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int audioRequestResult;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            AudioFocusRequest audioFocusRequest = new
                    AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setOnAudioFocusChangeListener(onAudioFocusChangeListener)
                    .build();
            audioRequestResult = audioManager.requestAudioFocus(audioFocusRequest);
        } else {
            audioRequestResult = audioManager.requestAudioFocus(
                    onAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
        }

        Intent intent = getIntent();
        String tripName = intent.getExtras().getString(Constants.TRIP_NAME_KEY);
        String alertDialogTitle = String
                .format(getResources().getString(R.string.trip_dialog_title), tripName);

        TextView title = findViewById(R.id.trip_dialog_title);
        title.setText(alertDialogTitle);

        if (audioRequestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Uri defaultRingtoneUri = RingtoneManager
                    .getActualDefaultRingtoneUri(getApplicationContext(),
                                                 RingtoneManager.TYPE_RINGTONE);

            mediaPlayer = MediaPlayer.create(this, defaultRingtoneUri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(onCompletionListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }
}
