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
import com.lertos.projectyorkie.tournament.TournamentGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class WhackTheCat extends TournamentGame {

    Random rng = new Random();
    private ArrayList<View> avatars;
    private ArrayList<View> avatarsInUse;
    private final int sizeAvatarInDP = 40;
    private final int sizeSquareInDP = 55;
    private final int numberOfRows = 3;
    private final int numberOfCols = 3;
    private Rect gameLayout = new Rect();
    private int xEnd, yEnd;

    public WhackTheCat(View view) {
        super(view);

        avatars = new ArrayList<>();
        avatarsInUse = new ArrayList<>();

        //Need the layout to be inflated before doing math using the variables produced inside this block
        RelativeLayout layout = (RelativeLayout) parentView.findViewById(R.id.relMainSection);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                layout.getGlobalVisibleRect(gameLayout);

                int buttonHeight = sizeSquareInDP;
                int layoutMargin = 60;

                xEnd = gameLayout.right - layoutMargin - buttonHeight;
                yEnd = gameLayout.bottom - 201 - layoutMargin - buttonHeight;

                //These methods require the variables assigned up above so they need to be in this block
                addImagesToView();
                setupOnClickListeners();
            }
        });
    }

    public void addImagesToView() {
        RelativeLayout layout = parentView.findViewById(R.id.relMainSection);
        int xFraction = xEnd / (numberOfCols * 2);
        int yFraction = yEnd / (numberOfRows * 2);

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
        for (View container : avatars) {
            container.setOnClickListener(v -> {
                v.animate().translationY(-sizeSquareInDP * 2).setDuration(1000);
            });
        }
    }

    protected void gameLoop() {
        //Run handler and constantly run it until game is over, then return
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                raiseAvatar();

                handler.postDelayed(this, 500);
            }
        };
        handler.post(runnable);
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
                avatarsInUse.remove(view);
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

}
