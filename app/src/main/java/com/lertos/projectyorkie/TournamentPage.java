package com.lertos.projectyorkie;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;
import com.lertos.projectyorkie.data.DataManager;

public class TournamentPage extends AppCompatActivity {

    private TournamentMaster tournamentMaster;
    private Slider sliderBetAmount;
    private TextView tvBetAmount;
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
        sliderBetAmount = findViewById(R.id.sliderBetAmount);
        tvBetAmount = findViewById(R.id.tvBetAmount);

        Helper.setupBottomButtonBar(this);
        setupUI();
        setOnClickListeners();

        //Set the initial value so that the onClick listener fires to load initial bet value
        sliderBetAmount.setValue(50);
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
            //TODO: Switch to the lobby and pass through the tournamentMaster object
        });

        ((Button) findViewById(R.id.btnDifficultyMenu)).setOnClickListener(v -> {
            showMenu(v, R.menu.difficulty_popup);
        });

        sliderBetAmount.addOnChangeListener((slider, value, fromUser) -> {
            double currentHearts = DataManager.getInstance().getPlayerData().getCurrentHearts();
            double percentBet = value / 100;

            //TODO: Set the amount in the TournamentMaster object
            tvBetAmount.setText(IdleNumber.getStrNumber(currentHearts * percentBet));
        });
    }

    private void showMenu(View view, int menuRes) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());

        popup.setOnMenuItemClickListener( menuItem -> {
            switch (menuItem.getItemId()) {
                //TODO: Need to set the difficulty inside the TournamentMaster object
                case R.id.optionEasy: {
                    ((Button) findViewById(R.id.btnDifficultyMenu)).setText("Easy");
                    break;
                }
                case R.id.optionNormal: {
                    ((Button) findViewById(R.id.btnDifficultyMenu)).setText("Normal");
                    break;
                }
                case R.id.optionHard: {
                    ((Button) findViewById(R.id.btnDifficultyMenu)).setText("Hard");
                    break;
                }
            }
            return true;
        });
        popup.show();
    }

    private void setupUI() {
        //TODO: Set actual bracket from tournamentMaster
        ((TextView) findViewById(R.id.tvCurrentBracket)).setText("Silver II");

        ((Button) findViewById(R.id.btnStart)).setText(
                Helper.createSpannable(
                        "Join Tournament\n",
                        IdleNumber.getStrNumber(tournamentMaster.getStartCost()) + " Tokens",
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);
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

