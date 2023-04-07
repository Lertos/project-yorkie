package com.lertos.projectyorkie;

import android.os.Handler;

import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.Talents;

public class PettingMaster {

    private final double puppyPower;
    private final double secondsLostWhenMissed;
    private final double secondsGainedWhenCorrect;
    private final int startThreshold;
    private final int squaresPerThreshold = 10;
    private double endReward = 0;
    private double currentSquareDisappearTime;
    private int currentSquareNumber;
    private int currentThreshold;
    private final double timerStartValue = 30.0;
    private double currentTimeLeft;
    private boolean isActive = true;
    private final int millisecondsPerUpdate = 100;

    public PettingMaster() {
        double baseSecondsLostWhenMissed = 4;
        double baseSecondsGainedWhenCorrect = 1;

        this.puppyPower = Talents.pettingPower.getCurrentBonus();
        this.secondsLostWhenMissed = baseSecondsLostWhenMissed - Talents.laxTreatment.getCurrentBonus();
        this.secondsGainedWhenCorrect = baseSecondsGainedWhenCorrect + Talents.pupPrecision.getCurrentBonus();
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

                if (!isActive)
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

        endReward = getHeartsReward();
        DataManager.getInstance().addHearts(endReward);
    }

    public int getStartThreshold() {
        return startThreshold;
    }

    private int setStartThreshold() {
        int currentThreshold = DataManager.getInstance().getPlayerData().getPettingHighestThreshold();
        currentThreshold -= squaresPerThreshold;

        return Math.max(1, currentThreshold);
    }

    public double getStartCost() {
        int costHeartTokensPerThreshold = 700;
        double cost = Math.max(costHeartTokensPerThreshold, costHeartTokensPerThreshold * startThreshold);
        double multiplier = Talents.purrsuasion.getCurrentBonus();

        if (multiplier != 0)
            cost -= Math.abs(cost * multiplier) - Math.abs(cost);

        return cost;
    }

    public void handleClickedSquare() {
        currentTimeLeft += secondsGainedWhenCorrect;

        if (currentTimeLeft > timerStartValue)
            currentTimeLeft = timerStartValue;

        //To handle the thresholds
        if (currentSquareNumber % squaresPerThreshold == 0)
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
        return currentTimeLeft;
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

    private double calculateNextDisappearTime() {
        double baseDisappearTime = 4.5;
        return (puppyPower + baseDisappearTime) / (double) currentSquareNumber;
    }

    public double getHeartsReward() {
        double baseRewardMultiplier = 50.0;
        double rewardMultiplierPerThreshold = 15.5;

        double heartsPerSecond = DataManager.getInstance().getPlayerData().getCurrentHeartsPerSecond();
        double rewardMultiplier = baseRewardMultiplier;

        //Apply the reward bonus for the amount of thresholds
        rewardMultiplier += currentThreshold * rewardMultiplierPerThreshold;

        //If there are leftover squares
        if (currentSquareNumber % squaresPerThreshold != 0) {
            int remainingSquares = currentSquareNumber % squaresPerThreshold;
            double rewardPerSquare = rewardMultiplierPerThreshold / squaresPerThreshold;

            rewardMultiplier += remainingSquares * rewardPerSquare;
        }

        return heartsPerSecond * rewardMultiplier;
    }

    public double getEndReward() {
        return endReward;
    }
}
