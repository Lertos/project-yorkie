package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.model.Activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Activities {

    private List<Activity> listActivities;

    public Activities() {
        this.listActivities = new ArrayList<>();

        listActivities.add(new Activity("Pet Their Head", 5, 1.08, 6));
        listActivities.add(new Activity("Throw The Ball Around", 125, 1.075, 130));
        listActivities.add(new Activity("Go For A Walk", 890, 1.07, 900));

        //TODO: Read from the file - or update the list after reading the player data "dogs_unlocked" section
        listActivities.get(0).levelUp();
        listActivities.get(0).setUnlocked(true);
    }

    public List<Activity> getListActivities() {
        return Collections.unmodifiableList(listActivities);
    }

}
