package com.lertos.projectyorkie.data.file;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kotlin.Pair;
import kotlin.Triple;


public class DataFile {

    protected List<Triple> listOfDefaultKeys;
    protected List<Triple> listOfDataKeys;
    protected final String fileName;
    private final String PAIR_SEPARATOR = ":";
    protected Context context;
    private final String VALUE_SEPARATOR = "|";
    private boolean hasNewChanges = false;

    public DataFile(String fileName, Context context) {
        this.listOfDefaultKeys = new ArrayList<>();
        this.listOfDataKeys = new ArrayList<>();
        this.fileName = fileName;
        this.context = context;
    }

    protected void setListOfDefaultKeys() {

    }

    protected void setupFile() {
        //If the file already exists, replace the default values with what we have in the file
        if (doesFileExist()) {
            loadCurrentKeyValues();
        } else {
            for (Triple triple : listOfDefaultKeys)
                listOfDataKeys.add(triple);

            saveValues();
        }
    }

    private boolean doesFileExist() {
        String[] files = context.fileList();

        for (String file : files) {
            if (file.equalsIgnoreCase(fileName))
                return true;
        }
        return false;
    }

    private void loadCurrentKeyValues() {
        FileInputStream fis;

        try {
            fis = context.openFileInput(fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        HashMap<String, String> lines = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            Pair<String, String> pair;

            while (line != null) {
                pair = getPairFromLine(line);
                lines.put(pair.getFirst(), pair.getSecond());
                line = reader.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Now we check which triples to pull from the file and which one are new to add going forward
        for (Triple triple : listOfDefaultKeys) {
            String key = triple.getFirst().toString();

            //Add existing triples copying everything but the value; which we get from the save file
            if (lines.containsKey(key)) {
                Triple newTriple = new Triple<>(key, triple.getSecond(), lines.get(key));

                listOfDataKeys.add(newTriple);
            }
            //If the key doesn't exist in the file, it means it's a new key, so add the default value
            else
                listOfDataKeys.add(triple);
        }
    }

    private Triple getTripleFromKey(Enum enumKey) {
        String enumName = enumKey.name();

        for (Triple triple : listOfDataKeys) {
            String key = triple.getFirst().toString();

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
            String key = listOfDataKeys.get(i).getFirst().toString();

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

        //Set to true so that the next save will save this file
        hasNewChanges = true;
    }

    public void saveValues() {
        if (!hasNewChanges)
            return;

        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            StringBuilder sb = new StringBuilder();

            for (Triple triple : listOfDataKeys)
                sb.append(triple.getFirst().toString()).append(PAIR_SEPARATOR).append(triple.getThird().toString()).append("\n");

            fos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Log.d(fileName, "FILE SAVED WITH NEW VALUES");

        //Reset back to false so that saves only happen for new changes
        hasNewChanges = false;
    }

    private Pair<String, String> getPairFromLine(String line) {
        int index = line.indexOf(PAIR_SEPARATOR);

        if (index == -1)
            throw new RuntimeException();

        return new Pair(line.substring(0, index), line.substring(index + 1));
    }

    public List<Triple> getListOfDataKeys() {
        return listOfDataKeys;
    }

}
