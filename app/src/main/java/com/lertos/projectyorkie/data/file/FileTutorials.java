package com.lertos.projectyorkie.data.file;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

import kotlin.Triple;


public class FileTutorials extends DataFile {

    private ArrayList<Triple> defaultKeys = new ArrayList<>(Arrays.asList(
            new Triple(FileTutorialsKeys.DATA_SHOW_TUT_HOME, boolean.class, "false"),
            new Triple(FileTutorialsKeys.DATA_SHOW_TUT_ACTIVITY, boolean.class, "false"),
            new Triple(FileTutorialsKeys.DATA_SHOW_TUT_PETTING, boolean.class, "false"),
            new Triple(FileTutorialsKeys.DATA_SHOW_TUT_TOURNAMENT, boolean.class, "false"),
            new Triple(FileTutorialsKeys.DATA_SHOW_TUT_CATCH_DOG_TREATS, boolean.class, "false"),
            new Triple(FileTutorialsKeys.DATA_SHOW_TUT_DODGE_THE_CATS, boolean.class, "false"),
            new Triple(FileTutorialsKeys.DATA_SHOW_TUT_TREAT_TOSS, boolean.class, "false"),
            new Triple(FileTutorialsKeys.DATA_SHOW_TUT_WHACK_THE_CAT, boolean.class, "false")
    ));

    public FileTutorials(Context context) {
        super("tutorials", context);

        setListOfDefaultKeys();
        setupFile();
    }

    @Override
    protected void setListOfDefaultKeys() {
        listOfDefaultKeys = (ArrayList<Triple>) defaultKeys.clone();
    }
}
