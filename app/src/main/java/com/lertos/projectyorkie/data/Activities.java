package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.model.Activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Activities {

    private List<Activity> listActivities;

    public Activities() {
        this.listActivities = new ArrayList<>();

        listActivities.add(new Activity("Pet their head", 4, 1.07, 1.67));
        listActivities.add(new Activity("Throw the ball around", 18, 1.065, 15));
        listActivities.add(new Activity("Take for a walk", 34, 1.06, 32));

        //TODO: Read from the file - or update the list after reading the player data "dogs_unlocked" section
        listActivities.get(0).setCurrentLevel(1);
        listActivities.get(0).setUnlocked(true);
    }

    public List<Activity> getListActivities() {
        return Collections.unmodifiableList(listActivities);
    }

}
