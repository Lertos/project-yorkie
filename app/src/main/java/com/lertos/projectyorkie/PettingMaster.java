package com.lertos.projectyorkie;

import android.os.Handler;

import com.lertos.projectyorkie.data.DataManager;

public class PettingMaster {

    private final int puppyPower;
    private final double secondsLostWhenMissed;
    private final double secondsGainedWhenCorrect;
    private final int startThreshold;

    private final double rewardMultiplier = 1.5;
    private final double baseDisappearTime = 4.5;

    private double currentSquareDisappearTime;
    private int currentSquareNumber;
    private int currentThreshold;
    private final double timerStartValue = 30.0;
    private double currentTimeLeft;
    private boolean isActive = true;
    private int millisecondsPerUpdate = 100;

    public PettingMaster(int puppyPower, double secondsLostWhenMissed, double secondsGainedWhenCorrect) {
        this.puppyPower = puppyPower;
        this.secondsLostWhenMissed = secondsLostWhenMissed;
        this.secondsGainedWhenCorrect = secondsGainedWhenCorrect;
        //To make sure no matter what the minimum is always 1
        this.startThreshold = Math.max(1, DataManager.getInstance().getPlayerData().getPettingHighestThreshold() - 10);

        this.currentSquareNumber = startThreshold;
        this.currentSquareDisappearTime = calculateNextDisappearTime();
        this.currentTimeLeft = timerStartValue;
    }

    public void start() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                double convertedToSeconds = millisecondsPerUpdate / 1000.0;

                if (currentTimeLeft - convertedToSeconds < 0) {
                    currentTimeLeft = 0;
                    isActive = false;
                } else {
                    currentTimeLeft -= convertedToSeconds;
                }

                if(!isActive)
                    handler.removeCallbacks(this);
                else
                    handler.postDelayed(this, millisecondsPerUpdate);
            }
        };
        handler.post(runnable);
    }

    public void stop() {
        DataManager.getInstance().getPlayerData().setPettingHighestThreshold(currentThreshold);
        DataManager.getInstance().getPlayerData().setPettingHighestSquare(currentSquareNumber);
    }

    public void handleClickedSquare() {
        currentTimeLeft += secondsGainedWhenCorrect;

        if (currentTimeLeft > timerStartValue)
            currentTimeLeft = timerStartValue;

        //To handle the thresholds
        if (currentSquareNumber % 10 == 0)
            currentThreshold = currentSquareNumber;

        currentSquareNumber++;
        currentSquareDisappearTime = calculateNextDisappearTime();
    }

    public void handleMissedSquare() {
        currentTimeLeft -= secondsLostWhenMissed;

        if (currentTimeLeft < 0) {
            currentTimeLeft = 0;
            isActive = false;
        }
    }

    public double getCurrentTimeLeft() {
        return Helper.roundNumber(currentTimeLeft);
    }

    public double getTimerStartValue() {
        return timerStartValue;
    }

    public int getMillisecondsPerUpdate() {
        return millisecondsPerUpdate;
    }

    public double getCurrentSquareDisappearTime() {
        return currentSquareDisappearTime;
    }

    public int getCurrentSquareNumber() {
        return currentSquareNumber;
    }

    private double calculateNextDisappearTime() {
        return Helper.roundNumber((puppyPower + baseDisappearTime) / (double) currentSquareNumber);
    }
}
