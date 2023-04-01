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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.MediaManager;

import java.util.Random;

public class PettingPage extends AppCompatActivity {

    private PettingMaster pettingMaster;
    private final Rect pettingLayout = new Rect();
    private ImageButton focusButton;
    private LinearProgressIndicator indicator;
    private final int timerMax = 1000;
    private TextView timerInSeconds;
    private TextView tvCurrentHeartTokens;
    private TextView tvCurrentHeartTokensPerSec;
    private double timerStartValue;
    private int xStart, yStart, xEnd, yEnd;
    private boolean isPlaying = false;
    final Handler disappearTimeHandler = new Handler();
    private Runnable timerRunnable;
    static boolean isPageActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_petting);

        if (!isPageActive) {
            updateUIWithCurrentData();
            isPageActive = true;
        }

        pettingMaster = new PettingMaster();

        focusButton = findViewById(R.id.btnPettingFocus);
        indicator = findViewById(R.id.indPettingTimer);
        timerInSeconds = findViewById(R.id.tvTimerInSecs);

        tvCurrentHeartTokens = findViewById(R.id.tvCurrentHeartTokens);
        tvCurrentHeartTokensPerSec = findViewById(R.id.tvCurrentHeartTokensPerSec);

        //This makes sure the progress moves smoothly. 100 max makes it decrease in a choppy manner
        indicator.setMax(timerMax);

        LinearLayout layout = findViewById(R.id.linMainSection);
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

                findViewById(R.id.linGameScreen).setVisibility(View.GONE);
            }
        });

        Helper.setupBottomButtonBar(this);
        setPlayerScoreDataUI();
        setOnClickListeners();
    }

    protected void onDestroy() {
        super.onDestroy();
        isPlaying = false;
        isPageActive = false;
        MediaManager.getInstance().stopSong();
    }

    protected void onPause() {
        super.onPause();
        isPlaying = false;
        isPageActive = false;

        if (!DataManager.getInstance().switchedScreens) {
            MediaManager.getInstance().pauseSong();
            DataManager.getInstance().setMinimized(true);
        }
        DataManager.getInstance().switchedScreens = false;
    }

    protected void onResume() {
        super.onResume();
        isPlaying = true;
        isPageActive = true;

        if (DataManager.getInstance().isMinimized()) {
            new PopupTimeAway(this, R.id.relScreen);
            DataManager.getInstance().setMinimized(false);
        }
        updateUIWithCurrentData();
        MediaManager.getInstance().startSong();
    }

    private void setOnClickListeners() {
        //Start the petting mini game
        findViewById(R.id.btnStart).setOnClickListener(v -> {
            //Create a new instance of the petting mini game master
            pettingMaster = new PettingMaster();

            //Check if the player can afford to play
            double cost = pettingMaster.getStartCost();
            boolean canAfford = Helper.canAffordHeartTokens(cost);

            if (!canAfford) {
                Toast.makeText(this, "You do not have enough heart tokens", Toast.LENGTH_SHORT).show();
                return;
            }

            timerStartValue = pettingMaster.getTimerStartValue();
            isPlaying = true;

            setupUI();

            //Start the mini game
            pettingMaster.start();
        });

        findViewById(R.id.btnPettingFocus).setOnClickListener(v -> handleSquareClick(true, false));
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
        pettingMaster = new PettingMaster();

        ((TextView) findViewById(R.id.tvHighestThreshold)).setText(
                Helper.createSpannable(
                        "Threshold:",
                        " " + DataManager.getInstance().getPlayerData().getPettingHighestThreshold(),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        ((TextView) findViewById(R.id.tvHighestSquare)).setText(
                Helper.createSpannable(
                        "Square:",
                        " " + DataManager.getInstance().getPlayerData().getPettingHighestSquare(),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        ((TextView) findViewById(R.id.tvStartingThreshold)).setText(
                Helper.createSpannable(
                        "Start Threshold:",
                        " " + pettingMaster.getStartThreshold(),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        ((Button) findViewById(R.id.btnStart)).setText(
                Helper.createSpannable(
                        "Start\n",
                        IdleNumber.getStrNumber(pettingMaster.getStartCost()) + " Tokens",
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);
    }

    private void processGameStart() {
        setPlayerScoreDataUI();

        findViewById(R.id.linHeartTokens).setVisibility(View.GONE);
        findViewById(R.id.linStartScreen).setVisibility(View.GONE);
        findViewById(R.id.linGameScreen).setVisibility(View.VISIBLE);

        //Show the generated square
        focusButton.setVisibility(View.VISIBLE);
    }

    private void processGameOver() {
        setPlayerScoreDataUI();

        MediaManager.getInstance().playEffectTrack(R.raw.effect_end_screen);

        findViewById(R.id.linHeartTokens).setVisibility(View.VISIBLE);
        findViewById(R.id.linStartScreen).setVisibility(View.VISIBLE);
        findViewById(R.id.linGameScreen).setVisibility(View.GONE);

        //Show the reward won
        TextView tvRewardAmount = findViewById(R.id.tvRewardAmount);

        findViewById(R.id.tvRewardHeader).setVisibility(View.VISIBLE);

        tvRewardAmount.setVisibility(View.VISIBLE);
        tvRewardAmount.setAlpha(0);

        tvRewardAmount.animate().alpha(1).scaleX(3).scaleY(3).setDuration(1200).withEndAction(() -> tvRewardAmount.animate().scaleX(1).scaleY(1).setDuration(400));

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
                    ((TextView) findViewById(R.id.tvRewardAmount)).setText(IdleNumber.getStrNumber(pettingMaster.getEndReward()));
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

        focusButton.setAlpha(1f);
        disappearTimeHandler.removeCallbacks(timerRunnable);

        if (isPlaying) {
            moveClickSquare();

            long timeToDisappear = Math.round(pettingMaster.getCurrentSquareDisappearTime() * 1000);
            focusButton.animate().alpha(0f).setDuration(timeToDisappear);

            timerRunnable = this::handleSquareDisappearing;
            disappearTimeHandler.postDelayed(timerRunnable, timeToDisappear);
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

    private void updateUIWithCurrentData() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tvCurrentHeartTokens.setText(IdleNumber.getStrNumber(DataManager.getInstance().getPlayerData().getCurrentHeartTokens()));
                tvCurrentHeartTokensPerSec.setText(String.format("%.2f", DataManager.getInstance().getPlayerData().getCurrentHeartTokensPerSecond()));

                if (!isPageActive)
                    handler.removeCallbacks(this);
                else
                    handler.postDelayed(this, 500);
            }
        };
        handler.post(runnable);
    }

}

