package com.lertos.projectyorkie.tournament;

import android.view.View;

public abstract class TournamentGame {

    protected View view;
    boolean isPlaying = false;
    double score = 0;

    public TournamentGame(View view) {
        this.view = view;
    }

    protected abstract void gameLoop();

    //Runs the game and when it's over, returns the score
    public double startGame() {
        isPlaying = true;

        gameLoop();
        return 0.0;
    }

    private double stopGame() {
        //Stop the timer

        return getScore();
    }

    private double getScore() {
        return score;
    }

}
