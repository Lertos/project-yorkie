package com.lertos.projectyorkie.tournament.games;

import static android.view.View.GONE;

import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.tournament.TournamentGame;

import java.util.ArrayList;

public class WhackTheCat extends TournamentGame {

    private ArrayList<View> squares;
    private final int sizeAvatarInDP = 70;
    private final int sizeSquareInDP = 80;
    private final int numberOfRows = 2;
    private final int numberOfCols = 3;
    private Rect gameLayout = new Rect();
    private int xEnd, yEnd;

    public WhackTheCat(View view) {
        super(view);

        squares = new ArrayList<>();

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

                setMaxDimensionsForSquares(view);

                params.leftMargin = xFraction * (j * 2 + 1);
                params.topMargin = yFraction * (i * 2 + 1);

                layout.addView(view, params);

                squares.add(view);
            }
        }
    }

    private RelativeLayout.LayoutParams createLayoutParams() {
        int maxSizeX = pixelValue(sizeSquareInDP);
        int maxSizeY = maxSizeX * 3;

        return new RelativeLayout.LayoutParams(maxSizeX, maxSizeY);
    }

    private void setMaxDimensionsForSquares(View view) {
        ViewGroup imageGroup = ((ViewGroup) view);

        ImageView avatar = ((ImageView) imageGroup.getChildAt(0));

        avatar.setMinimumHeight(pixelValue(sizeAvatarInDP));
        avatar.setMaxHeight(pixelValue(sizeAvatarInDP));

        ImageView block = ((ImageView) imageGroup.getChildAt(1));

        block.setMinimumHeight(pixelValue(sizeSquareInDP));
        block.setMaxHeight(pixelValue(sizeSquareInDP));
    }

    public void setupOnClickListeners() {
        for (View container : squares) {
            ViewGroup viewGroup = ((ViewGroup) container);
            View view = viewGroup.getChildAt(0);

            view.setOnClickListener(v -> {
                v.animate().translationY(-sizeSquareInDP * 2).setDuration(1000);
            });
        }
    }

    protected void gameLoop() {
        //Run handler and constantly run it until game is over, then return
    }


}
