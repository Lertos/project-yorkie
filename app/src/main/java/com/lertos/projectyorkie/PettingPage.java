package com.lertos.projectyorkie;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.MediaManager;

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

        pettingMaster = new PettingMaster();

        focusButton = findViewById(R.id.btnPettingFocus);
        indicator = findViewById(R.id.indPettingTimer);
        timerInSeconds = findViewById(R.id.tvPettingTimerInSecs);

        //This makes sure the progress moves smoothly. 100 max makes it decrease in a choppy manner
        indicator.setMax(timerMax);

        LinearLayout layout = (LinearLayout) findViewById(R.id.linPettingMainSection);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                layout.getGlobalVisibleRect(pettingLayout);

                int buttonHeight = focusButton.getMaxHeight();
                int layoutMargin = 60;

                xStart = pettingLayout.left;
                yStart = pettingLayout.top - layoutMargin;
                xEnd = pettingLayout.right - buttonHeight;
                yEnd = pettingLayout.bottom - layoutMargin - buttonHeight;

                findViewById(R.id.linPettingGameScreen).setVisibility(View.GONE);
            }
        });

        Helper.setupBottomButtonBar(this);
        setPlayerScoreDataUI();
        setOnClickListeners();
    }

    protected void onDestroy() {
        super.onDestroy();
        isPlaying = false;
    }

    protected void onPause() {
        super.onPause();
        isPlaying = false;
    }

    protected void onResume() {
        super.onResume();
        isPlaying = true;
    }

    private void setOnClickListeners() {
        //Start the petting mini game
        ((Button) findViewById(R.id.btnStartPetting)).setOnClickListener(v -> {
            //Create a new instance of the petting mini game master
            pettingMaster = new PettingMaster();
            timerStartValue = pettingMaster.getTimerStartValue();
            isPlaying = true;

            setupUI();

            //Start the mini game
            pettingMaster.start();
        });

        ((ImageButton) findViewById(R.id.btnPettingFocus)).setOnClickListener(v -> handleSquareClick(true, false));
    }

    private void setupUI() {
        //Create the first square
        handleSquareClick(false, true);

        //Initialize the timer and the UI for it
        indicator.setProgressCompat(100, false);
        updateIndicatorWithTime();

        //Handle visibility of the rest of the UI
        processGameStart();
    }

    private void setPlayerScoreDataUI() {
        ((TextView) findViewById(R.id.pettingCurrentHeartTokens)).setText(
                Helper.createSpannable(
                        "",
                        IdleNumber.getStrNumber(DataManager.getInstance().getPlayerData().getCurrentHeartTokens()),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        ((TextView) findViewById(R.id.tvPettingHighestThreshold)).setText(
                Helper.createSpannable(
                        "Highest Threshold:",
                        " " + DataManager.getInstance().getPlayerData().getPettingHighestThreshold(),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        ((TextView) findViewById(R.id.tvPettingHighestSquare)).setText(
                Helper.createSpannable(
                        "Highest Square:",
                        " " + DataManager.getInstance().getPlayerData().getPettingHighestSquare(),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        ((TextView) findViewById(R.id.tvPettingStartingThreshold)).setText(
                Helper.createSpannable(
                        "Starting Threshold:",
                        " " + pettingMaster.getStartThreshold(),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        ((Button) findViewById(R.id.btnStartPetting)).setText(
                Helper.createSpannable(
                        "Start\n",
                        " " + IdleNumber.getStrNumber(pettingMaster.getStartCost()) + " Tokens",
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);
    }

    private void processGameStart() {
        setPlayerScoreDataUI();

        findViewById(R.id.linPettingStartScreen).setVisibility(View.GONE);
        findViewById(R.id.linPettingGameScreen).setVisibility(View.VISIBLE);

        //Show the generated square
        focusButton.setVisibility(View.VISIBLE);
    }

    private void processGameOver() {
        setPlayerScoreDataUI();

        findViewById(R.id.linPettingStartScreen).setVisibility(View.VISIBLE);
        findViewById(R.id.linPettingGameScreen).setVisibility(View.GONE);

        //Show the reward won
        findViewById(R.id.tvPettingRewardHeader).setVisibility(View.VISIBLE);
        findViewById(R.id.tvPettingRewardAmount).setVisibility(View.VISIBLE);

        //Hide the generated square
        focusButton.setVisibility(View.INVISIBLE);
    }

    private void updateIndicatorWithTime() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                double currentTimeLeft = pettingMaster.getCurrentTimeLeft();
                int calculatedProgress = (int) Math.round((currentTimeLeft / timerStartValue) * timerMax);

                indicator.setProgressCompat(calculatedProgress, false);
                timerInSeconds.setText(IdleNumber.getStrNumber(currentTimeLeft));

                if (currentTimeLeft <= 0) {
                    isPlaying = false;
                    pettingMaster.stop();
                    ((TextView) findViewById(R.id.tvPettingRewardAmount)).setText(IdleNumber.getStrNumber(pettingMaster.getEndReward()));
                    processGameOver();
                }

                if (!isPlaying)
                    handler.removeCallbacks(this);
                else
                    handler.postDelayed(this, pettingMaster.getMillisecondsPerUpdate());
            }
        };
        handler.post(runnable);
    }

    private void handleSquareClick(boolean wasClicked, boolean gameJustStarted) {
        if (wasClicked) {
            MediaManager.getInstance().playEffectTrack(R.raw.effect_correct);
            pettingMaster.handleClickedSquare();
        } else {
            if (!gameJustStarted && isPlaying)
                MediaManager.getInstance().playEffectTrack(R.raw.effect_miss);
        }

        disappearTimeHandler.removeCallbacks(timerRunnable);

        if (isPlaying) {
            timerRunnable = () -> handleSquareDisappearing();
            disappearTimeHandler.postDelayed(timerRunnable, (long) Math.round(pettingMaster.getCurrentSquareDisappearTime() * 1000));

            moveClickSquare();
        }
    }

    private void handleSquareDisappearing() {
        pettingMaster.handleMissedSquare();
        handleSquareClick(false, false);
    }

    private void moveClickSquare() {
        Random rng = new Random();
        int xPos = rng.nextInt(xEnd - xStart) + xStart;
        int yPos = rng.nextInt(yEnd - yStart) + yStart;

        focusButton.setX(xPos);
        focusButton.setY(yPos);
    }

}

