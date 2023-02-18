package com.lertos.projectyorkie;

import android.os.Handler;
import android.util.Log;

import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.Talents;

public class PettingMaster {

    private final double puppyPower;
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

    public PettingMaster() {
        this.puppyPower = Talents.pettingPower.getCurrentBonus();
        this.secondsLostWhenMissed = 4 - Talents.laxTreatment.getCurrentBonus();
        this.secondsGainedWhenCorrect = 1 + Talents.pupPrecision.getCurrentBonus();
        //To make sure no matter what the minimum is always 1
        this.startThreshold = setStartThreshold();

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

    private double setSecondsLostWhenMissed(double base) {
        return base - Talents.laxTreatment.getCurrentBonus();
    }


    private double setSecondsGainedWhenCorrect(double base) {
        return base + Talents.pupPrecision.getCurrentBonus();
    }

    private int setStartThreshold() {
        int currentThreshold = DataManager.getInstance().getPlayerData().getPettingHighestThreshold();
        currentThreshold -= 10;

        return Math.max(1, currentThreshold);
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
