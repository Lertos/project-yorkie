package com.lertos.projectyorkie.tournament;

import android.os.Handler;
import android.util.TypedValue;
import android.view.View;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.Talents;

public abstract class TournamentGame {

    private final String gameTitle;
    private TournamentMaster tournamentMaster;
    protected TournamentDifficulty tournamentDifficulty;
    protected View parentView;
    protected boolean isPlaying = false;
    protected double score = 0;
    protected final double canineFocus;
    protected LinearProgressIndicator indicator;
    protected final int millisecondsPerUpdate = 100;
    protected final int timerMax = 1000;
    protected final double startTime = 30.0;
    protected double currentTime;

    public TournamentGame(TournamentMaster tournamentMaster, TournamentDifficulty difficulty, View view, String gameTitle) {
        this.tournamentMaster = tournamentMaster;
        this.tournamentDifficulty = difficulty;
        this.parentView = view;
        this.gameTitle = gameTitle;

        canineFocus = Talents.canineFocus.getCurrentBonus();

        currentTime = startTime;

        //This makes sure the progress moves smoothly. 100 max makes it decrease in a choppy manner
        indicator = parentView.findViewById(R.id.indTimer);
        indicator.setMax(timerMax);
    }

    protected abstract void gameLoop();
    protected abstract double getAverageScore();

    //Runs the game and when it's over, returns the score
    public void startGame() {
        isPlaying = true;

        gameLoop();
        handleTimer();
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
