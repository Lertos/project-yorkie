package com.lertos.projectyorkie.model;

import com.lertos.projectyorkie.Helper;

public class Activity {

    //TODO: Add multipliers to all the get double methods
    private final String name;
    private int currentLevel = 0;
    private final double baseUpgradeCost;
    private final double baseRateGrowth;
    private final double baseProductionOutput;
    private boolean isUnlocked = false;

    public Activity(String name, double baseUpgradeCost, double baseRateGrowth, double baseProductionOutput) {
        this.name = name;
        this.baseUpgradeCost = baseUpgradeCost;
        this.baseRateGrowth = baseRateGrowth;
        this.baseProductionOutput = baseProductionOutput;
    }

    public String getName() {
        return name;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void levelUp() {
        this.currentLevel += 1;

        if (this.currentLevel == 1)
            this.isUnlocked = true;
    }

    public double getBaseUpgradeCost() {
        return baseUpgradeCost;
    }

    public double getBaseRateGrowth() {
        return baseRateGrowth;
    }

    public double getBaseProductionOutput() {
        return baseProductionOutput;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }

    public double getNextUpgradeCost() {
        return Helper.roundNumber(baseUpgradeCost * Math.pow(baseRateGrowth, currentLevel));
    }

    public double getCurrentProductionOutput() {
        return Helper.roundNumber(baseProductionOutput * currentLevel);
    }

    public double getNextProductionOutput() {
        return Helper.roundNumber(baseProductionOutput * (currentLevel + 1));
    }
}
