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
import com.lertos.projectyorkie.tournament.TournamentDifficulty;
import com.lertos.projectyorkie.tournament.TournamentRank;

public class TournamentPage extends AppCompatActivity {

    static boolean isPageActive = false;
    private Toast toastMsg;
    private Slider sliderBetAmount;
    private TextView tvBetAmount;
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

        Helper.setupBottomButtonBar(this);
        setOnClickListeners();
        setupUI();
    }

    protected void onDestroy() {
        super.onDestroy();
        isPageActive = false;
    }

    protected void onPause() {
        super.onPause();
        isPageActive = false;
    }

    protected void onResume() {
        super.onResume();
        isPageActive = true;
    }

    private void setOnClickListeners() {
        ((Button) findViewById(R.id.btnMoveToLobby)).setOnClickListener(v -> {
            if (difficulty == null) {
                if (toastMsg != null)
                    toastMsg.cancel();

                toastMsg = Toast.makeText(v.getContext(), "You must choose a difficulty", Toast.LENGTH_SHORT);
                toastMsg.show();
                return;
            }

            Intent intent = new Intent(this, TournamentLobbyPage.class);
            intent.putExtra("STR_DIFFICULTY", difficulty.getDisplayStr());
            intent.putExtra("DOUBLE_BET_AMOUNT", (Double) heartsBet);
            startActivity(intent);
        });

        findViewById(R.id.rbDifficultyEasy).setOnClickListener(v -> {
            pickDifficultyOption(TournamentDifficulty.EASY);
        });
        findViewById(R.id.rbDifficultyNormal).setOnClickListener(v -> {
            pickDifficultyOption(TournamentDifficulty.NORMAL);
        });
        findViewById(R.id.rbDifficultyHard).setOnClickListener(v -> {
            pickDifficultyOption(TournamentDifficulty.HARD);
        });

        sliderBetAmount.addOnChangeListener((slider, value, fromUser) -> {
            double currentHearts = DataManager.getInstance().getPlayerData().getCurrentHearts();
            double percentBet = value / 100;

            heartsBet = currentHearts * percentBet;
            tvBetAmount.setText(IdleNumber.getStrNumber(heartsBet));
        });
    }

    private void setupUI() {
        TournamentRank rank = DataManager.getInstance().getPlayerData().getTournamentRank();
        double costInTokens = rank.getTokenCostForRank();

        //Choose a default value for the difficulty
        ((RadioButton) findViewById(R.id.rbDifficultyNormal)).setChecked(true);
        pickDifficultyOption(TournamentDifficulty.NORMAL);

        ((Button) findViewById(R.id.btnMoveToLobby)).setText(
                Helper.createSpannable(
                        "Join ",
                        IdleNumber.getStrNumber(costInTokens) + " Tokens",
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        //Set the initial value so that the onClick listener fires to load initial bet value
        sliderBetAmount.setValue(50);
    }

    private void pickDifficultyOption(TournamentDifficulty chosenDifficulty) {
        difficulty = chosenDifficulty;
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

