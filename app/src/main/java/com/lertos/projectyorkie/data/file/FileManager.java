package com.lertos.projectyorkie.data.file;

import android.content.Context;
import android.util.Log;

public class FileManager {

    private Context context;
    private final String FILE_NAME_SETTINGS = "settings";
    private final String FILE_NAME_PLAYER_DATA = "playerData";


    private final String DATA_HAS_PLAYED_BEFORE = "DATA_HAS_PLAYED_BEFORE";
    private final String DATA_LAST_TIME_ON = "DATA_LAST_TIME_ON";
    private final String DATA_SHOW_TUT_HOME = "DATA_SHOW_TUT_HOME";
    private final String DATA_SHOW_TUT_ACTIVITY = "DATA_SHOW_TUT_ACTIVITY";
    private final String DATA_SHOW_TUT_PETTING = "DATA_SHOW_TUT_PETTING";
    private final String DATA_SHOW_TUT_TOURNAMENT = "DATA_SHOW_TUT_TOURNAMENT";
    private final String DATA_SHOW_TUT_CATCH_DOG_TREATS = "DATA_SHOW_TUT_CATCH_DOG_TREATS";
    private final String DATA_SHOW_TUT_DODGE_THE_CATS = "DATA_SHOW_TUT_DODGE_THE_CATS";
    private final String DATA_SHOW_TUT_TREAT_TOSS = "DATA_SHOW_TUT_TREAT_TOSS";
    private final String DATA_SHOW_TUT_WHACK_THE_CAT = "DATA_SHOW_TUT_WHACK_THE_CAT";
    private final String DATA_HIGH_SCORE_THRESHOLD = "DATA_HIGH_SCORE_THRESHOLD";
    private final String DATA_HIGH_SCORE_SQUARE = "DATA_HIGH_SCORE_SQUARE";
    private final String DATA_CURRENT_RANK_NAME = "DATA_CURRENT_RANK_NAME";
    private final String DATA_CURRENT_RANK_TIER = "DATA_CURRENT_RANK_TIER";
    private final String DATA_CURRENT_HEARTS = "DATA_CURRENT_HEARTS";
    private final String DATA_CURRENT_HEART_TOKENS = "DATA_CURRENT_HEART_TOKENS";
    private final String DATA_PACK_DOGS_UNLOCKED = "DATA_PACK_DOGS_UNLOCKED";
    private final String DATA_TALENT_LEVELS = "DATA_TALENT_LEVELS";
    private final String DATA_ACTIVITY_LEVELS = "DATA_ACTIVITY_LEVELS";

    private final String SETTING_EFFECT_VOLUME = "SETTING_EFFECT_VOLUME";
    private final String SETTING_MUSIC_VOLUME = "SETTING_MUSIC_VOLUME";
    private final String SETTING_SHOW_ANIMATIONS_IN_TOURNAMENT = "SETTING_SHOW_ANIMATIONS_IN_TOURNAMENT";

    private FileSettings fileSettings;

    public FileManager(Context context) {
        this.context = context;

        fileSettings = new FileSettings(context);

        Log.d("===KEYS", fileSettings.getListOfDataKeys().toString());
    }
}
