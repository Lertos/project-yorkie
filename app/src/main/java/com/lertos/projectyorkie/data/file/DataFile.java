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
        FileInputStream fis;

        try {
            fis = context.openFileInput(fileName);
        } catch (Exception e) {
            return;
        }

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

    private Triple getTripleFromKey(Enum enumKey) {
        String enumName = enumKey.name();

        for (Triple triple : listOfDataKeys) {
            String key = ((Enum) triple.getFirst()).name();

            if (key.equalsIgnoreCase(enumName))
                return triple;
        }
        throw new RuntimeException("No key found with that enumName");
    }

    public boolean getBoolean(Enum enumKey) {
        Triple triple = getTripleFromKey(enumKey);

        if (!triple.getSecond().equals(boolean.class))
            throw new RuntimeException("The key provided has a different class than the calling method");
        return Boolean.parseBoolean(triple.getThird().toString());
    }

    public float getFloat(Enum enumKey) {
        Triple triple = getTripleFromKey(enumKey);

        if (!triple.getSecond().equals(float.class))
            throw new RuntimeException("The key provided has a different class than the calling method");
        return Float.parseFloat(triple.getThird().toString());
    }

    public int getInt(Enum enumKey) {
        Triple triple = getTripleFromKey(enumKey);

        if (!triple.getSecond().equals(int.class))
            throw new RuntimeException("The key provided has a different class than the calling method");
        return Integer.parseInt(triple.getThird().toString());
    }

    public double getDouble(Enum enumKey) {
        Triple triple = getTripleFromKey(enumKey);

        if (!triple.getSecond().equals(double.class))
            throw new RuntimeException("The key provided has a different class than the calling method");
        return Double.parseDouble(triple.getThird().toString());
    }

    public String getString(Enum enumKey) {
        Triple triple = getTripleFromKey(enumKey);

        if (!triple.getSecond().equals(String.class))
            throw new RuntimeException("The key provided has a different class than the calling method");
        return triple.getThird().toString();
    }

    public <T> void setValue(Enum enumKey, T value) {
        String stringValue = String.valueOf(value);
        String enumName = enumKey.name();
        int index = -1;

        for (int i = 0; i < listOfDataKeys.size(); i++) {
            String key = ((Enum) listOfDataKeys.get(i).getFirst()).name();

            if (key.equalsIgnoreCase(enumName)) {
                index = i;
                break;
            }
        }
        if (index == -1)
            throw new RuntimeException("No key found with that enumName");

        Triple triple = listOfDataKeys.get(index);
        Triple newTriple = new Triple<>(triple.getFirst(), triple.getSecond(), stringValue);

        listOfDataKeys.remove(index);
        listOfDataKeys.add(index, newTriple);
    }

    //TODO: Remove after getting a working set method for all value types
    public String getValueOfKey(Enum enumKey) {
        FileInputStream fis;

        try {
            fis = context.openFileInput(fileName);
        } catch (Exception e) {
            throw new RuntimeException();
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
        throw new RuntimeException();
    }

    private String getKeyFromLine(String line) {
        int index = line.indexOf(PAIR_SEPARATOR);

        if (index == -1)
            throw new RuntimeException();

        return line.substring(0, index);
    }

    private String getValueFromLine(String line) {
        int index = line.indexOf(PAIR_SEPARATOR);

        if (index == -1)
            throw new RuntimeException();

        return line.substring(index + 1);
    }

    public List<Triple> getListOfDataKeys() {
        return listOfDataKeys;
    }
}
