package com.lertos.projectyorkie.data;

public class Tutorial {

    private final String matchingClassName;
    private final int layoutId;
    private final int viewStubId;
    boolean hasPlayerSeen;

    public Tutorial(String gameClassName, int layoutId, int viewStubId, boolean hasPlayerSeen) {
        this.matchingClassName = gameClassName;
        this.layoutId = layoutId;
        this.viewStubId = viewStubId;
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

    public int getViewStubId() {
        return viewStubId;
    }

}
