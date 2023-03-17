package com.lertos.projectyorkie.tournament.games;

import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
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

public class TreatToss extends TournamentGame {

    private final Handler gameLoopTimeHandler = new Handler();
    private Runnable gameLoopTimeRunnable;
    private Runnable collisionRunnable;
    private final double secondsLostForMiss = 3;
    private final double secondsGainedForHit = 1;
    private final double baseTimeOfTreatMovement = 3.5;
    private final double scorePerHit = 100;
    private final int initialTimeOfTreatMovement;
    private ImageView ivYorkieAvatar;
    private ImageView ivTreatAvatar;
    private ImageView ivSizeToCopy;
    private int yorkieY;
    private int treatY;
    private int avatarCollisionHeight;
    private int headerHeight;
    private int sectionHeight;
    private int sectionWidth;
    private boolean isMovingToLeft = true;
    private boolean isBeingTossed = false;
    private boolean isReadyForNewTreat = true;
    private int timeOfTreatToss;
    private int timeOfTreatMovement;
    private int currentTreat = 1;

    public TreatToss(TournamentMaster tournamentMaster, TournamentDifficulty difficulty, AppCompatActivity view, String gameTitle, String gameHint) {
        super(tournamentMaster, difficulty, view, gameTitle, gameHint);

        initialTimeOfTreatMovement = calculateInitialTimeOfTreatMovement();
        timeOfTreatMovement = initialTimeOfTreatMovement;
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

                //Assign the avatars to reuse
                ivYorkieAvatar = parentView.findViewById(R.id.ivYorkieAvatar);
                ivTreatAvatar = parentView.findViewById(R.id.ivTreatAvatar);
                ivSizeToCopy = parentView.findViewById(R.id.ivSizeToCopy);

                //Get the game screen bounds
                Rect headerLayout = new Rect();
                parentView.findViewById(R.id.linHeader).getGlobalVisibleRect(headerLayout);

                headerHeight = headerLayout.height();
                sectionHeight = gameLayout.height() - headerHeight;
                sectionWidth = gameLayout.width();

                //Now that we know all the measurements, let's get the Y positions
                yorkieY = (int) headerHeight;
                treatY = sectionHeight - ivSizeToCopy.getHeight();

                //These methods require the variables assigned up above so they need to be in this block
                setupAvatarDimensions();
                setTimingOfMovements();
                setupOnClickListeners();
                checkForCollisions();
            }
        });
    }

    private void setupAvatarDimensions() {
        int avatarWidth = ivSizeToCopy.getWidth();

        //Set width and height to match the lane sizes
        ivYorkieAvatar.setMinimumWidth(avatarWidth);
        ivYorkieAvatar.setMinimumHeight(avatarWidth);
        ivYorkieAvatar.setMaxWidth(avatarWidth);
        ivYorkieAvatar.setMaxHeight(avatarWidth);

        ivTreatAvatar.setMinimumWidth(avatarWidth);
        ivTreatAvatar.setMinimumHeight(avatarWidth);
        ivTreatAvatar.setMaxWidth(avatarWidth);
        ivTreatAvatar.setMaxHeight(avatarWidth);

        //Position the avatars at the correct start locations
        //TODO: Calculate X values differently
        ivYorkieAvatar.setX(300);
        ivYorkieAvatar.setY(yorkieY);

        ivTreatAvatar.setX(300);
        ivTreatAvatar.setY(treatY);

        //Make the avatars visible
        ivYorkieAvatar.setVisibility(View.VISIBLE);
        ivTreatAvatar.setVisibility(View.VISIBLE);

        //Set these so we don't have to calculate these everytime we want to use them
        avatarCollisionHeight = avatarWidth / 4;
    }

    private void setTimingOfMovements() {
        timeOfTreatToss = 0;

        switch (tournamentDifficulty) {
            case EASY:
                timeOfTreatToss = 1200;
                break;
            case NORMAL:
                timeOfTreatToss = 900;
                break;
            case HARD:
                timeOfTreatToss = 700;
                break;
        }
    }

    private void setupOnClickListeners() {
        parentView.findViewById(R.id.relParent).setOnClickListener(v -> {
            //If the treat is in mid air or there's no treat there is nothing to do here
            if (isBeingTossed || isReadyForNewTreat)
                return;

            isBeingTossed = true;

            ivTreatAvatar.animate().cancel();

            ivTreatAvatar.animate().translationY(0).setDuration(timeOfTreatToss).setInterpolator(new LinearInterpolator()).withEndAction(() -> {
                //If there was no collision with the player, they missed so punish them
                if (!isReadyForNewTreat) {
                    handleTreatMiss();
                    isReadyForNewTreat = true;
                }
            });
        });
    }

    private void checkForCollisions() {
        final Handler handler = new Handler();

        collisionRunnable = () -> {
            //Only check for collision detection if the treat is being tossed
            if (isBeingTossed) {
                //Check for X lineup first; if they don't have the X right, Y won't matter
                if (ivTreatAvatar.getX() + avatarCollisionHeight < ivYorkieAvatar.getX() && ivTreatAvatar.getX() - avatarCollisionHeight > ivYorkieAvatar.getX()) {
                    if (ivTreatAvatar.getY() + avatarCollisionHeight < ivYorkieAvatar.getY() && ivTreatAvatar.getY() - avatarCollisionHeight > ivYorkieAvatar.getY()) {
                        Log.d("hit", "ddd");
                    }
                }
            }

            handler.postDelayed(collisionRunnable, 100);
        };
        handler.post(collisionRunnable);
    }

    protected void gameLoop() {
        gameLoopTimeRunnable = () -> {
            if (!isPlaying)
                return;

            int postDelay = 100;

            //If there is no active treat; create one and send it to the opposite side of the last direction
            if (isReadyForNewTreat) {
                isReadyForNewTreat = false;
                isBeingTossed = false;

                //Place the yorkie randomly
                ivYorkieAvatar.setX(rng.nextInt(sectionWidth - ivSizeToCopy.getWidth()));

                //Set it back to it's normal coordinates
                ivTreatAvatar.setY(treatY);

                //Make the treat move faster next time
                setNextTimeOfTreatMovement();
            }

            if (!isBeingTossed) {
                postDelay = timeOfTreatMovement;
                sendTreatToSide();
            }

            gameLoopTimeHandler.removeCallbacks(gameLoopTimeRunnable);
            gameLoopTimeHandler.postDelayed(gameLoopTimeRunnable, postDelay);
        };
        gameLoopTimeHandler.post(gameLoopTimeRunnable);
    }

    private void sendTreatToSide() {
        int xToStartAt;
        int xToSendTo;

        if (isMovingToLeft) {
            xToStartAt = 0;
            xToSendTo = sectionWidth - ivSizeToCopy.getWidth();
            isMovingToLeft = false;
        } else {
            xToStartAt = sectionWidth - ivSizeToCopy.getWidth();
            xToSendTo = 0;
            isMovingToLeft = true;
        }

        ivTreatAvatar.setX(xToStartAt);

        ivTreatAvatar.animate().cancel();
        ivTreatAvatar.animate().translationX(xToSendTo).setDuration(timeOfTreatMovement);
    }

    private void handleTreatMiss() {
        currentTime -= secondsLostForMiss;

        if (isPlaying)
            MediaManager.getInstance().playEffectTrack(R.raw.effect_whacked);
    }

    private void handlePlayerHitWithTreat() {
        currentTime += secondsGainedForHit;
        addScore();
    }

    private void addScore() {
        score += scorePerHit * Talents.cutenessFactor.getCurrentBonus();
        tvScore.setText(String.valueOf(Math.round(score)));
    }

    protected int getAverageScore() {
        double score = 0;

        switch (tournamentDifficulty) {
            case EASY:
                score = scorePerHit * 13;
                break;
            case NORMAL:
                score = scorePerHit * 18;
                break;
            case HARD:
                score = scorePerHit * 23;
                break;
        }
        return (int) Math.round(score);
    }

    private int calculateInitialTimeOfTreatMovement() {
        int tournamentRankValue = DataManager.getInstance().getPlayerData().getTournamentRank().getRankValue();
        double timeInSeconds = (canineFocus + baseTimeOfTreatMovement) / tournamentRankValue;
        int timeInMilliseconds = (int) Math.round(timeInSeconds * 1000);

        return timeInMilliseconds;
    }

    private void setNextTimeOfTreatMovement() {
        timeOfTreatMovement = (int) Math.floor(initialTimeOfTreatMovement / ((currentTreat + 1) / 2.0)); //+1 is so math works better
    }

}