package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.model.Activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Activities {

    private List<Activity> listActivities;

    public Activities() {
        this.listActivities = new ArrayList<>();

        listActivities.add(new Activity("Pet Their Head", 2));
        listActivities.add(new Activity("Throw the Ball Around", 3));
        listActivities.add(new Activity("Go for a Walk", 4));
        listActivities.add(new Activity("Cover Them in a Blanket", 5));
        listActivities.add(new Activity("Feed Them Low-Grade Food", 6));
        listActivities.add(new Activity("Teach Them Tricks", 7));
        listActivities.add(new Activity("Rub Their Paws", 8));
        listActivities.add(new Activity("Feed Them Medium-Grade Food", 9));
        listActivities.add(new Activity("Play with the Squeaky Toy", 10));
        listActivities.add(new Activity("Call the Mailman Over", 11));
        listActivities.add(new Activity("Stroke Behind the Ears", 12));
        listActivities.add(new Activity("Feed Them Premium-Grade Food", 13));

        //TODO: Read from the file - or update the list after reading the player data "dogs_unlocked" section
        listActivities.get(0).levelUp();
    }

    public List<Activity> getListActivities() {
        return Collections.unmodifiableList(listActivities);
    }

}
