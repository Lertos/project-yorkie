package com.lertos.projectyorkie;

import android.os.Handler;

import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.Talents;

public class PettingMaster {

    private final double puppyPower;
    private final double baseSecondsLostWhenMissed = 4;
    private final double baseSecondsGainedWhenCorrect = 1;
    private final double secondsLostWhenMissed;
    private final double secondsGainedWhenCorrect;
    private final int startThreshold;
    private final int costHeartTokensPerThreshold = 15;
    private final int squaresPerThreshold = 10;
    private final double rewardMultiplier = 1.5;
    private final double baseDisappearTime = 4.5;
    private double endReward = 0;
    private double currentSquareDisappearTime;
    private int currentSquareNumber;
    private int currentThreshold;
    private final double timerStartValue = 30.0;
    private double currentTimeLeft;
    private boolean isActive = true;
    private int millisecondsPerUpdate = 100;

    public PettingMaster() {
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
        double cost = Math.max(costHeartTokensPerThreshold, costHeartTokensPerThreshold * (startThreshold / squaresPerThreshold));
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
        return (puppyPower + baseDisappearTime) / (double) currentSquareNumber;
    }

    public double getHeartsReward() {
        double heartsPerSecond = DataManager.getInstance().getPlayerData().getCurrentHeartsPerSecond();
        double heartTokensPerSecond = DataManager.getInstance().getPlayerData().getCurrentHeartTokensPerSecond();
        double heartsPerToken = heartsPerSecond / heartTokensPerSecond;
        double heartsReward;

        //Get the reward for threshold, the uncompleted threshold, find the difference, then find the reward for the highest square
        if (currentSquareNumber % squaresPerThreshold == 0)
            heartsReward = heartsPerToken * (costHeartTokensPerThreshold * currentThreshold);
        else {
            double thresholdReward = heartsPerToken * (costHeartTokensPerThreshold * (currentThreshold / squaresPerThreshold));
            double finishedSquares = currentSquareNumber - currentThreshold;
            double finishedSquaresRatio = finishedSquares / squaresPerThreshold;
            double rewardForRatio = heartsPerToken * finishedSquaresRatio;

            heartsReward = thresholdReward + rewardForRatio;
        }

        //Apply the multipliers
        double packMultiplier = DataManager.getInstance().getTotalPackMultiplier();

        if (packMultiplier != 0)
            heartsReward *= packMultiplier;

        return heartsReward * rewardMultiplier;
    }

    public double getEndReward() {
        return endReward;
    }
}
