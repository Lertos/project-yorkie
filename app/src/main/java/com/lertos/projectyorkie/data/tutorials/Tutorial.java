package com.lertos.projectyorkie.data.tutorials;

public class Tutorial {

    private final String matchingClassName;
    private final int layoutId;
    boolean hasPlayerSeen;

    public Tutorial(String gameClassName, int layoutId, boolean hasPlayerSeen) {
        this.matchingClassName = gameClassName;
        this.layoutId = layoutId;
        this.hasPlayerSeen = hasPlayerSeen;
    }

    public String getMatchingClassName() {
        return matchingClassName;
    }

    public boolean hasPlayerSeen() {
        return hasPlayerSeen;
    }

    public void setHasPlayerSeen(boolean hasPlayerSeen) {
        this.hasPlayerSeen = hasPlayerSeen;
    }

    public int getLayoutId() {
        return layoutId;
    }

}
