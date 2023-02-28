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

}
