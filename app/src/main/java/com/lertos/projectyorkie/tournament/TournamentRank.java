package com.lertos.projectyorkie.tournament;

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
            case WOOD:
                heartTokens = 11250;
                break;
            case IRON:
                heartTokens = 22500;
                break;
            case BRONZE:
                heartTokens = 33750;
                break;
            case SILVER:
                heartTokens = 45000;
                break;
            case GOLD:
                heartTokens = 56250;
                break;
            case PLATINUM:
                heartTokens = 67500;
                break;
            case DIAMOND:
                heartTokens = 78750;
                break;
            case EMERALD:
                heartTokens = 90000;
                break;
            default:
                heartTokens = 0;
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

    //To get a numerical, sequential number of the rank. 1 would be WOOD 5, and 40 would be EMERALD 1 for example
    //Each tier counts as 1
    //Each division counts as the division # MULTIPLIED by the amount of tiers per division
    public int getRankValue() {
        int rankNumber = -1;

        //Get the base number
        switch (division) {
            case WOOD: rankNumber = 0; break;
            case IRON: rankNumber = 1; break;
            case BRONZE: rankNumber = 2; break;
            case SILVER: rankNumber = 3; break;
            case GOLD: rankNumber = 4; break;
            case PLATINUM: rankNumber = 5; break;
            case DIAMOND: rankNumber = 6; break;
            case EMERALD: rankNumber = 7; break;
        }

        //Get the division value based on how many tiers they would have gone through
        rankNumber *= startTier;

        //Get the tier remaining value
        rankNumber += (startTier + 1) - tier;

        return rankNumber;
    }

}
