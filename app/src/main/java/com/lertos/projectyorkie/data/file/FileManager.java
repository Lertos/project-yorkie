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
        Log.d("===VALUE of DATA_CURRENT_HEARTS", fileData.getValueOfKey(FileDataKeys.DATA_CURRENT_HEARTS));
    }

    public FileSettings getSettingsFile() {
        return fileSettings;
    }

    public FileData getDataFile() {
        return fileData;
    }
}
