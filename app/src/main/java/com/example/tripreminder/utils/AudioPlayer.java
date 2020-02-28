package com.example.tripreminder.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

public class AudioPlayer {

    private Context context;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    // Implement what we need when audio focus change
    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener =
            focusChange -> {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        mediaPlayer.pause();
                        break;
                    case AudioManager.AUDIOFOCUS_GAIN:
                        mediaPlayer.start();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        releaseMediaPlayer();
                        break;
                }
            };
    // Implement what we need when audio complete
    private MediaPlayer.OnCompletionListener onCompletionListener = mp -> {
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
    };

    /**
     * Create {@link AudioPlayer} object
     *
     * @param context the context of the owner of this object
     */
    public AudioPlayer(Context context) {
        this.context = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * Play The default ringtone
     */
    public void playAudio() {
        int requestResult = audioManager.requestAudioFocus(
                onAudioFocusChangeListener, AudioManager.STREAM_ALARM,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        if (requestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Uri defaultRingtoneUri = RingtoneManager
                    .getActualDefaultRingtoneUri(context,
                            RingtoneManager.TYPE_RINGTONE);
            mediaPlayer = MediaPlayer.create(context, defaultRingtoneUri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(onCompletionListener);
        }
    }

    /**
     * Resume playing audio
     */
    public void resumeAudio() {
        mediaPlayer.start();
    }

    /**
     * Pause Playing audio
     */
    public void pauseAudio() {
        mediaPlayer.pause();
    }

    /**
     * Release media player
     */
    public void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }
}
