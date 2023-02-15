package com.lertos.projectyorkie.model;

import com.lertos.projectyorkie.Helper;
import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.MediaManager;

public class Talent {

    private final String name;
    private final String description;
    private final TalentBonusType bonusType;
    private final int bonusSign;
    private final double costConstant = 500.0;
    private final double costBase = 5.0;
    private final double costExponentNumerator;
    private final double bonusBase;
    private final double bonusAddedPerLevel;
    private final int maxLevel;
    private int currentLevel;
    private boolean isUnlocked = false;

    public Talent(String name, String description, TalentBonusType bonusType, int bonusSign, double costExponentNumerator, double bonusBase, double bonusAddedPerLevel, int maxLevel) {
        this.name = name;
        this.description = description;
        this.bonusType = bonusType;
        this.bonusSign = bonusSign;
        this.costExponentNumerator = costExponentNumerator;
        this.bonusBase = bonusBase;
        this.bonusAddedPerLevel = bonusAddedPerLevel;
        this.maxLevel = maxLevel;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TalentBonusType getBonusType() { return bonusType; }

    public int getBonusSign() { return bonusSign; }

    public int getMaxLevel() { return maxLevel; }

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

    //TODO: Add multipliers from talents
    public double getNextUpgradeCost() {
        return Helper.roundNumber(costConstant + (costBase * Math.pow(currentLevel, costExponentNumerator) / currentLevel));
    }

    private double getBonus(int level) {
        return bonusBase + (bonusAddedPerLevel * level);
    }

    public double getCurrentDisplayBonus() {
        double bonus = getBonus(currentLevel);

        bonus *= bonusSign;

        return Helper.roundNumber(bonus);
    }

    public double getNextDisplayBonus() {
        double bonus = getBonus(currentLevel + 1);

        bonus *= bonusSign;

        return Helper.roundNumber(bonus);
    }

    public double getCurrentBonus() {
        double bonus = getBonus(currentLevel + 1);

        if (bonusType.equals(TalentBonusType.PERCENTAGE))
            bonus = (1 + bonus / 100);

        bonus *= bonusSign;

        return Helper.roundNumber(bonus);
    }

}
