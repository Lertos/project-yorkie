package com.lertos.projectyorkie.tournament.games;

import android.graphics.Rect;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.tournament.TournamentGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class WhackTheCat extends TournamentGame {

    private Random rng = new Random();
    private ArrayList<View> avatars;
    private ArrayList<View> avatarsInUse;
    private final int sizeAvatarInDP = 40;
    private final int sizeSquareInDP = 55;
    private final int numberOfRows = 3;
    private final int numberOfCols = 3;
    private Rect gameLayout = new Rect();
    private final Handler disappearTimeHandler = new Handler();
    private Runnable disappearTimeRunnable;
    private int sectionWidth, sectionHeight;
    private final double secondsLostWhenMissed = 4;
    private final double secondsGainedWhenCorrect = 1;
    private final double baseDisappearTime = 3.5;
    private final int initialSquareDisappearTime;
    private int currentSquareDisappearTime;
    //Starting at 2 so the math works better
    private int currentSquare = 2;

    public WhackTheCat(View view) {
        super(view);

        avatars = new ArrayList<>();
        avatarsInUse = new ArrayList<>();

        initialSquareDisappearTime = calculateInitialDisappearTime();
        currentSquareDisappearTime = initialSquareDisappearTime;

        //Need the layout to be inflated before doing math using the variables produced inside this block
        RelativeLayout layout = (RelativeLayout) parentView.findViewById(R.id.relMainSection);
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
                addImagesToView();
                setupOnClickListeners();
            }
        });
    }

    public void addImagesToView() {
        RelativeLayout layout = parentView.findViewById(R.id.relMainSection);
        //These are to give even spacing, so: (SPACE) (OBJ) (SPACE) (OBJ) (SPACE) for example
        int xFraction = sectionWidth / ((numberOfCols * 2) + 1);
        int yFraction = sectionHeight / ((numberOfRows * 2) + 1);

        for (int i=0; i<numberOfRows; i++) {
            for (int j=0; j<numberOfCols; j++) {
                View view = LayoutInflater.from(parentView.getContext()).inflate(R.layout.subpage_whack_cat_item, null);

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

    private ImageView setMaxDimensionsForSquare(ViewGroup imageGroup) {
        ImageView square = ((ImageView) imageGroup.getChildAt(1));

        square.setMinimumHeight(pixelValue(sizeSquareInDP));
        square.setMaxHeight(pixelValue(sizeSquareInDP));

        return square;
    }

    public void setupOnClickListeners() {
        for (View view : avatars) {
            view.setOnClickListener(v -> {
                //TODO: Add score (based on a formula) for getting it right as well
                //If the player clicks a valid, rising avatar - award them time
                if (avatarsInUse.contains(view)) {
                    currentTime += secondsGainedWhenCorrect;
                    currentSquare++;

                    setNextDisappearTime();

                    v.animate().translationY(0).setDuration(100).withEndAction(() -> {
                        avatarsInUse.remove(view);
                    });
                }
                //If the player clicks an inactive spot (wrong click / timing / spamming) - take time away
                else {
                    currentTime -= secondsLostWhenMissed;
                }
            });
        }
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

        int timeToRise = 1000;
        int timeToDisappear = 1500;

        //Add the specific avatar to a list to check against later when we want to raise the next one
        avatarsInUse.add(view);

        //Animate the movement above the square to allow them time to click on it
        view.animate().translationY(pixelValue(-sizeAvatarInDP)).setDuration(1000);

        Handler handler = new Handler();
        Runnable runnable = () -> {
            view.animate().translationY(0).setDuration(timeToRise / 2).withEndAction(() -> {
                if (avatarsInUse.contains(view)) {
                    //They missed it; deduct time
                    currentTime -= secondsLostWhenMissed;
                    avatarsInUse.remove(view);
                }
            });
        };
        handler.postDelayed(runnable, timeToDisappear);
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