package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.data.file.FileTutorialsKeys;

public class Tutorial {

    private final String matchingClassName;
    private final int layoutId;
    private final int viewStubId;
    private boolean hasPlayerSeen;
    private final FileTutorialsKeys fileKey;

    public Tutorial(String gameClassName, int layoutId, int viewStubId, boolean hasPlayerSeen, FileTutorialsKeys fileKey) {
        this.matchingClassName = gameClassName;
        this.layoutId = layoutId;
        this.viewStubId = viewStubId;
        this.hasPlayerSeen = hasPlayerSeen;
        this.fileKey = fileKey;
    }

    public String getMatchingClassName() {
        return matchingClassName;
    }

    public boolean hasPlayerSeen() {
        return hasPlayerSeen;
    }

    public void setHasPlayerSeen(boolean hasPlayerSeen) {
        this.hasPlayerSeen = hasPlayerSeen;
        DataManager.getInstance().getFiles().getTutorialFile().setValue(fileKey, !hasPlayerSeen);
    }

    public int getLayoutId() {
        return layoutId;
    }

    public int getViewStubId() {
        return viewStubId;
    }

}
