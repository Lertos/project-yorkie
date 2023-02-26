package com.lertos.projectyorkie;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.lertos.projectyorkie.data.DataManager;

public class TournamentPage extends AppCompatActivity {

    private TournamentMaster tournamentMaster;
    private final int timerMax = 1000;
    private boolean isPlaying = false;
    static boolean isPageActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_tournament);

        if (!isPageActive) {
            updateUIWithCurrentHeartTokens();
            isPageActive = true;
        }

        tournamentMaster = new TournamentMaster();

        Helper.setupBottomButtonBar(this);
        setPlayerScoreDataUI();
        setOnClickListeners();
    }

    protected void onDestroy() {
        super.onDestroy();
        isPlaying = false;
        isPageActive = false;
    }

    protected void onPause() {
        super.onPause();
        isPlaying = false;
        isPageActive = false;
    }

    protected void onResume() {
        super.onResume();
        isPlaying = true;
        isPageActive = true;
    }

    private void setOnClickListeners() {
        //Start the petting mini game
        ((Button) findViewById(R.id.btnStart)).setOnClickListener(v -> {
            //Create a new instance of the petting mini game master
            tournamentMaster = new TournamentMaster();
            isPlaying = true;

            setupUI();

            //Start the mini game
            tournamentMaster.start();
        });
    }

    private void setupUI() {
        setPlayerScoreDataUI();
    }

    private void setPlayerScoreDataUI() {
        /*
        ((TextView) findViewById(R.id.tvHighestThreshold)).setText(
                Helper.createSpannable(
                        "Highest Threshold:",
                        " " + DataManager.getInstance().getPlayerData().getPettingHighestThreshold(),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        ((TextView) findViewById(R.id.tvHighestSquare)).setText(
                Helper.createSpannable(
                        "Highest Square:",
                        " " + DataManager.getInstance().getPlayerData().getPettingHighestSquare(),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        ((TextView) findViewById(R.id.tvStartingThreshold)).setText(
                Helper.createSpannable(
                        "Starting Threshold:",
                        " " + tournamentMaster.getStartThreshold(),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        ((Button) findViewById(R.id.btnStart)).setText(
                Helper.createSpannable(
                        "Start\n",
                        " " + IdleNumber.getStrNumber(tournamentMaster.getStartCost()) + " Tokens",
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);
         */
    }

    private void updateUIWithCurrentHeartTokens() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.tvCurrentHeartTokens)).setText(IdleNumber.getStrNumber(DataManager.getInstance().getPlayerData().getCurrentHeartTokens()));

                if (!isPageActive)
                    handler.removeCallbacks(this);
                else
                    handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }

}

