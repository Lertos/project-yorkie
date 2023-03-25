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

    public DataFile(List<Triple> listOfDataKeys, String fileName, Context context) {
        this.listOfDataKeys = listOfDataKeys;
        this.existingKeys = new ArrayList<>();
        this.fileName = fileName;
        this.context = context;

        setupFile();
    }

    private void setupFile() {
        if (!fileExists(fileName))
            createFile();

        getExistingKeys();
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

    private void getExistingKeys() {
        FileInputStream fis;

        try {
            fis = context.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            return;
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);

        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();

            while (line != null) {
                existingKeys.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            return;
        }
    }

    private void addMissingDefaults() {
        List<Triple> keysToAdd = new ArrayList<>();

        //Find all missing keys
        for (Triple triple : listOfDataKeys) {
            String key = triple.getFirst().toString();

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
                sb.append(triple.getFirst()).append(PAIR_SEPARATOR).append(triple.getThird());

            fos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
