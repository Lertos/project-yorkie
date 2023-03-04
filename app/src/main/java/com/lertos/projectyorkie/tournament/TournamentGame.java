package com.lertos.projectyorkie.tournament;

import android.util.TypedValue;
import android.view.View;

public abstract class TournamentGame {

    protected View parentView;
    protected boolean isPlaying = false;
    protected double score = 0;

    public TournamentGame(View view) {
        this.parentView = view;
    }

    protected abstract void gameLoop();

    //Runs the game and when it's over, returns the score
    public double startGame() {
        isPlaying = true;

        gameLoop();
        return score;
    }

    protected int pixelValue(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, parentView.getResources().getDisplayMetrics());
    }


}
