package com.lertos.projectyorkie.data.file;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import kotlin.Triple;


public class DataFile {

    protected List<Triple> listOfDataKeys;
    protected final String fileName;
    protected final String PAIR_SEPARATOR = ":";
    protected Context context;
    protected final String VALUE_SEPARATOR = "|";
    protected List<String> existingKeys;

    public DataFile(String fileName, Context context) {
        this.listOfDataKeys = new ArrayList<>();
        this.existingKeys = new ArrayList<>();
        this.fileName = fileName;
        this.context = context;
    }

    protected void setListOfDataKeys() {
    }

    protected void setupFile() {
        getExistingKeys();
        addMissingDefaults();
    }

    private void getExistingKeys() {
        FileInputStream fis = null;

        try {
            fis = context.openFileInput(fileName);
        } catch (Exception e) {
        }

        if (fis == null)
            return;

        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);

        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();

            while (line != null) {
                existingKeys.add(getKeyFromLine(line));
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void addMissingDefaults() {
        List<Triple> keysToAdd = new ArrayList<>();

        //Find all missing keys
        for (Triple triple : listOfDataKeys) {
            String key = ((Enum) triple.getFirst()).name();

            //If the key does not exist, add it and
            if (!existingKeys.contains(key))
                keysToAdd.add(triple);
        }
        writeMissingKeysToFile(keysToAdd);
    }

    private void writeMissingKeysToFile(List<Triple> keysToAdd) {
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE + Context.MODE_APPEND)) {
            StringBuilder sb = new StringBuilder();

            for (Triple triple : keysToAdd)
                sb.append(((Enum) triple.getFirst()).name()).append(PAIR_SEPARATOR).append(triple.getThird()).append("\n");

            fos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getValueOfKey(Enum enumKey) {
        FileInputStream fis;

        try {
            fis = context.openFileInput(fileName);
        } catch (Exception e) {
            return "";
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        String line = "";
        String lineKey;

        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            line = reader.readLine();
            lineKey = getKeyFromLine(line);

            while (line != null) {
                if (!lineKey.equalsIgnoreCase(enumKey.name())) {
                    line = reader.readLine();
                    lineKey = getKeyFromLine(line);
                    continue;
                } else {
                    return getValueFromLine(line);
                }
            }
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

        return line.substring(index + 1);
    }

    public List<Triple> getListOfDataKeys() {
        return listOfDataKeys;
    }
}
