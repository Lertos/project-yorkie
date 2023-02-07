package com.lertos.projectyorkie.data;

import android.graphics.Color;

public class Player {

    //Formatting/colorization purposes
    private final int highlightColor = Color.YELLOW;

    //Player data that will change
    private double currentHeartsPerSecond;
    private double currentHearts;
    private int currentHeartTokens;
    private int dogsCollected;

    //Static data that won't change
    private final int maxDogsToCollect = 27;

    public Player(double currentHearts, int currentHeartTokens, int dogsCollected) {
        this.currentHearts = currentHearts;
        this.currentHeartTokens = currentHeartTokens;
        this.dogsCollected = dogsCollected;
    }

    public double getCurrentHeartsPerSecond() {
        return currentHeartsPerSecond;
    }

    public void setCurrentHeartsPerSecond(double currentHeartsPerSecond) {
        this.currentHeartsPerSecond = currentHeartsPerSecond;
    }

    public double getCurrentHearts() {
        return currentHearts;
    }

    public void setCurrentHearts(double currentHearts) {
        this.currentHearts = currentHearts;
    }

    public int getCurrentHeartTokens() {
        return currentHeartTokens;
    }

    public void setCurrentHeartTokens(int currentHeartTokens) {
        this.currentHeartTokens = currentHeartTokens;
    }

    public int getDogsCollected() {
        return dogsCollected;
    }

    public void setDogsCollected(int dogsCollected) {
        this.dogsCollected = dogsCollected;
    }

    public int getHighlightColor() {
        return highlightColor;
    }

    public int getMaxDogsToCollect() {
        return maxDogsToCollect;
    }
}
