package com.lertos.projectyorkie.data.file;

import android.content.Context;

public class FileManager {

    private FileSettings fileSettings;
    private FilePlayer filePlayer;
    private FileTutorials fileTutorials;

    public FileManager(Context context) {
        fileSettings = new FileSettings(context);
        filePlayer = new FilePlayer(context);
        fileTutorials = new FileTutorials(context);
    }

    public FileSettings getSettingsFile() {
        return fileSettings;
    }

    public FilePlayer getDataFile() {
        return filePlayer;
    }

    public FileTutorials getTutorialFile() {
        return fileTutorials;
    }

    public void saveFiles() {
        fileSettings.saveValues();
        filePlayer.saveValues();
        fileTutorials.saveValues();
    }
}
