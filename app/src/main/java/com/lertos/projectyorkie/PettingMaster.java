package com.lertos.projectyorkie;

public class PettingMaster {

    private final int puppyPower;
    private final double secondsLostWhenMissed;
    private final double secondsGainedWhenCorrect;
    private final int startThreshold;

    private final double rewardMultiplier = 1.5;
    private final double baseDisappearTime = 4.5;

    private Square currentSquare;
    private double currentTimeLeft;

    public PettingMaster(int puppyPower, double secondsLostWhenMissed, double secondsGainedWhenCorrect, int startThreshold) {
        this.puppyPower = puppyPower;
        this.secondsLostWhenMissed = secondsLostWhenMissed;
        this.secondsGainedWhenCorrect = secondsGainedWhenCorrect;
        this.startThreshold = startThreshold;
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
