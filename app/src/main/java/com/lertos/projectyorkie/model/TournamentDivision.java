package com.lertos.projectyorkie.model;

public enum TournamentDivision {
    BRONZE,
    SILVER,
    GOLD,
    PLATINUM,
    DIAMOND;

    private String displayStr;

    static {
        BRONZE.displayStr = "Bronze";
        SILVER.displayStr = "Silver";
        GOLD.displayStr = "Gold";
        PLATINUM.displayStr = "Platinum";
        DIAMOND.displayStr = "Diamond";
    }

    public String getDisplayStr() {
        return displayStr;
    }
}
