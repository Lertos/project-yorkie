package com.lertos.projectyorkie.tournament;

public enum TournamentDifficulty {

    EASY,
    NORMAL,
    HARD;

    private String displayStr;

    static {
        EASY.displayStr = "Easy";
        NORMAL.displayStr = "Normal";
        HARD.displayStr = "Hard";
    }

    public String getDisplayStr() {
        return displayStr;
    }

}
