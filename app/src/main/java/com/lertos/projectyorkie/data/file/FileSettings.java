package com.lertos.projectyorkie.data.file;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

import kotlin.Triple;


public class FileSettings extends DataFile {

    private ArrayList<Triple> defaultKeys = new ArrayList<>(Arrays.asList(
            new Triple(FileSettingsKeys.SETTING_EFFECT_VOLUME, float.class, "0.5"),
            new Triple(FileSettingsKeys.SETTING_MUSIC_VOLUME, float.class, "0.25"),
            new Triple(FileSettingsKeys.SETTING_SHOW_ANIMATIONS_IN_TOURNAMENT, boolean.class, "true")
    ));

    public FileSettings(Context context) {
        super("settings", context);

        setListOfDefaultKeys();
        setupFile();
    }

    @Override
    protected void setListOfDefaultKeys() {
        listOfDefaultKeys = (ArrayList<Triple>) defaultKeys.clone();
    }
}
