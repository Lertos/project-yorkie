package com.lertos.projectyorkie.model;

public abstract class TournamentGame {

    boolean isPlaying = false;
    double score = 0;

    //Runs the game and when it's over, returns the score
    public double startGame() {
        //Setup UI, onClickListeners, start the timer, run the game loop
        setupOnClickListeners();

        isPlaying = true;

        gameLoop();
        return 0.0;
    }

    private double stopGame() {
        //Stop the timer

        return getScore();
    }

    private void setupOnClickListeners() {

    }

    private double getScore() {
        return score;
    }

    private void gameLoop() {
        //Run handler and constantly run it until game is over, then return
    }

}
