package com.lertos.projectyorkie;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.Random;

public class PettingPage extends AppCompatActivity {

    private PettingMaster pettingMaster;
    private Rect pettingLayout = new Rect();
    private ImageButton focusButton;
    private LinearProgressIndicator indicator;
    private final int timerMax = 1000;
    private TextView timerInSeconds;
    private double timerStartValue;
    private int xStart, yStart, xEnd, yEnd;
    private boolean isPlaying = false;
    final Handler disappearTimeHandler = new Handler();
    private Runnable timerRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_petting);

        focusButton = findViewById(R.id.btnPettingFocus);
        indicator = findViewById(R.id.indPettingTimer);
        timerInSeconds = findViewById(R.id.tvPettingTimerInSecs);

        //This makes sure the progress moves smoothly. 100 max makes it decrease in a choppy manner
        indicator.setMax(timerMax);

        LinearLayout layout = (LinearLayout)findViewById(R.id.linPettingMainSection);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                layout.getGlobalVisibleRect(pettingLayout);

                int buttonHeight = focusButton.getMaxHeight();
                int layoutMargin = 60;

                xStart = pettingLayout.left;
                yStart = pettingLayout.top  - layoutMargin;
                xEnd = pettingLayout.right - buttonHeight;
                yEnd = pettingLayout.bottom  - layoutMargin - buttonHeight;
            }
        });

        Helper.setupBottomButtonBar(this);
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        //Start the petting mini game
        ((Button) findViewById(R.id.btnStartPetting)).setOnClickListener( v -> {
            //Create a new instance of the petting mini game master
            //TODO: Use actual values instead of test values
            pettingMaster = new PettingMaster(1, 5, 1, 1);
            timerStartValue = pettingMaster.getTimerStartValue();
            isPlaying = true;

            setupUI(v);

            //Start the mini game
            pettingMaster.start();

            //Make the generated square visible after setup is done
            focusButton.setVisibility(View.VISIBLE);
        });

        ((ImageButton) findViewById(R.id.btnPettingFocus)).setOnClickListener( v -> handleSquareClick(true) );
    }

    private void setupUI(View v) {
        //Create the first square
        handleSquareClick(false);

        //Hide the start button
        v.setVisibility(View.INVISIBLE);

        //Show the timer
        findViewById(R.id.linPettingTimerSection).setVisibility(View.VISIBLE);

        //Initialize the timer and the UI for it
        indicator.setProgressCompat(100, false);
        updateIndicatorWithTime();
    }

    private void updateIndicatorWithTime() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                double currentTimeLeft = pettingMaster.getCurrentTimeLeft();
                int calculatedProgress = (int) Math.round((currentTimeLeft / timerStartValue) * timerMax);

                indicator.setProgressCompat(calculatedProgress, false);
                timerInSeconds.setText(String.valueOf(currentTimeLeft));

                if (currentTimeLeft <= 0)
                    isPlaying = false;

                if(!isPlaying)
                    handler.removeCallbacks(this);
                else
                    handler.postDelayed(this, pettingMaster.getMillisecondsPerUpdate());
            }
        };
        handler.post(runnable);
    }

    private void handleSquareClick(boolean wasClicked) {
        if (wasClicked)
            pettingMaster.handleClickedSquare();

        disappearTimeHandler.removeCallbacks(timerRunnable);
        timerRunnable = () -> handleSquareDisappearing();
        disappearTimeHandler.postDelayed(timerRunnable, (long) Math.round(pettingMaster.getCurrentSquareDisappearTime() * 1000));

        moveClickSquare();
    }

    private void handleSquareDisappearing() {
        pettingMaster.handleMissedSquare();
        handleSquareClick(false);
    }

    private void moveClickSquare() {
        Random rng = new Random();
        int xPos = rng.nextInt(xEnd - xStart) + xStart;
        int yPos = rng.nextInt(yEnd - yStart) + yStart;

        focusButton.setX(xPos);
        focusButton.setY(yPos);
    }

}

