package com.lertos.projectyorkie.model;

import com.lertos.projectyorkie.data.DataManager;

public class PackDog {

    private final String name;
    private final int avatar;
    private final double addedBonus = 0.3;
    private boolean unlocked = false;

    public PackDog(String name, int avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public int getAvatar() {
        return avatar;
    }

    public double getAddedBonus() {
        return addedBonus;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;

        DataManager.getInstance().getFiles().getDataFile().setHasNewChanges(true);
    }
}
