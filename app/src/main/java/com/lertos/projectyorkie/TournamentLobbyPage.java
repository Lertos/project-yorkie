package com.lertos.projectyorkie;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.tournament.TournamentContestant;
import com.lertos.projectyorkie.tournament.TournamentMaster;
import com.lertos.projectyorkie.tournament.games.WhackTheRat;

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

        tournamentMaster = new TournamentMaster(difficulty, initialBet);

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
            //TODO: Load the next game in the TournamentMaster game list
            findViewById(R.id.relScreen).setVisibility(View.GONE);

            ViewStub stub = findViewById(R.id.viewStubGame);
            stub.setLayoutResource(R.layout.page_game_test);
            View inflatedStub = stub.inflate();

            ((TextView) findViewById(R.id.tvHeader)).setText("Updated");

            WhackTheRat game = new WhackTheRat(inflatedStub);
            game.startGame();
        });

        findViewById(R.id.btnLeaveTournament).setOnClickListener(v -> {
            super.finish();
        });
    }

    private void setupUI() {
        setupHeaderInfo();
        refreshContestantUI();
    }

    private void setupHeaderInfo() {
        ((TextView) findViewById(R.id.tvCurrentBracket)).setText(
                Helper.createSpannable(
                        "Bracket: ",
                        DataManager.getInstance().getPlayerData().getTournamentRank().getRankDisplay(),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        ((TextView) findViewById(R.id.tvDifficulty)).setText(
                Helper.createSpannable(
                        "Difficulty: ",
                        tournamentMaster.getTournamentDifficulty().getDisplayStr(),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        ((TextView) findViewById(R.id.tvBetAmount)).setText(
                Helper.createSpannable(
                        "Bet Amount: ",
                        IdleNumber.getStrNumber(tournamentMaster.getInitialBet()),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);
    }

    private void refreshContestantUI() {
        //Decided to use numbered elements instead of RecyclerView as it seems overkill for a static amount of NPCs
        List<TournamentContestant> contestantList = tournamentMaster.getContestants();

        setupContestant(
                contestantList.get(0),
                findViewById(R.id.ivDog1),
                findViewById(R.id.tvDogType1),
                findViewById(R.id.tvDogRecentScore1),
                findViewById(R.id.tvDogTotalScore1)
        );

        setupContestant(
                contestantList.get(1),
                findViewById(R.id.ivDog2),
                findViewById(R.id.tvDogType2),
                findViewById(R.id.tvDogRecentScore2),
                findViewById(R.id.tvDogTotalScore2)
        );

        setupContestant(
                contestantList.get(2),
                findViewById(R.id.ivDog3),
                findViewById(R.id.tvDogType3),
                findViewById(R.id.tvDogRecentScore3),
                findViewById(R.id.tvDogTotalScore3)
        );

        setupContestant(
                contestantList.get(3),
                findViewById(R.id.ivDog4),
                findViewById(R.id.tvDogType4),
                findViewById(R.id.tvDogRecentScore4),
                findViewById(R.id.tvDogTotalScore4)
        );
    }

    private void setupContestant(TournamentContestant contestant, View ivAvatar, View tvDogType, View tvCurrentScore, View tvTotalScore) {
        ((ImageView) ivAvatar).setImageResource(contestant.getPackDog().getAvatar());

        ((TextView) tvDogType).setText(
                Helper.createSpannable(
                        "Type: ",
                        contestant.getPackDog().getName(),
                        ContextCompat.getColor(this, R.color.gold)
                ),
                TextView.BufferType.SPANNABLE);

        ((TextView) tvCurrentScore).setText(
                Helper.createSpannable(
                        "Round Score: ",
                        String.valueOf(contestant.getCurrentScore()),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        ((TextView) tvTotalScore).setText(
                Helper.createSpannable(
                        "Total Score: ",
                        String.valueOf(contestant.getTotalScore()),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);
    }
}