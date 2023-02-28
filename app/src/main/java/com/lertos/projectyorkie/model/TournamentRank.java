package com.lertos.projectyorkie.model;

public class TournamentRank {

    //Tiers go from V > IV > III > II > I for example,
    private final int startTier = 5;
    private final int maxTier = 1;
    private TournamentDivision division;
    private TournamentDivision defaultDivision = TournamentDivision.WOOD;
    private int tier;
    private int defaultTier = startTier;

    public TournamentRank() {
        this.division = defaultDivision;
        this.tier = defaultTier;
    }

    public TournamentDivision getDivision() {
        return division;
    }

    public int getTier() {
        return tier;
    }

    public void setDivision(TournamentDivision division) {
        if (division != null)
            this.division = division;
    }

    public void setTier(int tier) {
        if (tier > maxTier && tier < startTier)
            this.tier = tier;
    }

    public double getTokenCostForRank() {
        double heartTokens;

        switch (division) {
            case WOOD: heartTokens = 10;
            case IRON: heartTokens = 165;
            case BRONZE: heartTokens = 660;
            case SILVER: heartTokens = 1485;
            case GOLD: heartTokens = 2640;
            case PLATINUM: heartTokens = 4125;
            case DIAMOND: heartTokens = 5940;
            case EMERALD: heartTokens = 8085;
            default: heartTokens = 0;
        }
        heartTokens += heartTokens / (double) tier;

        return heartTokens;
    }

    //Returns true if they move up a division, false otherwise
    public boolean increaseTier() {
        tier--;

        if (tier < maxTier) {
            TournamentDivision newDivision = division.getNextDivision();

            //If they are at the max division - there is nothing to do; reset to default
            if (newDivision == null) {
                tier = maxTier;
                return false;
            }
            //If they are not at max division
            else {
                division = newDivision;
                tier = startTier;
                return true;
            }
        }
        return false;
    }

    //Returns true if they move down a division, false otherwise
    public boolean decreaseTier() {
        tier++;

        if (tier > startTier) {
            TournamentDivision newDivision = division.getPreviousDivision();

            //If they are at the bottom division - there is nothing to do; reset to default
            if (newDivision == null) {
                tier = startTier;
                return false;
            }
            //If they are not at the bottom division
            else {
                division = newDivision;
                tier = maxTier;
                return true;
            }
        }
        return false;
    }

    public String getRankDisplay() {
        StringBuilder sb = new StringBuilder(division.getDisplayStr());
        sb.append(" ");
        sb.append(getRomanNumeral(tier));

        return sb.toString();
    }

    private String getRomanNumeral(int number) {
        if (number == 1)
            return "I";
        else if (number == 2)
            return "II";
        else if (number == 3)
            return "III";
        else if (number == 4)
            return "IV";
        else if (number == 5)
            return "V";
        return "NaN";
    }

}
