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

    public TournamentDivision getPreviousDivision(TournamentDivision division) {
        if (division.equals(DIAMOND))
            return PLATINUM;
        else if (division.equals(PLATINUM))
            return GOLD;
        else if (division.equals(GOLD))
            return SILVER;
        else if (division.equals(SILVER))
            return BRONZE;
        else
            return null;
    }

    public TournamentDivision getNextDivision(TournamentDivision division) {
        if (division.equals(BRONZE))
            return SILVER;
        else if (division.equals(SILVER))
            return GOLD;
        else if (division.equals(GOLD))
            return PLATINUM;
        else if (division.equals(PLATINUM))
            return DIAMOND;
        else
            return null;
    }
}
