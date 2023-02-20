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

    private DataManager() {
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void start() {
        //TODO: Later on, load (and save) the player data in a file and populate the Player object with the data
        playerData = new Player(0, 0, 0);

        playerData.setPettingHighestThreshold(1);
        playerData.setPettingHighestSquare(3);

        PackDogs packDogs = new PackDogs();
        packDogList = packDogs.getListPackDogs();

        Talents talents = new Talents();
        talentList = talents.getListTalents();

        Activities activities = new Activities();
        activityList = activities.getListActivities();

        setHeartsPerSecond();
        setHeartTokensPerSecond();
    }

    public Player getPlayerData() {
        return playerData;
    }

    public void setHeartsPerSecond() {
        double heartsPerSecond = 0;

        for (Activity activity : activityList) {
            if (!activity.isUnlocked())
                break;
            heartsPerSecond += activity.getCurrentIncome();
        }
        playerData.setCurrentHeartsPerSecond(heartsPerSecond);
    }

    public void setHeartTokensPerSecond() {
        double heartTokensPerSecond = 0;

        for (Activity activity : activityList) {
            if (!activity.isUnlocked())
                break;
            heartTokensPerSecond += activity.getBaseHeartTokensPerSecond();
        }
        playerData.setCurrentHeartTokensPerSecond(heartTokensPerSecond);
    }

    public void addHearts(double hearts) {
        playerData.setCurrentHearts(playerData.getCurrentHearts() + hearts);
    }

    public void addHeartTokens(double heartTokens) {
        playerData.setCurrentHeartTokens(playerData.getCurrentHeartTokens() + heartTokens);
    }


    public void calculateHeartsPerSecond() {
        DataManager.getInstance().setHeartsPerSecond();

        double currentHearts = DataManager.getInstance().getPlayerData().getCurrentHearts();
        double currentHeartsPerSecond = DataManager.getInstance().getPlayerData().getCurrentHeartsPerSecond();

        DataManager.getInstance().getPlayerData().setCurrentHearts(currentHearts + currentHeartsPerSecond);
    }

    public void calculateHeartTokensPerSecond() {
        DataManager.getInstance().setHeartTokensPerSecond();

        double currentHeartTokens = DataManager.getInstance().getPlayerData().getCurrentHeartTokens();
        double currentHeartTokensPerSecond = DataManager.getInstance().getPlayerData().getCurrentHeartTokensPerSecond();

        DataManager.getInstance().getPlayerData().setCurrentHeartTokens(currentHeartTokens + currentHeartTokensPerSecond);
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
