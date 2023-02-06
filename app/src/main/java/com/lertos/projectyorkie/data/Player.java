package com.lertos.projectyorkie.data;

import android.graphics.Color;

public class Player {

    //Formatting/colorization purposes
    private final int highlightColor = Color.YELLOW;

    //Player data that will change
    private int currentHearts;
    private int currentHeartTokens;
    private int dogsCollected;

    //Static data that won't change
    private final int maxDogsToCollect = 27;

    public Player(int currentHearts, int currentHeartTokens, int dogsCollected) {
        this.currentHearts = currentHearts;
        this.currentHeartTokens = currentHeartTokens;
        this.dogsCollected = dogsCollected;
    }

    public int getCurrentHearts() {
        return currentHearts;
    }

    public void setCurrentHearts(int currentHearts) {
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
