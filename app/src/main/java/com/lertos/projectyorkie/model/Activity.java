package com.lertos.projectyorkie.model;

import com.lertos.projectyorkie.Helper;
import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.MediaManager;

public class Activity {

    //TODO: Add multipliers to all the get double methods
    private final String name;
    private int currentLevel = 0;
    private final double powerDivider = 2.0;
    private final double baseUpgradeCost;
    private final double baseCostRateGrowth;
    private final double baseProductionOutput;
    private final double baseProductionGrowth;
    private boolean isUnlocked = false;

    public Activity(String name, double baseUpgradeCost, double baseCostRateGrowth, double baseProductionOutput, double baseProductionGrowth) {
        this.name = name;
        this.baseUpgradeCost = baseUpgradeCost;
        this.baseCostRateGrowth = baseCostRateGrowth;
        this.baseProductionOutput = baseProductionOutput;
        this.baseProductionGrowth = baseProductionGrowth;
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
        return Helper.roundNumber(baseUpgradeCost * Math.pow(baseCostRateGrowth, currentLevel));
    }

    public double getCurrentProductionOutput() {
        return Helper.roundNumber(baseProductionOutput * Math.pow(baseProductionGrowth, (double) currentLevel / powerDivider));
    }

    public double getNextProductionOutput() {
        return Helper.roundNumber(baseProductionOutput * Math.pow(baseProductionGrowth, (double) (currentLevel + 1) / powerDivider));
    }
}
