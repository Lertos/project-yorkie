package com.lertos.projectyorkie.tournament;

import android.graphics.Rect;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.Talents;

import java.util.Random;

public abstract class TournamentGame {

    protected Random rng = new Random();
    private final String gameTitle;
    private final String gameHint;
    private final TournamentMaster tournamentMaster;
    protected TournamentDifficulty tournamentDifficulty;
    protected AppCompatActivity parentView;
    protected boolean isPlaying = false;
    protected double score = 0;
    protected final double canineFocus;
    protected LinearProgressIndicator indicator;
    protected TextView tvScore;
    protected Rect gameLayout = new Rect();
    protected final int millisecondsPerUpdate = 100;
    protected final int timerMax = 1000;
    protected final double startTime = 30.0;
    protected double currentTime;

    public TournamentGame(TournamentMaster tournamentMaster, TournamentDifficulty difficulty, AppCompatActivity view, String gameTitle, String gameHint) {
        this.tournamentMaster = tournamentMaster;
        this.tournamentDifficulty = difficulty;
        this.parentView = view;
        this.gameTitle = gameTitle;
        this.gameHint = gameHint;

        canineFocus = Talents.canineFocus.getCurrentBonus();

        currentTime = startTime;
    }

    protected abstract void gameLoop();

    protected abstract void setupUI();

    protected abstract int getAverageScore();

    //Runs the game and when it's over, returns the score
    public void startGame() {
        //This makes sure the progress moves smoothly. 100 max makes it decrease in a choppy manner
        indicator = parentView.findViewById(R.id.indTimer);
        indicator.setMax(timerMax);

        //Set the description of the game in the header
        ((TextView) parentView.findViewById(R.id.tvGameDescription)).setText(gameHint);

        tvScore = parentView.findViewById(R.id.tvScore);
        tvScore.setText(String.valueOf(Math.round(score)));

        setupUI();

        //Show the shared header that holds the timer and score
        parentView.findViewById(R.id.linGameHeader).setVisibility(View.VISIBLE);

        isPlaying = true;

        gameLoop();
        handleTimer();
    }

    protected void addScore(double scorePerAction) {
        score += scorePerAction * Talents.cutenessFactor.getCurrentBonus();
        tvScore.setText(String.valueOf(Math.round(score)));
    }

    protected void addTimeToTimer(double timeToAdd) {
        if (timeToAdd > 0)
            currentTime = Math.min(currentTime + timeToAdd, startTime);
        else
            currentTime += timeToAdd;
    }

    protected void handleTimer() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //Handle the indicators shown time
                int calculatedProgress = (int) Math.round((currentTime / startTime) * timerMax);

                indicator.setProgressCompat(calculatedProgress, false);

                if (currentTime <= 0)
                    isPlaying = false;

                if (!isPlaying) {
                    tournamentMaster.showEndGameScreen();
                    return;
                }

                //Calculate the new time
                currentTime -= millisecondsPerUpdate / 1000.0;

                handler.postDelayed(this, millisecondsPerUpdate);
            }
        };
        handler.post(runnable);
    }

    protected int pixelValue(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, parentView.getResources().getDisplayMetrics());
    }

    public String getGameTitle() {
        return gameTitle;
    }
}
