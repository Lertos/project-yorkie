package com.lertos.projectyorkie.data.file;

import android.content.Context;
import android.util.Log;

public class FileManager {

    private FileSettings fileSettings;
    private FilePlayer filePlayer;

    public FileManager(Context context) {
        fileSettings = new FileSettings(context);
        filePlayer = new FilePlayer(context);

        Log.d("===DATA KEYS", filePlayer.getListOfDataKeys().toString());
        boolean hasPlayedBefore = filePlayer.getBoolean(FilePlayerKeys.DATA_HAS_PLAYED_BEFORE);
        Log.d("===VALUE of DATA_HAS_PLAYED_BEFORE", String.valueOf(hasPlayedBefore));
        boolean showTutActivity = filePlayer.getBoolean(FilePlayerKeys.DATA_SHOW_TUT_ACTIVITY);
        Log.d("===VALUE of DATA_SHOW_TUT_ACTIVITY", String.valueOf(showTutActivity));
    }

    public FileSettings getSettingsFile() {
        return fileSettings;
    }

    public FilePlayer getDataFile() {
        return filePlayer;
    }

    public void saveFiles() {
        fileSettings.saveValues();
        filePlayer.saveValues();
    }
}
