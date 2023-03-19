package com.lertos.projectyorkie.data;

public class Tutorial {

    private final String className;
    private final int layoutId;
    private final int viewStubId;
    boolean hasPlayerSeen;

    public Tutorial(String className, int layoutId, int viewStubId, boolean hasPlayerSeen) {
        this.className = className;
        this.layoutId = layoutId;
        this.viewStubId = viewStubId;
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

    public int getViewStubId() {
        return viewStubId;
    }
}
