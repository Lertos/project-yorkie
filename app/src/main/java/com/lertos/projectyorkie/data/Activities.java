package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.model.Activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Activities {

    private List<Activity> listActivities;

    public Activities() {
        this.listActivities = new ArrayList<>();

        listActivities.add(new Activity("Pet Their Head", 8, 1.19, 4, 1.05));
        listActivities.add(new Activity("Throw The Ball Around", 80, 1.18, 26, 1.06));
        listActivities.add(new Activity("Go For A Walk", 800, 1.17, 305, 1.07));

        //TODO: Read from the file - or update the list after reading the player data "dogs_unlocked" section
        listActivities.get(0).levelUp();
    }

    public List<Activity> getListActivities() {
        return Collections.unmodifiableList(listActivities);
    }

}
