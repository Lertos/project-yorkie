package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.model.Activity;
import com.lertos.projectyorkie.model.PackDog;
import com.lertos.projectyorkie.model.Talent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataManager {

    private static DataManager instance;
    private Player playerData;
    private List<Talent> talentList = new ArrayList<>();
    private List<PackDog> packDogList = new ArrayList<>();
    private List<Activity> activityList = new ArrayList<>();

    private DataManager() {}

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }

        return instance;
    }

    public void start() {
        //TODO: Later on, load (and save) the player data in a file and populate the Player object with the data
        playerData = new Player(450, 12, 3);

        PackDogs packDogs = new PackDogs();
        packDogList = packDogs.getListPackDogs();

        Talents talents = new Talents();
        talentList = talents.getListTalents();

        Activities activities = new Activities();
        activityList = activities.getListActivities();
    }

    public Player getPlayerData() {
        return playerData;
    }

    public List<PackDog> getPackDogs() {
        return Collections.unmodifiableList(packDogList);
    }

    public List<Talent> getTalents() {
        return Collections.unmodifiableList(talentList);
    }

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(activityList);
    }
}
