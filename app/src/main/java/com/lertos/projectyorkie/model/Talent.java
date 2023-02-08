package com.lertos.projectyorkie.model;

import com.lertos.projectyorkie.Helper;

public class Talent {

    private final String name;
    private final String description;
    private int currentLevel;
    private double currentBonus;
    private double nextBonus;

    //TODO: Need to have enums for the sign of the bonus (increase/decrease)
    //TODO: Need to have enums for the type of the bonus (percentage/whole)
    //TODO: Need to have unlock order as well as isUnlocked; also the sorting comparator from PackDog

    public Talent(String name, String description, int currentLevel, double currentBonus, double nextBonus) {
        this.name = name;
        this.description = description;
        this.currentLevel = currentLevel;
        this.currentBonus = currentBonus;
        this.nextBonus = nextBonus;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public double getCurrentBonus() {
        return Helper.roundNumber(currentBonus);
    }

    public double getNextBonus() {
        return Helper.roundNumber(nextBonus);
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void setCurrentBonus(double currentBonus) {
        this.currentBonus = currentBonus;
    }

    public void setNextBonus(double nextBonus) {
        this.nextBonus = nextBonus;
    }
}
