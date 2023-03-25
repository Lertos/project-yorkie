package com.lertos.projectyorkie.data;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileManager {

    private Context context;
    private final String FILE_NAME_SETTINGS = "settings";
    private final String FILE_NAME_PLAYER_DATA = "playerData";

    private final String PAIR_SEPARATOR = ":";
    private final String VALUE_SEPARATOR = "|";

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

    public FileManager(Context context) {
        this.context = context;

        createFilesIfNotExist();

        Log.d("===EffectVolume", getValueOfKey(FILE_NAME_SETTINGS, SETTING_EFFECT_VOLUME));
    }

    private void createFilesIfNotExist() {
        String[] files = context.fileList();

        if (files.length != 0)
            return;

        createDefaultFile(FILE_NAME_SETTINGS, getDefaultSettingsContent());
    }

    private void createDefaultFile(String fileName, String defaultText) {
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(defaultText.getBytes(StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getDefaultSettingsContent() {
        StringBuilder sb = new StringBuilder();

        sb.append(SETTING_EFFECT_VOLUME).append(PAIR_SEPARATOR).append("0.5").append("\n");
        sb.append(SETTING_MUSIC_VOLUME).append(PAIR_SEPARATOR).append("0.25").append("\n");
        sb.append(SETTING_SHOW_ANIMATIONS_IN_TOURNAMENT).append(PAIR_SEPARATOR).append("0");

        return sb.toString();
    }

    private String getValueOfKey(String fileName, String key) {
        FileInputStream fis;

        try {
            fis = context.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            return "";
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        String line = "";
        String lineKey;

        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            line = reader.readLine();
            lineKey = getKeyFromLine(line);

            while (line != null) {
                Log.d("d", line);
                if (!lineKey.equalsIgnoreCase(key)) {
                    line = reader.readLine();
                    lineKey = getKeyFromLine(line);
                    continue;
                } else {
                    return getValueFromLine(line);
                }
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getKeyFromLine(String line) {
        int index = line.indexOf(PAIR_SEPARATOR);

        if (index == -1)
            return "";

        return line.substring(0, index);
    }

    private String getValueFromLine(String line) {
        int index = line.indexOf(PAIR_SEPARATOR);

        if (index == -1)
            return "";

        return line.substring(index);
    }
}
