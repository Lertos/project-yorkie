package com.lertos.projectyorkie.data.file;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

import kotlin.Triple;


public class FilePlayer extends DataFile {

    private ArrayList<Triple> defaultKeys = new ArrayList<>(Arrays.asList(
            new Triple(FilePlayerKeys.DATA_HAS_PLAYED_BEFORE, boolean.class, "true"),
            new Triple(FilePlayerKeys.DATA_LAST_TIME_ON, String.class, ""),
            new Triple(FilePlayerKeys.DATA_HIGH_SCORE_THRESHOLD, int.class, "1"),
            new Triple(FilePlayerKeys.DATA_HIGH_SCORE_SQUARE, int.class, "0"),
            new Triple(FilePlayerKeys.DATA_CURRENT_RANK_NAME, String.class, "Wood"),
            new Triple(FilePlayerKeys.DATA_CURRENT_RANK_TIER, int.class, "5"),
            new Triple(FilePlayerKeys.DATA_CURRENT_HEARTS, double.class, "1000"),
            new Triple(FilePlayerKeys.DATA_CURRENT_HEART_TOKENS, double.class, "100"),
            new Triple(FilePlayerKeys.DATA_PACK_DOGS_UNLOCKED, String.class, ""),
            new Triple(FilePlayerKeys.DATA_TALENT_LEVELS, String.class, ""),
            new Triple(FilePlayerKeys.DATA_ACTIVITY_LEVELS, String.class, "")
    ));

    public FilePlayer(Context context) {
        super("playerData", context);

        setListOfDefaultKeys();
        setupFile();
    }

    @Override
    protected void setListOfDefaultKeys() {
        listOfDefaultKeys = (ArrayList<Triple>) defaultKeys.clone();
    }
}
