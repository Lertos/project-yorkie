package com.lertos.projectyorkie.model;

public enum TournamentDivision {
    WOOD,
    IRON,
    BRONZE,
    SILVER,
    GOLD,
    PLATINUM,
    DIAMOND,
    EMERALD;

    private String displayStr;

    static {
        WOOD.displayStr = "Wood";
        IRON.displayStr = "Iron";
        BRONZE.displayStr = "Bronze";
        SILVER.displayStr = "Silver";
        GOLD.displayStr = "Gold";
        PLATINUM.displayStr = "Platinum";
        DIAMOND.displayStr = "Diamond";
        EMERALD.displayStr = "Emerald";
    }

    public String getDisplayStr() {
        return displayStr;
    }

    public TournamentDivision getPreviousDivision() {
        if (this.equals(EMERALD))
            return DIAMOND;
        else if (this.equals(DIAMOND))
            return PLATINUM;
        else if (this.equals(PLATINUM))
            return GOLD;
        else if (this.equals(GOLD))
            return SILVER;
        else if (this.equals(SILVER))
            return BRONZE;
        else if (this.equals(BRONZE))
            return IRON;
        else if (this.equals(IRON))
            return WOOD;
        else
            return null;
    }

    public TournamentDivision getNextDivision() {
        if (this.equals(WOOD))
            return IRON;
        else if (this.equals(IRON))
            return BRONZE;
        else if (this.equals(BRONZE))
            return SILVER;
        else if (this.equals(SILVER))
            return GOLD;
        else if (this.equals(GOLD))
            return PLATINUM;
        else if (this.equals(PLATINUM))
            return DIAMOND;
        else if (this.equals(DIAMOND))
            return EMERALD;
        else
            return null;
    }
}
