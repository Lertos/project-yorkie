package com.lertos.projectyorkie.data.file;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private final List<DataFile> filesToSave;
    private final FileSettings fileSettings;
    private final FilePlayer filePlayer;
    private final FileTutorials fileTutorials;

    public FileManager(Context context) {
        filesToSave = new ArrayList<>();

        fileSettings = new FileSettings(context);
        filePlayer = new FilePlayer(context);
        fileTutorials = new FileTutorials(context);

        filesToSave.add(fileSettings);
        filesToSave.add(filePlayer);
        filesToSave.add(fileTutorials);
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
        for (DataFile file : filesToSave)
            file.saveValues();
    }

    public boolean isAnySaveNeeded() {
        for (DataFile file : filesToSave) {
            if (file.hasNewChanges())
                return true;
        }
        return false;
    }
}
