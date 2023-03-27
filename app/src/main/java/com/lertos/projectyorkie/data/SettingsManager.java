package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.data.file.FileSettingsKeys;

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
        DataManager.getInstance().getFiles().getSettingsFile().setValue(FileSettingsKeys.SETTING_EFFECT_VOLUME, trackEffectVolume);
    }

    public float getTrackSongVolume() {
        return trackSongVolume;
    }

    public void setTrackSongVolume(float trackSongVolume) {
        this.trackSongVolume = trackSongVolume;
        DataManager.getInstance().getFiles().getSettingsFile().setValue(FileSettingsKeys.SETTING_MUSIC_VOLUME, trackSongVolume);
    }

    public boolean isShowAppearAnimationsInTournament() {
        return showAppearAnimationsInTournament;
    }

    public void setShowAppearAnimationsInTournament(boolean showAppearAnimationsInTournament) {
        this.showAppearAnimationsInTournament = showAppearAnimationsInTournament;
        DataManager.getInstance().getFiles().getSettingsFile().setValue(FileSettingsKeys.SETTING_SHOW_ANIMATIONS_IN_TOURNAMENT, showAppearAnimationsInTournament);
    }
}
