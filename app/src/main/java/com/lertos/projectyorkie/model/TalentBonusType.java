package com.lertos.projectyorkie.model;

public enum TalentBonusType {
    FLAT,
    PERCENTAGE;

    private String suffix;

    static {
        FLAT.suffix = "";
        PERCENTAGE.suffix = "%";
    }

    public String getSuffix() {
        return suffix;
    }
}
