package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.model.Activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Activities {

    private final List<Activity> listActivities;
    private final String separator;

    public Activities(String separator) {
        this.listActivities = new ArrayList<>();
        this.separator = separator;

        listActivities.add(new Activity("Pet Their Head", 2, 1.25));
        listActivities.add(new Activity("Throw the Ball Around", 3, 0.028));
        listActivities.add(new Activity("Go for a Walk", 4, 0.056));
        listActivities.add(new Activity("Cover Them in a Blanket", 5, 0.084));
        listActivities.add(new Activity("Feed Them Low-Grade Food", 6, 0.112));
        listActivities.add(new Activity("Teach Them Tricks", 7, 0.140));
        listActivities.add(new Activity("Rub Their Paws", 8, 0.168));
        listActivities.add(new Activity("Feed Them Medium-Grade Food", 9, 0.196));
        listActivities.add(new Activity("Play with the Squeaky Toy", 10, 0.224));
        listActivities.add(new Activity("Call the Mailman Over", 11, 0.252));
        listActivities.add(new Activity("Stroke Behind the Ears", 12, 0.281));
        listActivities.add(new Activity("Feed Them Premium-Grade Food", 13, 0.309));

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
