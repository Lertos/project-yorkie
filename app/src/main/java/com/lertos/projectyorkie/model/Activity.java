package com.lertos.projectyorkie.model;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.MediaManager;
import com.lertos.projectyorkie.data.Talents;

public class Activity {

    private final String name;
    private int currentLevel = 0;
    private final double baseHeartTokensPerSecond;
    private final int orderPosition;
    private boolean isUnlocked = false;

    public Activity(String name, int orderPosition, double baseHeartTokensPerSecond) {
        this.name = name;
        this.orderPosition = orderPosition;
        this.baseHeartTokensPerSecond = baseHeartTokensPerSecond;
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

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
        DataManager.getInstance().getFiles().getDataFile().setHasNewChanges(true);
    }

    public void levelUp() {
        this.currentLevel += 1;

        if (this.currentLevel == 1) {
            this.isUnlocked = true;
            MediaManager.getInstance().playEffectTrack(R.raw.effect_dog_bark);
        } else
            MediaManager.getInstance().playEffectTrack(R.raw.effect_levelup);

        DataManager.getInstance().getFiles().getDataFile().setHasNewChanges(true);
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public double getUnlockCost() {
        int unlockLevel = 15;
        double cost = getIncome(unlockLevel * orderPosition) / 10.0 + (orderPosition * 2500.0);
        double multiplier = Talents.greatMinds.getCurrentBonus();

        if (multiplier != 0)
            cost -= Math.abs(cost * multiplier) - Math.abs(cost);

        return cost;
    }

    public double getUpgradeCost(int level) {
        double costExponent = 2.5;
        double costBase = 36.0;
        double costConstant = 8.0;

        double consistentHalf = costConstant + costBase * Math.pow(orderPosition, costExponent);
        double cost;
        double multiplier = Talents.petLover.getCurrentBonus();

        double costGrowthMultiplier = 0.005;
        double costGrowthConstant = 1.30;

        if (level == -1)
            cost = consistentHalf * Math.pow((costGrowthConstant - (orderPosition * costGrowthMultiplier)), currentLevel);
        else
            cost = consistentHalf * Math.pow((costGrowthConstant - (orderPosition * costGrowthMultiplier)), level);

        if (multiplier != 0)
            cost -= Math.abs(cost * multiplier) - Math.abs(cost);

        return cost;
    }

    private double getIncome(int level) {
        double incomeGrowthMultiplier = 0.0025;
        double incomeGrowthConstant = 1.05;
        double incomeExponent = 1.5;
        double incomeBase = 15.0;
        double incomeConstant = 4.0;

        double totalIncome = incomeConstant + incomeBase * Math.pow(orderPosition, incomeExponent) * Math.pow((incomeGrowthConstant + (orderPosition * incomeGrowthMultiplier)), level);
        double heartsMultiplier = Talents.heartBeater.getCurrentBonus();
        double packMultiplier = DataManager.getInstance().getTotalPackMultiplier();

        if (packMultiplier != 0)
            totalIncome *= packMultiplier;

        if (heartsMultiplier != 0)
            totalIncome *= heartsMultiplier;

        return totalIncome;
    }

    public double getCurrentIncome() {
        return getIncome(currentLevel);
    }

    public double getNextIncome() {
        return getIncome(currentLevel + 1);
    }

    public double getBaseHeartTokensPerSecond() { return baseHeartTokensPerSecond; }

    public void buyMaxLevels() {
        int level = currentLevel;
        double currentHearts;
        double nextCost;
        boolean canAfford = true;

        while (canAfford) {
            currentHearts = DataManager.getInstance().getPlayerData().getCurrentHearts();
            nextCost = getUpgradeCost(level);

            if (currentHearts < nextCost)
                canAfford = false;
            else {
                DataManager.getInstance().addHearts(-nextCost);
                level++;
            }
        }
        currentLevel = level;
        DataManager.getInstance().getFiles().getDataFile().setHasNewChanges(true);
    }
}
