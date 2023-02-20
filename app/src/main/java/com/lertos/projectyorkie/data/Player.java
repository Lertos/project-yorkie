package com.lertos.projectyorkie.data;

import android.graphics.Color;

public class Player {

    //Formatting/colorization purposes
    private final int highlightColor = Color.YELLOW;

    //Player data that will change
    private double currentHeartsPerSecond;
    private double currentHeartTokensPerSecond;
    private double currentHearts;
    private double currentHeartTokens;
    private int dogsCollected;
    private int pettingHighestThreshold;
    private int pettingHighestSquare;

    //Static data that won't change
    private final int maxDogsToCollect = 27;

    public Player(double currentHearts, double currentHeartTokens, int dogsCollected) {
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

    public double getCurrentHeartTokensPerSecond() {
        return currentHeartTokensPerSecond;
    }

    public void setCurrentHeartTokensPerSecond(double currentHeartTokensPerSecond) {
        this.currentHeartTokensPerSecond = currentHeartTokensPerSecond;
    }

    public double getCurrentHearts() {
        return currentHearts;
    }

    public void setCurrentHearts(double currentHearts) {
        this.currentHearts = currentHearts;
    }

    public double getCurrentHeartTokens() {
        return currentHeartTokens;
    }

    public void setCurrentHeartTokens(double currentHeartTokens) {
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

    public int getPettingHighestThreshold() {
        return pettingHighestThreshold;
    }

    public void setPettingHighestThreshold(int pettingHighestThreshold) {
        if (pettingHighestThreshold > this.pettingHighestThreshold)
            this.pettingHighestThreshold = pettingHighestThreshold;
    }

    public int getPettingHighestSquare() {
        return pettingHighestSquare;
    }

    public void setPettingHighestSquare(int pettingHighestSquare) {
        if (pettingHighestSquare > this.pettingHighestSquare)
            this.pettingHighestSquare = pettingHighestSquare;
    }
}
