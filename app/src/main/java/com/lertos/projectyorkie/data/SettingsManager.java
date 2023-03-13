package com.lertos.projectyorkie.data;

public class SettingsManager {

    private float trackEffectVolume;
    private float trackSongVolume;
    private boolean showAppearAnimationsInTournament;

    public SettingsManager(float trackEffectVolume, float trackSongVolume, boolean showAppearAnimationsInTournament) {
        this.trackEffectVolume = trackEffectVolume;
        this.trackSongVolume = trackSongVolume;
        this.showAppearAnimationsInTournament = showAppearAnimationsInTournament;
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
