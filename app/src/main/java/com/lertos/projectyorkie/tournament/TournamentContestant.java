package com.lertos.projectyorkie.tournament;

import com.lertos.projectyorkie.model.PackDog;

public class TournamentContestant {

    private final PackDog packDog;
    private double currentScore;
    private double totalScore;
    private final boolean isPlayer;

    public TournamentContestant(PackDog packDog, boolean isPlayer) {
        this.packDog = packDog;
        this.currentScore = 0;
        this.totalScore = 0;
        this.isPlayer = isPlayer;
    }

    public PackDog getPackDog() {
        return packDog;
    }

    public int getCurrentScore() {
        return (int) Math.round(currentScore);
    }

    public int getTotalScore() {
        return (int) Math.round(totalScore);
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public void resetCurrentScore() {
        currentScore = 0;
    }

    public double addToCurrentScore(double scoreToAdd) {
        currentScore += scoreToAdd;

        if (currentScore < 0)
            currentScore = 0;

        totalScore += currentScore;

        return currentScore;
    }
}
