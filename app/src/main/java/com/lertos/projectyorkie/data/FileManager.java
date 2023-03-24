package com.lertos.projectyorkie.data;

import android.content.Context;

public class FileManager {

    private Context context;
    private String fileNameSettings = "settings";
    private String fileNameplayerData = "playerData";

    public FileManager(Context context) {
        this.context = context;
    }

    public void updateSettings(String key, String newValue) {

    }

    public void updatePlayerData(String key, String newValue) {

    }
}
