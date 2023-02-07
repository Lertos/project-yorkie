package com.lertos.projectyorkie.model;

public class Activity {

    //TODO: Add multipliers to all the get double methods
    private final String name;
    private int currentLevel;
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

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
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
        return baseUpgradeCost * Math.pow(baseRateGrowth, currentLevel);
    }

    public double getCurrentProductionOutput() {
        return baseProductionOutput * currentLevel;
    }

    public double getNextProductionOutput() {
        return baseProductionOutput * (currentLevel + 1);
    }
}
