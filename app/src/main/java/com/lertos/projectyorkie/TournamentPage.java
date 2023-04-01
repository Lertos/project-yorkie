package com.lertos.projectyorkie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.MediaManager;
import com.lertos.projectyorkie.tournament.TournamentDifficulty;

public class TournamentPage extends AppCompatActivity {

    static boolean isPageActive = false;
    private Toast toastMsg;
    private Slider sliderBetAmount;
    private TextView tvCurrentRank;
    private TextView tvBetAmount;
    private TextView tvCurrentHeartTokens;
    private TextView tvCurrentHeartTokensPerSec;
    private TournamentDifficulty difficulty;
    private double heartsBet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_tournament);

        if (!isPageActive) {
            updateUIWithCurrentHeartTokens();
            isPageActive = true;
        }

        sliderBetAmount = findViewById(R.id.sliderBetAmount);
        tvBetAmount = findViewById(R.id.tvBetAmount);

        tvCurrentRank = findViewById(R.id.tvCurrentRank);
        tvCurrentHeartTokens = findViewById(R.id.tvCurrentHeartTokens);
        tvCurrentHeartTokensPerSec = findViewById(R.id.tvCurrentHeartTokensPerSec);

        Helper.setupBottomButtonBar(this);
        setOnClickListeners();
        setupUI();
    }

    protected void onDestroy() {
        super.onDestroy();
        isPageActive = false;
        MediaManager.getInstance().stopSong();
    }

    protected void onPause() {
        super.onPause();
        isPageActive = false;

        if (!DataManager.getInstance().switchedScreens) {
            MediaManager.getInstance().pauseSong();
            DataManager.getInstance().setMinimized(true);
        }
        DataManager.getInstance().switchedScreens = false;
    }

    protected void onResume() {
        super.onResume();

        if (!isPageActive) {
            updateUIWithCurrentHeartTokens();
            isPageActive = true;
        }

        if (DataManager.getInstance().isMinimized()) {
            if (DataManager.getInstance().getTimeAwayTotalTime() != null)
                new PopupTimeAway(this, R.id.relScreen);
            DataManager.getInstance().setMinimized(false);
        }

        MediaManager.getInstance().startSong();
        setupUI();
    }

    private void setOnClickListeners() {
        findViewById(R.id.btnMoveToLobby).setOnClickListener(v -> {
            if (difficulty == null) {
                if (toastMsg != null)
                    toastMsg.cancel();

                toastMsg = Toast.makeText(v.getContext(), "You must choose a difficulty", Toast.LENGTH_SHORT);
                toastMsg.show();
                return;
            }

            //Check if the player can afford to play
            double cost = getTokenCostForRank();
            boolean canAfford = Helper.canAffordHeartTokens(cost);

            if (!canAfford) {
                Toast.makeText(this, "You do not have enough heart tokens", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, TournamentLobbyPage.class);
            intent.putExtra("STR_DIFFICULTY", difficulty.getDisplayStr());
            intent.putExtra("DOUBLE_BET_AMOUNT", (Double) heartsBet);

            DataManager.getInstance().switchedScreens = true;

            startActivity(intent);
        });

        findViewById(R.id.rbDifficultyEasy).setOnClickListener(v -> pickDifficultyOption(TournamentDifficulty.EASY));
        findViewById(R.id.rbDifficultyNormal).setOnClickListener(v -> pickDifficultyOption(TournamentDifficulty.NORMAL));
        findViewById(R.id.rbDifficultyHard).setOnClickListener(v -> pickDifficultyOption(TournamentDifficulty.HARD));

        sliderBetAmount.addOnChangeListener((slider, value, fromUser) -> {
            double currentHearts = DataManager.getInstance().getPlayerData().getCurrentHearts();
            double percentBet = value / 100;

            heartsBet = currentHearts * percentBet;
            tvBetAmount.setText(IdleNumber.getStrNumber(heartsBet));
        });
    }

    private void setupUI() {
        //Choose a default value for the difficulty
        ((RadioButton) findViewById(R.id.rbDifficultyNormal)).setChecked(true);
        pickDifficultyOption(TournamentDifficulty.NORMAL);

        tvCurrentRank.setText(DataManager.getInstance().getPlayerData().getTournamentRank().getRankDisplay());

        ((Button) findViewById(R.id.btnMoveToLobby)).setText(
                Helper.createSpannable(
                        "Join\n",
                        IdleNumber.getStrNumber(getTokenCostForRank()) + " Tokens",
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        //Set the initial value so that the onClick listener fires to load initial bet value
        sliderBetAmount.setValue(50);
    }

    private double getTokenCostForRank() {
        return DataManager.getInstance().getPlayerData().getTournamentRank().getTokenCostForRank();
    }

    private void pickDifficultyOption(TournamentDifficulty chosenDifficulty) {
        difficulty = chosenDifficulty;
    }

    private void updateUIWithCurrentHeartTokens() {
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

