package com.lertos.projectyorkie.data;

import android.content.Context;
import android.media.MediaPlayer;

public class MediaManager {

    private static MediaManager instance;
    private Context context;
    private MediaPlayer trackEffect;
    private MediaPlayer trackSong;
    private float trackEffectVolume;
    private float trackSongVolume;

    private MediaManager() {
    }

    public static MediaManager getInstance() {
        if (instance == null) {
            instance = new MediaManager();
        }
        return instance;
    }

    public void start(Context context) {
        this.context = context;
        this.trackEffect = new MediaPlayer();
        this.trackSong = new MediaPlayer();

        //TODO: Have these saved somewhere in the user prefs
        this.trackEffectVolume = 0.5f;
        this.trackSongVolume = 0.0f;
    }

    private void resetEffectTrack() {
        if (trackEffect.isPlaying())
            trackEffect.stop();

        trackEffect.release();
        trackEffect = null;
    }

    private void resetSongTrack() {
        if (trackSong.isPlaying())
            trackSong.stop();

        trackSong.release();
        trackSong = null;
    }

    public void playEffectTrack(int resId) {
        if (trackEffect != null)
            resetEffectTrack();

        trackEffect = MediaPlayer.create(this.context, resId);

        trackEffect.setOnCompletionListener(player -> resetEffectTrack());

        trackEffect.setVolume(trackEffectVolume, trackEffectVolume);
        trackEffect.start();
    }

    public void playSongTrack(int resId, boolean loopSong) {
        if (trackSong != null)
            resetSongTrack();

        trackSong = MediaPlayer.create(this.context, resId);

        trackEffect.setOnCompletionListener(player -> resetSongTrack());

        trackSong.setVolume(trackSongVolume, trackSongVolume);
        trackSong.start();

        if (loopSong)
            trackSong.setLooping(true);
    }

    public void changeEffectTrackVolume(float volume) {
        trackEffectVolume = volume;
        trackEffect.setVolume(volume, volume);
    }

    public void changeSongTrackVolume(float volume) {
        trackSongVolume = volume;
        trackSong.setVolume(volume, volume);
    }
}
