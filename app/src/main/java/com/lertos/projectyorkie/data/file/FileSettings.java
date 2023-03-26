package com.lertos.projectyorkie.data.file;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

import kotlin.Triple;


public class FileSettings extends DataFile {

    private ArrayList<Triple> defaultKeys = new ArrayList<>(Arrays.asList(
            new Triple("test", boolean.class, "0"),
            new Triple("test2", boolean.class, "1")
    ));

    public FileSettings(Context context) {
        super("settings", context);

        setListOfDataKeys();
        setupFile();
    }

    @Override
    protected void setListOfDataKeys() {
        listOfDataKeys = (ArrayList<Triple>) defaultKeys.clone();
    }
}
