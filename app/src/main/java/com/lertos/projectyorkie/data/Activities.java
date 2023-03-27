package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.model.Activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Activities {

    private List<Activity> listActivities;
    private final String separator;

    public Activities(String separator) {
        this.listActivities = new ArrayList<>();
        this.separator = separator;

        listActivities.add(new Activity("Pet Their Head", 2, 0.1));
        listActivities.add(new Activity("Throw the Ball Around", 3, 0.16));
        listActivities.add(new Activity("Go for a Walk", 4, 0.27));
        listActivities.add(new Activity("Cover Them in a Blanket", 5, 0.39));
        listActivities.add(new Activity("Feed Them Low-Grade Food", 6, 0.65));
        listActivities.add(new Activity("Teach Them Tricks", 7, 0.95));
        listActivities.add(new Activity("Rub Their Paws", 8, 1.35));
        listActivities.add(new Activity("Feed Them Medium-Grade Food", 9, 1.9));
        listActivities.add(new Activity("Play with the Squeaky Toy", 10, 2.5));
        listActivities.add(new Activity("Call the Mailman Over", 11, 3.8));
        listActivities.add(new Activity("Stroke Behind the Ears", 12, 5.3));
        listActivities.add(new Activity("Feed Them Premium-Grade Food", 13, 8.9));

        listActivities.get(0).setUnlocked(true);
        listActivities.get(0).setCurrentLevel(1);
    }

    public void setInitialLevels(String strLevels) {
        if (strLevels.isEmpty())
            return;

        String[] levels = strLevels.split(separator);

        if (levels.length != listActivities.size())
            throw new RuntimeException("Activities: Saved value does not have equal # of elements");

        for (int i = 0; i < levels.length; i++) {
            int level = Integer.parseInt(levels[i]);

            if (level > 0) {
                listActivities.get(i).setUnlocked(true);
                listActivities.get(i).setCurrentLevel(level);
            }
        }
    }

    public String getLevelsAsString() {
        StringBuilder sb = new StringBuilder();

        for (Activity activity : listActivities) {
            sb.append(activity.getCurrentLevel()).append(separator.replace("\\", ""));
        }
        String finalString = sb.toString();
        return finalString.substring(0, finalString.length() - 1);
    }

    public List<Activity> getListActivities() {
        return Collections.unmodifiableList(listActivities);
    }

}
