package com.lertos.projectyorkie.model;

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
    private int currentLevel = 1;

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

    public TalentBonusType getBonusType() {
        return bonusType;
    }

    public String getBonusTypeSuffix() {
        return bonusType.getSuffix();
    }

    public int getBonusSign() {
        return bonusSign;
    }

    public String getBonusSignPrefix() {
        if (bonusSign < 0)
            return "-";
        return "";
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void levelUp() {
        this.currentLevel += 1;
        MediaManager.getInstance().playEffectTrack(R.raw.effect_levelup);
    }

    //TODO: Add multipliers from talents
    public double getNextUpgradeCost() {
        return costConstant + (costBase * Math.pow(currentLevel, costExponentNumerator) / currentLevel);
    }

    private double getBonus(int level) {
        return bonusBase + (bonusAddedPerLevel * level);
    }

    public double getCurrentDisplayBonus() {
        double bonus = getBonus(currentLevel);

        bonus *= bonusSign;

        return bonus;
    }

    public double getNextDisplayBonus() {
        double bonus = getBonus(currentLevel + 1);

        bonus *= bonusSign;

        return bonus;
    }

    public double getCurrentBonus() {
        double bonus = getBonus(currentLevel + 1);

        if (bonusType.equals(TalentBonusType.PERCENTAGE))
            bonus = (1 + bonus / 100);

        bonus *= bonusSign;

        return bonus;
    }

}
