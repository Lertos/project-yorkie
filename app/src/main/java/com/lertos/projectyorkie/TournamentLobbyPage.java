package com.lertos.projectyorkie;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.tournament.TournamentContestant;
import com.lertos.projectyorkie.tournament.TournamentMaster;

import java.util.List;

public class TournamentLobbyPage extends AppCompatActivity {

    static boolean isPageActive = false;
    private TournamentMaster tournamentMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_tournament_lobby);

        if (!isPageActive)
            isPageActive = true;

        String difficulty = getIntent().getStringExtra("STR_DIFFICULTY");
        double initialBet = getIntent().getDoubleExtra("DOUBLE_BET_AMOUNT", 0.0);

        tournamentMaster = new TournamentMaster(this, difficulty, initialBet);

        setupUI();
        setOnClickListeners();
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
        findViewById(R.id.btnStartNextGame).setOnClickListener(v -> {
            findViewById(R.id.relScreen).setVisibility(View.GONE);

            tournamentMaster.startNextGame(this);
        });

        findViewById(R.id.btnLeaveScoreScreen).setOnClickListener(v -> {
            currentGameEnded();
        });

        findViewById(R.id.btnLeaveTournament).setOnClickListener(v -> {
            super.finish();
        });
    }

    public void currentGameEnded() {
        refreshContestantUI();

        findViewById(R.id.relScreen).setVisibility(View.VISIBLE);
        findViewById(R.id.relGameOverScreen).setVisibility(View.GONE);
    }

    private void setupUI() {
        setupHeaderInfo();
        refreshContestantUI();
    }

    private void setupHeaderInfo() {
        ((TextView) findViewById(R.id.tvCurrentBracket)).setText(DataManager.getInstance().getPlayerData().getTournamentRank().getRankDisplay().toUpperCase());
        ((TextView) findViewById(R.id.tvDifficulty)).setText(tournamentMaster.getTournamentDifficulty().getDisplayStr().toUpperCase());
        ((TextView) findViewById(R.id.tvBetAmount)).setText(IdleNumber.getStrNumber(tournamentMaster.getInitialBet()));
    }

    private void refreshContestantUI() {
        //Decided to use numbered elements instead of RecyclerView as it seems overkill for a static amount of NPCs
        List<TournamentContestant> contestantList = tournamentMaster.getContestants();

        setupContestant(
                contestantList.get(0),
                findViewById(R.id.ivDog1),
                findViewById(R.id.tvDogType1),
                findViewById(R.id.tvDogRecentScore1)
        );

        setupContestant(
                contestantList.get(1),
                findViewById(R.id.ivDog2),
                findViewById(R.id.tvDogType2),
                findViewById(R.id.tvDogRecentScore2)
        );

        setupContestant(
                contestantList.get(2),
                findViewById(R.id.ivDog3),
                findViewById(R.id.tvDogType3),
                findViewById(R.id.tvDogRecentScore3)
        );

        setupContestant(
                contestantList.get(3),
                findViewById(R.id.ivDog4),
                findViewById(R.id.tvDogType4),
                findViewById(R.id.tvDogRecentScore4)
        );
    }

    private void setupContestant(TournamentContestant contestant, View ivAvatar, View tvDogType, View tvCurrentScore) {
        ((ImageView) ivAvatar).setImageResource(contestant.getPackDog().getAvatar());

        ((TextView) tvDogType).setText(contestant.getPackDog().getName());

        ((TextView) tvCurrentScore).setText(
                Helper.createSpannable(
                        "SCORE: ",
                        contestant.getCurrentScore() + " (" + contestant.getTotalScore() + ")",
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);
    }
}