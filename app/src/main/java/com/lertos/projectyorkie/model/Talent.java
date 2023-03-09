package com.lertos.projectyorkie.model;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.MediaManager;
import com.lertos.projectyorkie.data.Talents;

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

    public String getBonusTypeSuffix() {
        return bonusType.getSuffix();
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

    public double getUpgradeCost(int level) {
        double cost;
        double multiplier = Talents.bargainMaster.getCurrentBonus();

        if (level == -1)
            cost = costConstant + (costBase * Math.pow(currentLevel, costExponentNumerator) / currentLevel);
        else
            cost = costConstant + (costBase * Math.pow(level, costExponentNumerator) / level);

        if (multiplier != 0)
            cost -= Math.abs(cost * multiplier) - Math.abs(cost);

        return cost;
    }

    public void buyMaxLevels() {
        int level = currentLevel;
        double currentHearts;
        double nextCost;
        boolean canAfford = true;

        while (canAfford) {
            currentHearts = DataManager.getInstance().getPlayerData().getCurrentHearts();
            nextCost = getUpgradeCost(level);

            if (level + 1 > maxLevel)
                return;

            if (currentHearts < nextCost)
                canAfford = false;
            else {
                DataManager.getInstance().addHearts(-nextCost);
                level++;
            }
        }
        currentLevel = level;
        MediaManager.getInstance().playEffectTrack(R.raw.effect_levelup);
    }

    private double getBonus(int level) {
        return bonusBase + (bonusAddedPerLevel * level);
    }

    public double getCurrentBonus() {
        double bonus = getBonus(currentLevel);

        if (bonusType.equals(TalentBonusType.PERCENTAGE))
            bonus = (1 + bonus / 100);

        bonus *= bonusSign;

        return bonus;
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
}
