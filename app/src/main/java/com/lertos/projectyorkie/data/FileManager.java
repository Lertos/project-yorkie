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

    private final String DATA_HAS_PLAYED_BEFORE = "";
    private final String DATA_LAST_TIME_ON = "";
    private final String DATA_SHOW_TUT_HOME = "";
    private final String DATA_SHOW_TUT_ACTIVITY = "";
    private final String DATA_SHOW_TUT_PETTING = "";
    private final String DATA_SHOW_TUT_TOURNAMENT = "";
    private final String DATA_SHOW_TUT_CATCH_DOG_TREATS = "";
    private final String DATA_SHOW_TUT_DODGE_THE_CATS = "";
    private final String DATA_SHOW_TUT_TREAT_TOSS = "";
    private final String DATA_SHOW_TUT_WHACK_THE_CAT = "";
    private final String DATA_HIGH_SCORE_THRESHOLD = "";
    private final String DATA_HIGH_SCORE_SQUARE = "";
    private final String DATA_CURRENT_RANK_NAME = "";
    private final String DATA_CURRENT_RANK_TIER = "";
    private final String DATA_CURRENT_HEARTS = "";
    private final String DATA_CURRENT_HEART_TOKENS = "";
    private final String DATA_PACK_DOGS_UNLOCKED = "";
    private final String DATA_TALENT_LEVELS = "";
    private final String DATA_ACTIVITY_LEVELS = "";

    private final String SETTING_EFFECT_VOLUME = "";
    private final String SETTING_MUSIC_VOLUME = "";
    private final String SETTING_SHOW_ANIMATIONS_IN_TOURNAMENT = "";

    public FileManager(Context context) {
        this.context = context;

        createFilesIfNotExist();

        try {
            readFileContents(FILE_NAME_SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return "Test Settings";
    }

    private void readFileContents(String fileName) throws FileNotFoundException {
        FileInputStream fis = context.openFileInput(fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();

            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            String contents = stringBuilder.toString();

            Log.d("==============FileContents ", contents);
        }
    }

    public void updateSettings(String key, String newValue) {

    }

    public void updatePlayerData(String key, String newValue) {

    }
}
