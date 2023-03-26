package com.lertos.projectyorkie.data.file;

import android.content.Context;
import android.util.Log;

public class FileManager {

    private FileSettings fileSettings;

    public FileManager(Context context) {
        fileSettings = new FileSettings(context);

        Log.d("===KEYS", fileSettings.getListOfDataKeys().toString());
        Log.d("===VALUE of SETTING_EFFECT_VOLUME", fileSettings.getValueOfKey(FileSettingsKeys.SETTING_EFFECT_VOLUME));

    }
}
