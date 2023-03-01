package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.model.Activity;
import com.lertos.projectyorkie.model.PackDog;
import com.lertos.projectyorkie.model.Talent;
import com.lertos.projectyorkie.tournament.TournamentDivision;
import com.lertos.projectyorkie.tournament.TournamentRank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
        playerData = new Player(100000, 100, 0);

        playerData.setPettingHighestThreshold(1);
        playerData.setPettingHighestSquare(3);

        TournamentRank rank = new TournamentRank();
        rank.setDivision(getDivisionFromString("Wood"));
        rank.setTier(3);

        playerData.setTournamentRank(rank);

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

    public double getTotalPackMultiplier() {
        double bonus = 0;
        double multiplier = Talents.strengthInNumbers.getCurrentBonus();

        for (PackDog packDog : packDogList) {
            if (!packDog.isUnlocked())
                break;
            bonus += packDog.getAddedBonus();
        }

        if (multiplier != 0)
            bonus *= multiplier;

        return bonus;
    }

    public PackDog getRandomPackDog() {
        Random rng = new Random();
        int randomIndex = rng.nextInt(packDogList.size());

        return packDogList.get(randomIndex);
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

        double multiplier = Talents.luckyStreak.getCurrentBonus();

        if (multiplier != 0)
            heartTokensPerSecond *= multiplier;

        playerData.setCurrentHeartTokensPerSecond(heartTokensPerSecond);
    }

    public void addHearts(double hearts) {
        if (hearts < 0 && hearts > playerData.getCurrentHearts())
            return;
        playerData.setCurrentHearts(playerData.getCurrentHearts() + hearts);
    }

    public void addHeartTokens(double heartTokens) {
        if (heartTokens < 0 && heartTokens > playerData.getCurrentHeartTokens())
            return;
        playerData.setCurrentHeartTokens(playerData.getCurrentHeartTokens() + heartTokens);
    }


    public void calculateHeartsPerSecond() {
        DataManager.getInstance().setHeartsPerSecond();
        addHearts(playerData.getCurrentHeartsPerSecond());
    }

    public void calculateHeartTokensPerSecond() {
        DataManager.getInstance().setHeartTokensPerSecond();
        addHeartTokens(playerData.getCurrentHeartTokensPerSecond());
    }

    private TournamentDivision getDivisionFromString(String str) {
        for(TournamentDivision division : TournamentDivision.BRONZE.getDeclaringClass().getEnumConstants()) {
            if (str.equalsIgnoreCase(division.getDisplayStr()))
                return division;
        }
        return null;
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
