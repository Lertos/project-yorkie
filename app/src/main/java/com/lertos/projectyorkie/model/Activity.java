package com.lertos.projectyorkie.model;

import com.lertos.projectyorkie.Helper;
import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.MediaManager;

public class Activity {

    //TODO: Add multipliers to all the get double methods
    private final String name;
    private int currentLevel = 0;
    private final int orderPosition;
    private final double costConstant = 8.0;
    private final double costBase = 36.0;
    private final double costExponent = 3.0;
    private final double costGrowthConstant = 1.20;
    private final double costGrowthMultiplier = 0.001;
    private final double incomeConstant = 4.0;
    private final double incomeBase = 15.0;
    private final double incomeExponent = 2.5;
    private final double incomeGrowthConstant = 1.05;
    private final double incomeGrowthMultiplier = 0.001;
    private boolean isUnlocked = false;

    public Activity(String name, int orderPosition) {
        this.name = name;
        this.orderPosition = orderPosition;
    }

    public String getName() {
        return name;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }

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
        return Helper.roundNumber((costConstant + costBase * Math.pow(orderPosition, costExponent)) * Math.pow((costGrowthConstant - (orderPosition * costGrowthMultiplier)), currentLevel));
    }

    public double getCurrentProductionOutput() {
        return Helper.roundNumber(incomeConstant + incomeBase * Math.pow(orderPosition, incomeExponent) * Math.pow((incomeGrowthConstant + (orderPosition * incomeGrowthMultiplier)), currentLevel));
    }

    public double getNextProductionOutput() {
        return Helper.roundNumber(incomeConstant + incomeBase * Math.pow(orderPosition, incomeExponent) * Math.pow((incomeGrowthConstant + (orderPosition * incomeGrowthMultiplier)), (currentLevel + 1)));
    }
}
