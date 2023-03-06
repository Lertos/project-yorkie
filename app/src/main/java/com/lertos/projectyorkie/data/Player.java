package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.tournament.TournamentRank;

public class Player {

    //Formatting/colorization purposes
    private int highlightColor;

    //Player data that will change
    private double currentHeartsPerSecond;
    private double currentHeartTokensPerSecond;
    private double currentHearts;
    private double currentHeartTokens;
    private int dogsCollected;
    private int pettingHighestThreshold;
    private int pettingHighestSquare;
    private TournamentRank tournamentRank;

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

    public TournamentRank getTournamentRank() {
        return tournamentRank;
    }

    public void setTournamentRank(TournamentRank tournamentRank) {
        this.tournamentRank = tournamentRank;
    }

    public int getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
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
