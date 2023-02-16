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
    private final double timerStartValue = 30.0;
    private double currentTimeLeft;
    private boolean isActive = true;
    private int millisecondsPerUpdate = 50;

    public PettingMaster(int puppyPower, double secondsLostWhenMissed, double secondsGainedWhenCorrect, int startThreshold) {
        this.puppyPower = puppyPower;
        this.secondsLostWhenMissed = secondsLostWhenMissed;
        this.secondsGainedWhenCorrect = secondsGainedWhenCorrect;
        this.startThreshold = startThreshold;

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

    public Square getCurrentSquare() {
        return currentSquare;
    }

    public double getCurrentTimeLeft() {
        return currentTimeLeft;
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
