package com.lertos.projectyorkie.tournament.games;

import android.graphics.Rect;
import android.os.Handler;
import android.view.ViewGroup;
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

public class CatchDogTreats extends TournamentGame {

    private final int sizeAvatarInDP = 40;
    private final int sizeSquareInDP = 55;
    private final Handler disappearTimeHandler = new Handler();
    private final double secondsLostWhenMissed = 4;
    private final double secondsGainedWhenCorrect = 1;
    private final double baseDisappearTime = 3.5;
    private final double scorePerClick = 50;
    private final int initialSquareDisappearTime;
    private Runnable disappearTimeRunnable;
    private ImageView mainSquare;
    private ArrayList<ImageView> dropSquares;
    private int headerHeight;
    private int sectionHeight;
    private int timeToFall;
    private int currentSquareDisappearTime;
    //Starting at 2 so the math works better
    private int currentSquare = 2;

    public CatchDogTreats(TournamentMaster tournamentMaster, TournamentDifficulty difficulty, AppCompatActivity view, String gameTitle) {
        super(tournamentMaster, difficulty, view, gameTitle);

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
    }

    private void setupOnClickListeners() {
        //TODO: Add the listeners for each drop square
    }

    private void handleSquareClicked() {
        currentTime += secondsGainedWhenCorrect;
        currentSquare++;

        setNextDisappearTime();
        addScore();

        tvScore.setText(String.format(String.format("%.2f", score)));
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

        newImage.setX(randomSquare.getX());
        newImage.setY(0);

        //Scale is purely for an opening animation before the squares fall
        newImage.setScaleX(0);
        newImage.setScaleY(0);

        ((ViewGroup) mainSquare.getParent()).addView(newImage);

        newImage.animate().scaleX(1).scaleY(1).setDuration(300).withEndAction(() -> {
            newImage.animate().translationY(sectionHeight + headerHeight).setDuration(1000);
        });
        /*
        View view = getUnusedAvatar();


        //Add the specific avatar to a list to check against later when we want to raise the next one
        avatarsInUse.add(view);

        //Animate the movement above the square to allow them time to click on it
        view.animate().translationY(pixelValue(-sizeAvatarInDP)).setDuration(timeToRise).withEndAction(() -> {
            Handler handler = new Handler();
            Runnable runnable = () -> {
                if (avatarsInUse.contains(view)) {
                    view.animate().translationY(0).setDuration(100).withEndAction(() -> {
                        //They missed it; deduct time
                        currentTime -= secondsLostWhenMissed;
                        avatarsInUse.remove(view);
                    });
                }
            };
            if (avatarsInUse.contains(view)) {
                handler.postDelayed(runnable, timeToDisappear);
            }
        });
         */
    }

    private void addScore() {
        score += scorePerClick * Talents.cutenessFactor.getCurrentBonus();
    }

    protected int getAverageScore() {
        double score = 0;

        switch (tournamentDifficulty) {
            case EASY:
                score = scorePerClick * 7;
                break;
            case NORMAL:
                score = scorePerClick * 11;
                break;
            case HARD:
                score = scorePerClick * 15;
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