package com.lertos.projectyorkie;

import android.os.Handler;

public class PettingMaster {

    private final int puppyPower;
    private final double secondsLostWhenMissed;
    private final double secondsGainedWhenCorrect;
    private final int startThreshold;

    private final double rewardMultiplier = 1.5;
    private final double baseDisappearTime = 4.5;

    private Square currentSquare;
    private int currentSquarePosition;
    private final double timerStartValue = 30.0;
    private double currentTimeLeft;
    private boolean isActive = true;
    private int millisecondsPerUpdate = 100;

    public PettingMaster(int puppyPower, double secondsLostWhenMissed, double secondsGainedWhenCorrect, int startThreshold) {
        this.puppyPower = puppyPower;
        this.secondsLostWhenMissed = secondsLostWhenMissed;
        this.secondsGainedWhenCorrect = secondsGainedWhenCorrect;
        this.startThreshold = startThreshold;

        currentSquarePosition = startThreshold;
        currentTimeLeft = timerStartValue;
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
        this.isActive = false;
    }

    public void handleClickedSquare() {
        //TODO: Create a new Square and reassign the current square variable to the new one
        if (currentTimeLeft + secondsGainedWhenCorrect > timerStartValue)
            currentTimeLeft = timerStartValue;
        else
            currentTimeLeft += secondsGainedWhenCorrect;

        currentSquarePosition++;
    }

    public Square getCurrentSquare() {
        return currentSquare;
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

    private class Square {

        private final int squareNumber;
        private final double disappearTime;

        public Square(int squareNumber, double disappearTime) {
            this.squareNumber = squareNumber;
            this.disappearTime = disappearTime;
        }

        public int getSquareNumber() {
            return squareNumber;
        }

        public double getDisappearTime() {
            return disappearTime;
        }
    }

}
