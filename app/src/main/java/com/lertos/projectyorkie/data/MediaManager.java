package com.lertos.projectyorkie.data;

import android.content.Context;
import android.media.MediaPlayer;

public class MediaManager {

    private static MediaManager instance;
    private Context context;
    private MediaPlayer trackEffect;
    private MediaPlayer trackSong;

    private MediaManager() {}

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
    }

    public void playEffectTrack(int resId) {
        if (trackEffect != null)
            trackEffect.stop();

        trackEffect = MediaPlayer.create(this.context, resId);
        trackEffect.start();
    }

    public void playSongTrack(int resId, boolean loopSong) {
        if (trackSong != null)
            trackSong.stop();

        trackSong = MediaPlayer.create(this.context, resId);
        trackSong.start();

        if (loopSong)
            trackSong.setLooping(true);
    }
}
