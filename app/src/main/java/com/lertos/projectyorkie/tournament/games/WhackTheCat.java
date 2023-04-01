package com.lertos.projectyorkie.tournament.games;

import android.graphics.Rect;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.MediaManager;
import com.lertos.projectyorkie.tournament.TournamentDifficulty;
import com.lertos.projectyorkie.tournament.TournamentGame;
import com.lertos.projectyorkie.tournament.TournamentMaster;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class WhackTheCat extends TournamentGame {

    private final ArrayList<View> avatars;
    private final ArrayList<View> avatarsInUse;
    private final int sizeAvatarInDP = 40;
    private final int sizeSquareInDP = 55;
    private final Handler disappearTimeHandler = new Handler();
    private Runnable disappearTimeRunnable;
    private int sectionWidth, sectionHeight;
    private final double secondsLostWhenMissed = 4;
    private final double scorePerClick = 50;
    private int timeToRise;
    private int timeToDisappear;
    private final int initialSquareDisappearTime;
    private int currentSquareDisappearTime;
    //Starting higher so the math works better
    private int currentSquare = 5;

    public WhackTheCat(TournamentMaster tournamentMaster, TournamentDifficulty difficulty, AppCompatActivity view, String gameTitle, String gameHint) {
        super(tournamentMaster, difficulty, view, gameTitle, gameHint);

        avatars = new ArrayList<>();
        avatarsInUse = new ArrayList<>();

        initialSquareDisappearTime = calculateInitialDisappearTime();
        currentSquareDisappearTime = initialSquareDisappearTime;
        setNextDisappearTime();
    }

    protected void setupUI() {
        //Need the layout to be inflated before doing math using the variables produced inside this block
        RelativeLayout layout = parentView.findViewById(R.id.relMainSection);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                layout.getGlobalVisibleRect(gameLayout);

                Rect headerLayout = new Rect();
                parentView.findViewById(R.id.linHeader).getGlobalVisibleRect(headerLayout);

                int headerHeight = headerLayout.height();

                sectionWidth = gameLayout.width();
                sectionHeight = gameLayout.height() - headerHeight;

                //These methods require the variables assigned up above so they need to be in this block
                setTimingOfMovements();
                addImagesToView();
                setupOnClickListeners();
            }
        });
        tvScore.setText(String.valueOf(Math.round(score)));
    }

    private void setTimingOfMovements() {
        timeToRise = 0;

        switch (tournamentDifficulty) {
            case EASY:
                timeToRise = 1100;
                break;
            case NORMAL:
                timeToRise = 1000;
                break;
            case HARD:
                timeToRise = 900;
                break;
        }

        timeToDisappear = 0;

        switch (tournamentDifficulty) {
            case EASY:
                timeToDisappear = 1500;
                break;
            case NORMAL:
                timeToDisappear = 1300;
                break;
            case HARD:
                timeToDisappear = 1100;
                break;
        }
    }

    private void addImagesToView() {
        RelativeLayout layout = parentView.findViewById(R.id.relMainSection);
        //These are to give even spacing, so: (SPACE) (OBJ) (SPACE) (OBJ) (SPACE) for example
        int numberOfCols = 3;
        int numberOfRows = 3;
        int xFraction = sectionWidth / ((numberOfCols * 2) + 1);
        int yFraction = sectionHeight / ((numberOfRows * 2) + 1);

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfCols; j++) {
                View view = LayoutInflater.from(parentView).inflate(R.layout.sub_page_whack_cat_item, null);

                RelativeLayout.LayoutParams params = createLayoutParams();

                ViewGroup imageGroup = ((ViewGroup) view);
                ImageView ivAvatar = setMaxDimensionsForAvatar(imageGroup);
                setMaxDimensionsForSquare(imageGroup);

                params.leftMargin = xFraction * (j * 2 + 1);
                params.topMargin = yFraction * (i * 2 + 1);

                ivAvatar.setId(View.generateViewId());

                layout.addView(view, params);

                avatars.add(ivAvatar);
            }
        }
    }

    private RelativeLayout.LayoutParams createLayoutParams() {
        int maxSizeX = pixelValue(sizeSquareInDP);
        int maxSizeY = maxSizeX * 3;

        return new RelativeLayout.LayoutParams(maxSizeX, maxSizeY);
    }

    private ImageView setMaxDimensionsForAvatar(ViewGroup imageGroup) {
        ImageView avatar = ((ImageView) imageGroup.getChildAt(0));

        avatar.setMinimumHeight(pixelValue(sizeAvatarInDP));
        avatar.setMaxHeight(pixelValue(sizeAvatarInDP));

        return avatar;
    }

    private void setMaxDimensionsForSquare(ViewGroup imageGroup) {
        ImageView square = ((ImageView) imageGroup.getChildAt(1));

        square.setMinimumHeight(pixelValue(sizeSquareInDP));
        square.setMaxHeight(pixelValue(sizeSquareInDP));
    }

    private void setupOnClickListeners() {
        for (View view : avatars) {
            view.setOnClickListener(v -> {
                //If the player clicks a valid, rising avatar - award them time
                if (avatarsInUse.contains(view)) {
                    handleSquareClicked();

                    //Animate the transition back to the avatars original position
                    v.animate().translationY(0).setDuration(100).withEndAction(() -> avatarsInUse.remove(view));
                }
                //If the player clicks an inactive spot (wrong click / timing / spamming) - take time away
                else {
                    addTimeToTimer(-secondsLostWhenMissed);
                }
            });
        }
    }

    private void handleSquareClicked() {
        addTimeToTimer(0.5);
        currentSquare++;

        if (isPlaying)
            MediaManager.getInstance().playEffectTrack(R.raw.effect_whacked);

        setNextDisappearTime();
        addScore(scorePerClick);
    }

    protected void gameLoop() {
        disappearTimeRunnable = () -> {
            if (!isPlaying)
                return;

            raiseAvatar();

            disappearTimeHandler.removeCallbacks(disappearTimeRunnable);
            disappearTimeHandler.postDelayed(disappearTimeRunnable, currentSquareDisappearTime);
        };
        disappearTimeHandler.post(disappearTimeRunnable);
    }

    private void raiseAvatar() {
        View view = getUnusedAvatar();

        if (view == null)
            return;

        //Add the specific avatar to a list to check against later when we want to raise the next one
        avatarsInUse.add(view);

        //Animate the movement above the square to allow them time to click on it
        view.animate().translationY(pixelValue(-sizeAvatarInDP)).setDuration(timeToRise).withEndAction(() -> {
            Handler handler = new Handler();
            Runnable runnable = () -> {
                if (avatarsInUse.contains(view)) {
                    view.animate().translationY(0).setDuration(100).withEndAction(() -> {
                        //They missed it; deduct time
                        addTimeToTimer(-secondsLostWhenMissed);
                        avatarsInUse.remove(view);
                    });
                }
            };
            if (avatarsInUse.contains(view)) {
                handler.postDelayed(runnable, timeToDisappear);
            }
        });
    }

    private View getUnusedAvatar() {
        //Check if all avatars in use
        if (avatars.size() == avatarsInUse.size())
            return null;

        Set<View> setAvatars = new HashSet<>();
        Set<View> setAvatarsInUse = new HashSet<>();

        setAvatars.addAll(avatars);
        setAvatarsInUse.addAll(avatarsInUse);

        //Find the difference of the sets to find unused avatars
        Set<View> difference = new HashSet<>(setAvatars);
        difference.removeAll(setAvatarsInUse);

        int randInd = rng.nextInt(difference.size());
        Iterator<View> iterator = difference.iterator();

        for (int i = 0; i < randInd; i++) {
            iterator.next();
        }
        return iterator.next();
    }

    protected int getAverageScore() {
        double score = 0;

        switch (tournamentDifficulty) {
            case EASY:
                score = scorePerClick * 20;
                break;
            case NORMAL:
                score = scorePerClick * 24;
                break;
            case HARD:
                score = scorePerClick * 28;
                break;
        }
        return (int) Math.round(score);
    }

    private int calculateInitialDisappearTime() {
        int tournamentRankValue = DataManager.getInstance().getPlayerData().getTournamentRank().getRankValue();
        double timeInSeconds = (canineFocus + 3.0) / tournamentRankValue;
        int timeInMilliseconds = (int) Math.round(timeInSeconds * 1000);

        return timeInMilliseconds;
    }

    private void setNextDisappearTime() {
        currentSquareDisappearTime = (int) Math.min(1500, Math.floor(initialSquareDisappearTime / (currentSquare / 2.0)));
    }

}