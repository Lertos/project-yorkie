package com.lertos.projectyorkie.data.file;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import kotlin.Triple;


public class DataFile {

    protected List<Triple> listOfDataKeys;
    protected String fileName;
    protected Context context;

    public DataFile(List<Triple> listOfDataKeys, String fileName, Context context) {
        this.listOfDataKeys = listOfDataKeys;
        this.fileName = fileName;
        this.context = context;

        setupFile();
    }

    private void setupFile() {
        if (!fileExists(fileName))
            createFile();

        addMissingDefaults();
    }

    private void createFile() {
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(new byte[0]);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean fileExists(String fileName) {
        String[] files = context.fileList();

        for (int i = 0; i < files.length; i++) {
            if (files[i].equalsIgnoreCase(fileName))
                return true;
        }
        return false;
    }

    private void addMissingDefaults() {

    }

}
