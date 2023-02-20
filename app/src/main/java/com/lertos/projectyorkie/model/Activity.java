package com.lertos.projectyorkie.model;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.MediaManager;
import com.lertos.projectyorkie.data.Talents;

public class Activity {

    //TODO: Add multipliers to all the get double methods
    private final String name;
    private int currentLevel = 0;
    private double baseHeartTokensPerSecond;
    private final int orderPosition;
    private final double costConstant = 8.0;
    private final double costBase = 36.0;
    private final double costExponent = 2.5;
    private final double costGrowthConstant = 1.30;
    private final double costGrowthMultiplier = 0.005;
    private final double incomeConstant = 4.0;
    private final double incomeBase = 15.0;
    private final double incomeExponent = 1.5;
    private final double incomeGrowthConstant = 1.05;
    private final double incomeGrowthMultiplier = 0.0025;
    private boolean isUnlocked = false;

    public Activity(String name, int orderPosition, double baseHeartTokensPerSecond) {
        this.name = name;
        this.orderPosition = orderPosition;
        this.baseHeartTokensPerSecond = baseHeartTokensPerSecond;
    }

    public String getName() {
        return name;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void levelUp() {
        this.currentLevel += 1;

        if (this.currentLevel == 1) {
            this.isUnlocked = true;
            MediaManager.getInstance().playEffectTrack(R.raw.effect_dog_bark);
        } else
            MediaManager.getInstance().playEffectTrack(R.raw.effect_levelup);
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public double getNextUpgradeCost() {
        double totalCost = (costConstant + costBase * Math.pow(orderPosition, costExponent)) * Math.pow((costGrowthConstant - (orderPosition * costGrowthMultiplier)), currentLevel);

        return totalCost;
    }

    private double getIncome(int level) {
        double totalIncome = incomeConstant + incomeBase * Math.pow(orderPosition, incomeExponent) * Math.pow((incomeGrowthConstant + (orderPosition * incomeGrowthMultiplier)), level);
        double heartsMultiplier = Talents.heartBeater.getCurrentBonus();

        //TODO: Apply pack bonuses
        if (heartsMultiplier != 0)
            totalIncome *= heartsMultiplier;

        return totalIncome;
    }

    public double getCurrentIncome() {
        return getIncome(currentLevel);
    }

    public double getNextIncome() {
        return getIncome(currentLevel + 1);
    }

    public double getBaseHeartTokensPerSecond() {
        //TODO: Add talent multipliers
        return baseHeartTokensPerSecond;
    }
}
