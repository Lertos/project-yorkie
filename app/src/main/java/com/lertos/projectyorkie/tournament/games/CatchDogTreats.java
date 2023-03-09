package com.lertos.projectyorkie.tournament.games;

import android.graphics.Rect;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.MediaManager;
import com.lertos.projectyorkie.data.Talents;
import com.lertos.projectyorkie.tournament.TournamentDifficulty;
import com.lertos.projectyorkie.tournament.TournamentGame;
import com.lertos.projectyorkie.tournament.TournamentMaster;

import java.util.ArrayList;

public class CatchDogTreats extends TournamentGame {

    private final Handler disappearTimeHandler = new Handler();
    private final double secondsLostWhenMissed = 4;
    private final double secondsGainedWhenCorrect = 1;
    private final double baseDisappearTime = 3.5;
    private final double scorePerClick = 50;
    private final int initialSquareDisappearTime;
    private Runnable disappearTimeRunnable;
    private ImageView mainSquare;
    private ArrayList<ImageView> fallingSquares;
    private ArrayList<ImageView> dropSquares;
    private int headerHeight;
    private int sectionHeight;
    private int timeToFall;
    private int timeToScaleUp;
    private int currentSquareDisappearTime;
    //Starting at 2 so the math works better
    private int currentSquare = 2;

    public CatchDogTreats(TournamentMaster tournamentMaster, TournamentDifficulty difficulty, AppCompatActivity view, String gameTitle, String gameHint) {
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

                mainSquare = parentView.findViewById(R.id.ivActiveSquare);

                fallingSquares = new ArrayList<>();
                dropSquares = new ArrayList<>();

                dropSquares.add(parentView.findViewById(R.id.ivStopSquare1));
                dropSquares.add(parentView.findViewById(R.id.ivStopSquare2));
                dropSquares.add(parentView.findViewById(R.id.ivStopSquare3));
                dropSquares.add(parentView.findViewById(R.id.ivStopSquare4));

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
        for (ImageView dropSquare : dropSquares) {
            Rect dropSquareRect = new Rect();
            dropSquare.getGlobalVisibleRect(dropSquareRect);

            dropSquare.setOnClickListener(v -> {
                boolean correctClick = false;
                ImageView clickedSquare = null;

                for (ImageView fallingSquare : fallingSquares) {
                    Rect fallingSquareRect = new Rect();
                    fallingSquare.getGlobalVisibleRect(fallingSquareRect);

                    boolean insideX = fallingSquareRect.left > dropSquareRect.left && fallingSquareRect.right < dropSquareRect.right;
                    boolean insideYAbove = fallingSquareRect.top > (dropSquareRect.top - fallingSquareRect.height());
                    boolean insideYBelow = fallingSquareRect.bottom < (dropSquareRect.bottom + fallingSquareRect.height());

                    if (insideX) {
                        if (insideYAbove && insideYBelow) {
                            correctClick = true;
                            clickedSquare = fallingSquare;
                            break;
                        }
                    }
                }

                if (correctClick)
                    handleSquareClicked(clickedSquare);
                else
                    handleWrongClick();
            });
        }
    }

    private void handleSquareClicked(ImageView fallingSquare) {
        currentTime += secondsGainedWhenCorrect;
        currentSquare++;

        if (isPlaying)
            MediaManager.getInstance().playEffectTrack(R.raw.effect_correct);

        setNextDisappearTime();
        addScore();

        fallingSquares.remove(fallingSquare);
        fallingSquare.clearAnimation();
        fallingSquare.animate().cancel();

        fallingSquare.animate().scaleX(0).scaleY(0).setDuration(100).withEndAction(() -> fallingSquare.setVisibility(View.GONE));

        tvScore.setText(String.valueOf(Math.round(score)));
    }

    private void handleWrongClick() {
        currentTime -= secondsLostWhenMissed;

        if (isPlaying)
            MediaManager.getInstance().playEffectTrack(R.raw.effect_miss);
    }

    protected void gameLoop() {
        disappearTimeRunnable = () -> {
            if (!isPlaying)
                return;

            sendTreat();

            disappearTimeHandler.removeCallbacks(disappearTimeRunnable);
            disappearTimeHandler.postDelayed(disappearTimeRunnable, currentSquareDisappearTime);
        };
        disappearTimeHandler.post(disappearTimeRunnable);
    }

    private void sendTreat() {
        ImageView newImage = new ImageView(parentView);

        newImage.setImageDrawable(mainSquare.getDrawable());
        newImage.setBackground(mainSquare.getBackground());

        ImageView randomSquare = dropSquares.get(rng.nextInt(dropSquares.size()));
        float correctedXValue = getCorrectedX(randomSquare.getX(), mainSquare.getWidth(), randomSquare.getWidth());

        newImage.setX(correctedXValue);
        newImage.setY(0);

        //Scale is purely for an opening animation before the squares fall
        newImage.setScaleX(0);
        newImage.setScaleY(0);

        ((ViewGroup) mainSquare.getParent()).addView(newImage);

        fallingSquares.add(newImage);

        newImage.animate().scaleX(1).scaleY(1).setDuration(timeToScaleUp).withEndAction(() -> {
            newImage.animate().translationY(sectionHeight + headerHeight).setDuration(timeToFall).withEndAction(() -> {
                if (fallingSquares.contains(newImage)) {
                    fallingSquares.remove(newImage);
                    handleWrongClick();
                }
            });
        });
    }

    private float getCorrectedX(float dropSquareX, int mainSquareWidth, int dropSquareWidth) {
        float xPos = dropSquareX;
        int widthDifference = dropSquareWidth - mainSquareWidth;
        int halfWidthDifference = Math.round(widthDifference / 2);

        return xPos + halfWidthDifference;
    }

    private void addScore() {
        score += scorePerClick * Talents.cutenessFactor.getCurrentBonus();
    }

    protected int getAverageScore() {
        double score = 0;

        switch (tournamentDifficulty) {
            case EASY:
                score = scorePerClick * 12;
                break;
            case NORMAL:
                score = scorePerClick * 16;
                break;
            case HARD:
                score = scorePerClick * 20;
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