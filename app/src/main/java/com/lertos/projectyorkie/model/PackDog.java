package com.lertos.projectyorkie.model;

public class PackDog {

    private final String name;
    private final String description;
    private final int avatar;
    private final double addedBonus = 1.05;

    public PackDog(String name, String description, int avatar) {
        this.name = name;
        this.description = description;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getAvatar() {
        return avatar;
    }

    public double getAddedBonus() {
        return addedBonus;
    }
}
