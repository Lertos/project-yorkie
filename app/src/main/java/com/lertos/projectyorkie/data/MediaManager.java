package com.lertos.projectyorkie.data;

import android.content.Context;
import android.media.MediaPlayer;

public class MediaManager {

    private static MediaManager instance;
    private Context context;
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
        this.trackSong = new MediaPlayer();

        //Setting defaults as we'll; be setting them later from the user prefs
        this.trackEffectVolume = 0.0f;
        this.trackSongVolume = 0.0f;
    }

    public void setVolumesFromUserPrefs() {
        this.trackEffectVolume = DataManager.getInstance().getSettings().getTrackEffectVolume();
        this.trackSongVolume = DataManager.getInstance().getSettings().getTrackSongVolume();
    }

    private void resetSongTrack() {
        if (trackSong == null)
            return;

        if (trackSong.isPlaying())
            trackSong.stop();

        trackSong.release();
        trackSong = null;
    }

    public void stopSong() {
        resetSongTrack();
    }

    public void pauseSong() {
        if (trackSong != null)
            trackSong.pause();
    }

    public void startSong() {
        if (trackSong != null) {
            if (!trackSong.isPlaying())
                trackSong.start();
        }
    }

    public void playEffectTrack(int resId) {
        MediaPlayer trackEffect = MediaPlayer.create(this.context, resId);

        trackEffect.setVolume(trackEffectVolume, trackEffectVolume);

        trackEffect.setOnCompletionListener(mediaPlayer -> trackEffect.release());

        trackEffect.start();
    }

    public void playSongTrack(int resId, boolean loopSong) {
        if (trackSong != null)
            resetSongTrack();

        trackSong = MediaPlayer.create(this.context, resId);

        trackSong.setVolume(trackSongVolume, trackSongVolume);
        trackSong.start();

        if (loopSong)
            trackSong.setLooping(true);
    }

    public void changeEffectTrackVolume(float volume) {
        trackEffectVolume = volume;
        DataManager.getInstance().getSettings().setTrackEffectVolume(volume);
    }

    public void changeSongTrackVolume(float volume) {
        DataManager.getInstance().getSettings().setTrackSongVolume(volume);
        trackSongVolume = volume;
        trackSong.setVolume(volume, volume);
    }
}
