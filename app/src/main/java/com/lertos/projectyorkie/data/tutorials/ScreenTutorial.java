package com.lertos.projectyorkie.data.tutorials;

public class ScreenTutorial extends Tutorial {

    private final int viewStubId;

    public ScreenTutorial(String className, int layoutId, int viewStubId, boolean hasPlayerSeen) {
        super(className, layoutId, hasPlayerSeen);
        this.viewStubId = viewStubId;
    }

    public int getViewStubId() {
        return viewStubId;
    }
}
