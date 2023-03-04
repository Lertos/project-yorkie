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
import java.util.Random;

public class WhackTheCat extends TournamentGame {

    Random rng = new Random();
    private ArrayList<View> avatars;
    private final int sizeAvatarInDP = 60;
    private final int sizeSquareInDP = 80;
    private final int numberOfRows = 2;
    private final int numberOfCols = 3;
    private Rect gameLayout = new Rect();
    private int xEnd, yEnd;

    public WhackTheCat(View view) {
        super(view);

        avatars = new ArrayList<>();

        RelativeLayout layout = (RelativeLayout) parentView.findViewById(R.id.relMainSection);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                layout.getGlobalVisibleRect(gameLayout);

                int buttonHeight = sizeSquareInDP;
                int layoutMargin = 120;

                xEnd = gameLayout.right - layoutMargin - buttonHeight;
                yEnd = gameLayout.bottom - 201 - layoutMargin - buttonHeight;

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

                handler.postDelayed(this, 2000);
            }
        };
        handler.post(runnable);
    }

    private void raiseAvatar() {
        View view = avatars.get(rng.nextInt(avatars.size()));
        int timeToRise = 1000;
        int timeToDisappear = 1500;

        view.animate().translationY(pixelValue(-sizeAvatarInDP)).setDuration(1000);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                view.animate().translationY(0).setDuration(timeToRise / 2);
            }
        };
        handler.postDelayed(runnable, timeToDisappear);
    }


}
