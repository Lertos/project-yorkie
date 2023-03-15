package com.lertos.projectyorkie.tournament.games;

import android.graphics.Rect;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
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

public class DodgeTheCats extends TournamentGame {

    private final Handler gameLoopTimeHandler = new Handler();
    private Runnable gameLoopTimeRunnable;
    private GestureDetector gestureDetector;
    private final double secondsLostWhenMissed = 4;
    private final double secondsGainedWhenCorrect = 1;
    private final double baseTimeOfCatFalling = 3.5;
    private final double scorePerDodge = 50;
    private final int initialTimeOfCatFalling;
    private ImageView ivCatAvatar;
    private ImageView ivYorkieAvatar;
    private ArrayList<ImageView> fallingCats = new ArrayList<>();
    private int laneX1;
    private int laneX2;
    private int laneX3;
    private int laneY;
    private int laneWidth;
    private int headerHeight;
    private int sectionHeight;
    private int timeToSwitchLanes = 75;
    private int timeBetweenWaves;
    private int timeBetweenCats;
    private int timeOfCatFalling;
    private int previousLaneIndex = 1;
    private int currentWave = 1;
    private int currentCatInWave = 1;
    private int catsPerWave = 6;

    public DodgeTheCats(TournamentMaster tournamentMaster, TournamentDifficulty difficulty, AppCompatActivity view, String gameTitle, String gameHint) {
        super(tournamentMaster, difficulty, view, gameTitle, gameHint);

        gestureDetector = new GestureDetector(parentView.getApplicationContext(), new GestureListener());

        initialTimeOfCatFalling = calculateInitialTimeOfCatFalling();
        timeOfCatFalling = initialTimeOfCatFalling;
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

                //Assign the two avatars to reuse
                ivCatAvatar = parentView.findViewById(R.id.ivCatToClone);
                ivYorkieAvatar = parentView.findViewById(R.id.ivYorkieAvatar);

                //Assign the lane info so the cloned avatars can copy their sizes and positions
                //NOTE: the lane X's will be off by the amount of the parent's margin, so we add that
                View laneParentContainer = (View) parentView.findViewById(R.id.ivLaneSpace1).getParent();
                int parentMargin = (int) laneParentContainer.getX();

                laneX1 = (int) parentView.findViewById(R.id.ivLaneSpace1).getX() + parentMargin;
                laneX2 = (int) parentView.findViewById(R.id.ivLaneSpace2).getX() + parentMargin;
                laneX3 = (int) parentView.findViewById(R.id.ivLaneSpace3).getX() + parentMargin;

                //Assign the simple width of the square to copy later
                laneWidth = parentView.findViewById(R.id.ivLaneSpace1).getWidth();

                //Get the game screen bounds
                Rect headerLayout = new Rect();
                parentView.findViewById(R.id.linHeader).getGlobalVisibleRect(headerLayout);

                headerHeight = headerLayout.height();
                sectionHeight = gameLayout.height() - headerHeight;

                //Now that we know all the measurements, let's get the laneY
                laneY = sectionHeight - headerHeight - (laneWidth / 2);

                //These methods require the variables assigned up above so they need to be in this block
                setupAvatarDimensions();
                setTimingOfMovements();
                setupOnClickListeners();
            }
        });
    }

    private void setupAvatarDimensions() {
        //Set width and height to match the lane sizes
        ivCatAvatar.setMinimumWidth(laneWidth);
        ivCatAvatar.setMinimumHeight(laneWidth);

        ivYorkieAvatar.setMinimumWidth(laneWidth);
        ivYorkieAvatar.setMinimumHeight(laneWidth);
        ivYorkieAvatar.setMaxWidth(laneWidth);
        ivYorkieAvatar.setMaxHeight(laneWidth);

        //Position the yorkie at the center start location
        ivYorkieAvatar.setX(laneX2);
        ivYorkieAvatar.setY(laneY);

        //Make the yorkie avatar visible
        ivYorkieAvatar.setVisibility(View.VISIBLE);
    }

    private void setTimingOfMovements() {
        timeBetweenWaves = 0;

        switch (tournamentDifficulty) {
            case EASY:
                timeBetweenWaves = 1200;
                break;
            case NORMAL:
                timeBetweenWaves = 1000;
                break;
            case HARD:
                timeBetweenWaves = 800;
                break;
        }

        timeBetweenCats = 0;

        switch (tournamentDifficulty) {
            case EASY:
                timeBetweenCats = 300;
                break;
            case NORMAL:
                timeBetweenCats = 200;
                break;
            case HARD:
                timeBetweenCats = 100;
                break;
        }
    }

    private void setupOnClickListeners() {
        View view = parentView.findViewById(R.id.relParent);

        view.setOnTouchListener((view1, motionEvent) -> gestureDetector.onTouchEvent(motionEvent));

        //For whatever reason, this needs to be set or the above detector doesn't work...
        view.setOnClickListener(v -> {
        });
    }

    protected void gameLoop() {
        gameLoopTimeRunnable = () -> {
            if (!isPlaying)
                return;

            int postDelay = 0;

            //If there are no more cats to send this wave, wait for the next wave and reset counter
            if (currentCatInWave >= catsPerWave) {
                postDelay = timeBetweenWaves;

                currentCatInWave = 1;
            }
            //If there are still cats to send, send them and continue to the next iteration
            else {
                postDelay = timeBetweenCats;

                createAndSendCat();
                currentCatInWave++;
            }

            gameLoopTimeHandler.removeCallbacks(gameLoopTimeRunnable);
            gameLoopTimeHandler.postDelayed(gameLoopTimeRunnable, postDelay);
        };
        gameLoopTimeHandler.post(gameLoopTimeRunnable);
    }

    private void createAndSendCat() {
        ImageView newImage = new ImageView(parentView);

        newImage.setImageDrawable(ivCatAvatar.getDrawable());
        newImage.setBackground(ivCatAvatar.getBackground());

        //Figure out which random lane this cat will spawn in
        int randomLane = rng.nextInt(3);
        int newX;

        //Make sure cats don't fall in the same lane back to back
        if (randomLane == previousLaneIndex) {
            randomLane = (randomLane + 1) % 2;
        }

        if (randomLane == 0) {
            newX = laneX1;
        } else if (randomLane == 1) {
            newX = laneX2;
        } else {
            newX = laneX3;
        }

        previousLaneIndex = randomLane;

        newImage.setX(newX);
        newImage.setY(0 - ivCatAvatar.getMinimumHeight());

        ((ViewGroup) ivCatAvatar.getParent()).addView(newImage);

        //After adding the image, set the width and height of the avatar to copy
        newImage.getLayoutParams().height = ivCatAvatar.getMinimumHeight();
        newImage.getLayoutParams().width = ivCatAvatar.getMinimumWidth();

        newImage.requestLayout();

        fallingCats.add(newImage);

        newImage.animate().translationY(sectionHeight + headerHeight).setDuration(timeBetweenWaves).withEndAction(() -> {
            if (fallingCats.contains(newImage)) {
                fallingCats.remove(newImage);
                //handleWrongClick();
            }
        });
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

    private int calculateInitialTimeOfCatFalling() {
        int tournamentRankValue = DataManager.getInstance().getPlayerData().getTournamentRank().getRankValue();
        double timeInSeconds = (canineFocus + baseTimeOfCatFalling) / tournamentRankValue;
        int timeInMilliseconds = (int) Math.round(timeInSeconds * 1000);

        return timeInMilliseconds;
    }

    private void setNextTimeOfCatFalling() {
        timeOfCatFalling = (int) Math.floor(initialTimeOfCatFalling / ((currentWave + 1) / 2.0)); //+1 is so math works better
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        //Don't return false here or none of the other gestures work
        @Override
        public boolean onDown(MotionEvent event) {
            return super.onDown(event);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velX, float velY) {
            int currentX = (int) ivYorkieAvatar.getX();

            //If the swipe was somehow only vertical, return
            if (velX == 0)
                return true;
                //If the player swiped any bit to the LEFT
            else if (velX < 0) {
                if (currentX == laneX2)
                    ivYorkieAvatar.animate().translationX(laneX1).setDuration(timeToSwitchLanes);
                else if (currentX == laneX3)
                    ivYorkieAvatar.animate().translationX(laneX2).setDuration(timeToSwitchLanes);
            }
            //If the player swiped any bit to the RIGHT
            else if (velX > 0) {
                if (currentX == laneX1)
                    ivYorkieAvatar.animate().translationX(laneX2).setDuration(timeToSwitchLanes);
                else if (currentX == laneX2)
                    ivYorkieAvatar.animate().translationX(laneX3).setDuration(timeToSwitchLanes);
            }
            return true;
        }
    }
}