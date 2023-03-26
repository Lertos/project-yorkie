package com.lertos.projectyorkie.data.file;

import android.content.Context;
import android.util.Log;

public class FileManager {

    private FileSettings fileSettings;
    private FileData fileData;

    public FileManager(Context context) {
        fileSettings = new FileSettings(context);
        fileData = new FileData(context);

        Log.d("===SETTINGS KEYS", fileSettings.getListOfDataKeys().toString());
        Log.d("===VALUE of SETTING_EFFECT_VOLUME", fileSettings.getValueOfKey(FileSettingsKeys.SETTING_EFFECT_VOLUME));

        Log.d("===DATA KEYS", fileData.getListOfDataKeys().toString());
        boolean hasPlayedBefore = fileData.getBoolean(FileDataKeys.DATA_HAS_PLAYED_BEFORE);
        Log.d("===VALUE of DATA_HAS_PLAYED_BEFORE", String.valueOf(hasPlayedBefore));
        boolean showTutActivity = fileData.getBoolean(FileDataKeys.DATA_SHOW_TUT_ACTIVITY);
        Log.d("===VALUE of DATA_SHOW_TUT_ACTIVITY", String.valueOf(showTutActivity));
    }

    public FileSettings getSettingsFile() {
        return fileSettings;
    }

    public FileData getDataFile() {
        return fileData;
    }
}
