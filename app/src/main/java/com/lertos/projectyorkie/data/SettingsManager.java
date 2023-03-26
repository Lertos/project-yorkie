package com.lertos.projectyorkie.data;

public class SettingsManager {

    private float trackEffectVolume;
    private float trackSongVolume;
    private boolean showAppearAnimationsInTournament;

    public SettingsManager() {
    }

    public float getTrackEffectVolume() {
        return trackEffectVolume;
    }

    public void setTrackEffectVolume(float trackEffectVolume) {
        this.trackEffectVolume = trackEffectVolume;
    }

    public float getTrackSongVolume() {
        return trackSongVolume;
    }

    public void setTrackSongVolume(float trackSongVolume) {
        this.trackSongVolume = trackSongVolume;
    }

    public boolean isShowAppearAnimationsInTournament() {
        return showAppearAnimationsInTournament;
    }

    public void setShowAppearAnimationsInTournament(boolean showAppearAnimationsInTournament) {
        this.showAppearAnimationsInTournament = showAppearAnimationsInTournament;
    }
}
