package com.lertos.projectyorkie.tournament.games;

import android.graphics.Rect;
import android.os.Handler;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.Talents;
import com.lertos.projectyorkie.tournament.TournamentDifficulty;
import com.lertos.projectyorkie.tournament.TournamentGame;
import com.lertos.projectyorkie.tournament.TournamentMaster;

import java.util.ArrayList;

public class DodgeTheCats extends TournamentGame {

    private final Handler disappearTimeHandler = new Handler();
    private final double secondsLostWhenMissed = 4;
    private final double secondsGainedWhenCorrect = 1;
    private final double baseDisappearTime = 3.5;
    private final double scorePerDodge = 50;
    private final int initialSquareDisappearTime;
    private Runnable disappearTimeRunnable;
    private ImageView ivCatAvatar;
    private ImageView ivYorkieAvatar;
    private ArrayList<ImageView> fallingCats;
    private int laneX1;
    private int laneX2;
    private int laneX3;
    private int laneWidth;
    private int headerHeight;
    private int sectionHeight;
    private int timeToFall;
    private int timeToScaleUp;
    private int timeBetweenCats;
    private int currentSquareDisappearTime;
    //Starting at 2 so the math works better
    private int currentSquare = 2;

    public DodgeTheCats(TournamentMaster tournamentMaster, TournamentDifficulty difficulty, AppCompatActivity view, String gameTitle, String gameHint) {
        super(tournamentMaster, difficulty, view, gameTitle, gameHint);

        initialSquareDisappearTime = calculateInitialDisappearTime();
        currentSquareDisappearTime = initialSquareDisappearTime;
    }

    protected void setupUI() {
        //Need the layout to be inflated before doing math using the variables produced inside this block
        RelativeLayout layout = (RelativeLayout) parentView.findViewById(R.id.relMainSection);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                layout.getGlobalVisibleRect(gameLayout);

                ivCatAvatar = parentView.findViewById(R.id.ivCatToClone);

                fallingCats = new ArrayList<>();

                laneX1 = (int) parentView.findViewById(R.id.ivLaneSpace1).getX();
                laneX2 = (int) parentView.findViewById(R.id.ivLaneSpace2).getX();
                laneX3 = (int) parentView.findViewById(R.id.ivLaneSpace3).getX();

                laneWidth = parentView.findViewById(R.id.ivLaneSpace1).getWidth();

                //Get the game screen bounds
                Rect headerLayout = new Rect();
                parentView.findViewById(R.id.linHeader).getGlobalVisibleRect(headerLayout);

                headerHeight = headerLayout.height();
                sectionHeight = gameLayout.height() - headerHeight;

                //These methods require the variables assigned up above so they need to be in this block
                setTimingOfMovements();
                setupOnClickListeners();
            }
        });
    }

    private void setTimingOfMovements() {
        timeToFall = 0;

        switch (tournamentDifficulty) {
            case EASY:
                timeToFall = 1200;
                break;
            case NORMAL:
                timeToFall = 1000;
                break;
            case HARD:
                timeToFall = 800;
                break;
        }

        timeToScaleUp = 0;

        switch (tournamentDifficulty) {
            case EASY:
                timeToScaleUp = 300;
                break;
            case NORMAL:
                timeToScaleUp = 200;
                break;
            case HARD:
                timeToScaleUp = 100;
                break;
        }
    }

    private void setupOnClickListeners() {
        //TODO: Setup a motion listener to see which direction the user swipes; move the character that direction
    }

    protected void gameLoop() {
        disappearTimeRunnable = () -> {
            if (!isPlaying)
                return;

            //TODO
            //Need to send "waves" at them. Have a limit of how many cats per wave. Each wave uses the same speed.
            //When they are all off the screen, the next wave comes in and has a faster speed.
            //The speed at which they fall gets smaller, as well as the space between them spawning.

            disappearTimeHandler.removeCallbacks(disappearTimeRunnable);
            disappearTimeHandler.postDelayed(disappearTimeRunnable, currentSquareDisappearTime);
        };
        disappearTimeHandler.post(disappearTimeRunnable);
    }

    private void addScore() {
        score += scorePerDodge * Talents.cutenessFactor.getCurrentBonus();
    }

    protected int getAverageScore() {
        double score = 0;

        switch (tournamentDifficulty) {
            case EASY:
                score = scorePerDodge * 12;
                break;
            case NORMAL:
                score = scorePerDodge * 16;
                break;
            case HARD:
                score = scorePerDodge * 20;
                break;
        }
        return (int) Math.round(score);
    }

    private int calculateInitialDisappearTime() {
        int tournamentRankValue = DataManager.getInstance().getPlayerData().getTournamentRank().getRankValue();
        double timeInSeconds = (canineFocus + baseDisappearTime) / tournamentRankValue;
        int timeInMilliseconds = (int) Math.round(timeInSeconds * 1000);

        return timeInMilliseconds;
    }

    private void setNextDisappearTime() {
        currentSquareDisappearTime = (int) Math.floor(initialSquareDisappearTime / (currentSquare / 2.0));
    }

}