package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.data.file.FilePlayerKeys;
import com.lertos.projectyorkie.tournament.TournamentRank;

public class Player {

    //Formatting/colorization purposes
    private int highlightColor;

    //Player data that will change
    private double currentHeartsPerSecond;
    private double currentHeartTokensPerSecond;
    private double currentHearts;
    private double currentHeartTokens;
    private int pettingHighestThreshold;
    private int pettingHighestSquare;
    private TournamentRank tournamentRank;

    public Player(double currentHearts, double currentHeartTokens) {
        this.currentHearts = currentHearts;
        this.currentHeartTokens = currentHeartTokens;
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

    public int getPettingHighestThreshold() {
        return pettingHighestThreshold;
    }

    public void setPettingHighestThreshold(int pettingHighestThreshold) {
        if (pettingHighestThreshold > this.pettingHighestThreshold) {
            this.pettingHighestThreshold = pettingHighestThreshold;
            DataManager.getInstance().getFiles().getDataFile().setValue(FilePlayerKeys.DATA_HIGH_SCORE_THRESHOLD, pettingHighestThreshold);
        }
    }

    public int getPettingHighestSquare() {
        return pettingHighestSquare;
    }

    public void setPettingHighestSquare(int pettingHighestSquare) {
        if (pettingHighestSquare > this.pettingHighestSquare) {
            this.pettingHighestSquare = pettingHighestSquare;
            DataManager.getInstance().getFiles().getDataFile().setValue(FilePlayerKeys.DATA_HIGH_SCORE_SQUARE, pettingHighestSquare);
        }
    }
}
