package com.lertos.projectyorkie.model;

import com.lertos.projectyorkie.Helper;

public class Talent {

    private final String name;
    private final String description;
    private int currentLevel;
    private double currentBonus;
    private double nextBonus;
    private boolean isUnlocked = false;

    //TODO: Need to have the math such as in Activity for calculating the cost/bonuses
    //TODO: Need to have enums for the sign of the bonus (increase/decrease)
    //TODO: Need to have enums for the type of the bonus (percentage/whole)

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

    //The boolean is to determine if it was a new unlock or not
    public boolean levelUp() {
        this.currentLevel += 1;

        if (this.currentLevel == 1) {
            this.isUnlocked = true;
            return true;
        }
        return false;
    }

}
