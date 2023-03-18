package com.lertos.projectyorkie.data;

public class Tutorial {

    private final String className;
    private final int layoutId;
    boolean hasPlayerSeen;

    public Tutorial(String className, int layoutId, boolean hasPlayerSeen) {
        this.className = className;
        this.layoutId = layoutId;
        this.hasPlayerSeen = hasPlayerSeen;
    }

    public String getClassName() {
        return className;
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
