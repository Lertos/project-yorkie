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

/* PLAYER DATA
    hasPlayedBefore;0/1
    lastTimeOn;unixTimeStamp
    --screens
    showTutHome;0/1
    showTutActivity;0/1
    showTutPetting;0/1
    showTutTournament;0/1
    --games
    showTutCatchDogTreats;0/1
    showTutDodgeTheCats;0/1
    showTutTreatToss;0/1
    showTutWhackTheCat;0/1
    --highscores
    highestThreshold;int
    highestSquare;int
    currentRankName;String
    currentRankTier;int
    --data
    currentHearts;double.2f
    currentHeartTokens;double.2f
    --levels
    packDogsUnlocked;1|1|0|0|0...
    talentLevels;12|6|1|1|1...
    activityLevels;12|6|0|0|0... //zero means not unlocked
*/

/* SETTINGS
   effectVolume;0.5
   musicVolume;0.5
   showAppearAnimationsInTournament;0/1
*/

    private Context context;
    private final String FILE_NAME_SETTINGS = "settings";
    private final String FILE_NAME_PLAYER_DATA = "playerData";

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
