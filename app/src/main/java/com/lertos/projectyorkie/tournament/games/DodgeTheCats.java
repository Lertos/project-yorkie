package com.lertos.projectyorkie.tournament.games;

import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
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
    private GestureDetector gestureDetector;
    private final double secondsLostWhenMissed = 4;
    private final double secondsGainedWhenCorrect = 1;
    private final double baseDisappearTime = 3.5;
    private final double scorePerDodge = 50;
    private final int initialSquareDisappearTime;
    private Runnable disappearTimeRunnable;
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
    private int timeToSwitchLanes = 100;
    private int timeToFall;
    private int timeToScaleUp;
    private int timeBetweenCats;
    private int currentSquareDisappearTime;
    //Starting at 2 so the math works better
    private int currentSquare = 2;

    public DodgeTheCats(TournamentMaster tournamentMaster, TournamentDifficulty difficulty, AppCompatActivity view, String gameTitle, String gameHint) {
        super(tournamentMaster, difficulty, view, gameTitle, gameHint);

        gestureDetector = new GestureDetector(parentView.getApplicationContext(), new GestureListener());

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
        ivCatAvatar.setMaxWidth(laneWidth);
        ivCatAvatar.setMaxHeight(laneWidth);

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
        View view = parentView.findViewById(R.id.relParent);

        view.setOnTouchListener((view1, motionEvent) -> gestureDetector.onTouchEvent(motionEvent));

        //For whatever reason, this needs to be set or the above detector doesn't work...
        view.setOnClickListener(v -> {
        });
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

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        //Don't return false here or none of the other gestures work
        @Override
        public boolean onDown(MotionEvent event) {
            Log.d("ondown", "ondown");
            return super.onDown(event);
            //return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            int currentX = (int) ivYorkieAvatar.getX();
            float xMovement = e1.getX() - e2.getX();

            Log.d("TAG2", "curx : " + currentX);
            Log.d("TAG2", "laneX1 : " + laneX1);
            Log.d("TAG2", "laneX2 : " + laneX2);
            Log.d("TAG2", "laneX3 : " + laneX3);
            Log.d("TAG2", "distanceX : " + distanceX);
            //If the swipe was somehow only vertical, return
            if (xMovement == 0)
                return true;
                //If the player swiped any bit to the LEFT
            else if (xMovement > 0) {
                if (currentX == laneX2)
                    ivYorkieAvatar.animate().translationX(laneX1).setDuration(timeToSwitchLanes);
                else if (currentX == laneX3)
                    ivYorkieAvatar.animate().translationX(laneX2).setDuration(timeToSwitchLanes);
            }
            //If the player swiped any bit to the RIGHT
            else if (xMovement < 0) {
                if (currentX == laneX1)
                    ivYorkieAvatar.animate().translationX(laneX2).setDuration(timeToSwitchLanes);
                else if (currentX == laneX2)
                    ivYorkieAvatar.animate().translationX(laneX3).setDuration(timeToSwitchLanes);
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velX, float velY) {
            //TODO: Add same logic to this that onScroll has
            Log.d("XXXX", "curx : " + velX);
            Log.d("XXXX", "laneX1 : " + velY);
            return true;
        }
    }
}