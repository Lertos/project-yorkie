package com.lertos.projectyorkie.model;

import com.lertos.projectyorkie.data.DataManager;

public class PackDog {

    private final String name;
    private final int avatar;
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
        return 0.3;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;

        DataManager.getInstance().getFiles().getDataFile().setHasNewChanges(true);
    }
}
