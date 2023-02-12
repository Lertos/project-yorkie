package com.lertos.projectyorkie.model;

import com.lertos.projectyorkie.Helper;
import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.MediaManager;

public class Talent {

    private final String name;
    private final String description;
    private int currentLevel;
    private final double baseUpgradeCost;
    private final double baseCostRateGrowth;
    private final double baseBonus;
    private final double baseBonusAddedPerLevel;
    private boolean isUnlocked = false;

    //TODO: Need to have enums for the sign of the bonus (increase/decrease)
    //TODO: Need to have enums for the type of the bonus (percentage/whole)


    public Talent(String name, String description, double baseUpgradeCost, double baseCostRateGrowth, double baseBonus, double baseBonusAddedPerLevel) {
        this.name = name;
        this.description = description;
        this.baseUpgradeCost = baseUpgradeCost;
        this.baseCostRateGrowth = baseCostRateGrowth;
        this.baseBonus = baseBonus;
        this.baseBonusAddedPerLevel = baseBonusAddedPerLevel;
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
        return Helper.roundNumber(baseUpgradeCost * Math.pow(baseCostRateGrowth, currentLevel));
    }

    public double getCurrentBonus() {
        return Helper.roundNumber(baseBonus + (baseBonusAddedPerLevel * currentLevel));
    }

    public double getNextBonus() {
        return Helper.roundNumber(baseBonus + (baseBonusAddedPerLevel * (currentLevel + 1)));
    }

}
