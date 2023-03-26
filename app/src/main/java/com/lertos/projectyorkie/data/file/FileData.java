package com.lertos.projectyorkie.data.file;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

import kotlin.Triple;


public class FileData extends DataFile {

    private ArrayList<Triple> defaultKeys = new ArrayList<>(Arrays.asList(
            new Triple(FileDataKeys.DATA_HAS_PLAYED_BEFORE, boolean.class, "false"),
            new Triple(FileDataKeys.DATA_LAST_TIME_ON, String.class, ""),
            new Triple(FileDataKeys.DATA_SHOW_TUT_HOME, boolean.class, "true"),
            new Triple(FileDataKeys.DATA_SHOW_TUT_ACTIVITY, boolean.class, "true"),
            new Triple(FileDataKeys.DATA_SHOW_TUT_PETTING, boolean.class, "true"),
            new Triple(FileDataKeys.DATA_SHOW_TUT_TOURNAMENT, boolean.class, "true"),
            new Triple(FileDataKeys.DATA_SHOW_TUT_CATCH_DOG_TREATS, boolean.class, "true"),
            new Triple(FileDataKeys.DATA_SHOW_TUT_DODGE_THE_CATS, boolean.class, "true"),
            new Triple(FileDataKeys.DATA_SHOW_TUT_TREAT_TOSS, boolean.class, "true"),
            new Triple(FileDataKeys.DATA_SHOW_TUT_WHACK_THE_CAT, boolean.class, "true"),
            new Triple(FileDataKeys.DATA_HIGH_SCORE_THRESHOLD, int.class, "1"),
            new Triple(FileDataKeys.DATA_HIGH_SCORE_SQUARE, int.class, "0"),
            new Triple(FileDataKeys.DATA_CURRENT_RANK_NAME, String.class, "Wood"),
            new Triple(FileDataKeys.DATA_CURRENT_RANK_TIER, int.class, "5"),
            new Triple(FileDataKeys.DATA_CURRENT_HEARTS, double.class, "100"),
            new Triple(FileDataKeys.DATA_CURRENT_HEART_TOKENS, double.class, "10"),
            new Triple(FileDataKeys.DATA_PACK_DOGS_UNLOCKED, String.class, ""),
            new Triple(FileDataKeys.DATA_TALENT_LEVELS, String.class, ""),
            new Triple(FileDataKeys.DATA_ACTIVITY_LEVELS, String.class, "")
    ));

    public FileData(Context context) {
        super("playerData", context);

        setListOfDataKeys();
        setupFile();
    }

    @Override
    protected void setListOfDataKeys() {
        listOfDataKeys = (ArrayList<Triple>) defaultKeys.clone();
    }
}
