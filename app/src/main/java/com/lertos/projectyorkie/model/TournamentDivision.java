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

    public TournamentDivision getPreviousDivision() {
        if (this.equals(DIAMOND))
            return PLATINUM;
        else if (this.equals(PLATINUM))
            return GOLD;
        else if (this.equals(GOLD))
            return SILVER;
        else if (this.equals(SILVER))
            return BRONZE;
        else
            return null;
    }

    public TournamentDivision getNextDivision() {
        if (this.equals(BRONZE))
            return SILVER;
        else if (this.equals(SILVER))
            return GOLD;
        else if (this.equals(GOLD))
            return PLATINUM;
        else if (this.equals(PLATINUM))
            return DIAMOND;
        else
            return null;
    }
}
