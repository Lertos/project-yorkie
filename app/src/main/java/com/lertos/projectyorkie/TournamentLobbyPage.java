package com.lertos.projectyorkie;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.MediaManager;
import com.lertos.projectyorkie.tournament.TournamentContestant;
import com.lertos.projectyorkie.tournament.TournamentMaster;

import java.util.List;

public class TournamentLobbyPage extends AppCompatActivity {

    static boolean isPageActive = false;
    private TournamentMaster tournamentMaster;
    private final int msToShowEachContestant = 750;
    private LinearLayout dogLayout1;
    private LinearLayout dogLayout2;
    private LinearLayout dogLayout3;
    private LinearLayout dogLayout4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_tournament_lobby);

        if (!isPageActive)
            isPageActive = true;

        String difficulty = getIntent().getStringExtra("STR_DIFFICULTY");
        double initialBet = getIntent().getDoubleExtra("DOUBLE_BET_AMOUNT", 0.0);

        tournamentMaster = new TournamentMaster(this, difficulty, initialBet);

        dogLayout1 = findViewById(R.id.linDog1);
        dogLayout2 = findViewById(R.id.linDog2);
        dogLayout3 = findViewById(R.id.linDog3);
        dogLayout4 = findViewById(R.id.linDog4);

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
            findViewById(R.id.relScreen).setVisibility(GONE);

            if (!tournamentMaster.isGameListEmpty())
                tournamentMaster.startNextGame();
        });

        findViewById(R.id.btnLeaveScoreScreen).setOnClickListener(v -> {
            currentGameEnded();
        });

        findViewById(R.id.btnLeaveTournament).setOnClickListener(v -> {
            super.finish();
        });

        findViewById(R.id.btnFinishTournament).setOnClickListener(v -> {
            super.finish();
        });
    }

    public void currentGameEnded() {
        if (tournamentMaster.isGameListEmpty())
            tournamentMaster.processEndOfTournament();

        refreshContestantUI();
        resetUIAfterGame();

        findViewById(R.id.relScreen).setVisibility(VISIBLE);
        findViewById(R.id.relGameOverScreen).setVisibility(GONE);
    }

    private void setupUI() {
        setupHeaderInfo();
        refreshContestantUI();
        resetUIAfterGame();
    }

    private void resetUIAfterGame() {
        changeBottomSectionInfo();
        hideBottomSectionInfo();
        showContestantsSlowly();
    }

    private void showContestantsSlowly() {
        dogLayout1.setAlpha(0);
        dogLayout2.setAlpha(0);
        dogLayout3.setAlpha(0);
        dogLayout4.setAlpha(0);

        MediaManager.getInstance().playEffectTrack(R.raw.effect_popup_1);

        dogLayout4.animate().alpha(1).setDuration(msToShowEachContestant).withEndAction(() -> {
            MediaManager.getInstance().playEffectTrack(R.raw.effect_popup_2);

            dogLayout3.animate().alpha(1).setDuration(msToShowEachContestant).withEndAction(() -> {
                MediaManager.getInstance().playEffectTrack(R.raw.effect_popup_3);

                dogLayout2.animate().alpha(1).setDuration(msToShowEachContestant).withEndAction(() -> {
                    MediaManager.getInstance().playEffectTrack(R.raw.effect_popup_4);

                    dogLayout1.animate().alpha(1).setDuration(msToShowEachContestant).withEndAction(() -> {
                        showBottomSectionInfo();
                    });
                });
            });
        });
    }

    private void hideBottomSectionInfo() {
        findViewById(R.id.linNextGame).setAlpha(0);
        findViewById(R.id.linButtonGroup).setAlpha(0);
    }

    private void showBottomSectionInfo() {
        findViewById(R.id.linNextGame).animate().alpha(1).setDuration(400);
        findViewById(R.id.linButtonGroup).animate().alpha(1).setDuration(400);
    }

    private void changeBottomSectionInfo() {
        TextView tvGameTitle = findViewById(R.id.tvNextGameTitle);

        if (tournamentMaster.isGameListEmpty()) {
            String playerPosition = tournamentMaster.getPlayerPosition();
            String endReward = IdleNumber.getStrNumber(tournamentMaster.getPlayerFinalReward());

            ((TextView) findViewById(R.id.tvNextGameHeader)).setText("You were " + playerPosition + " place");
            tvGameTitle.setText(endReward);

            findViewById(R.id.btnStartNextGame).setVisibility(GONE);
            findViewById(R.id.btnLeaveTournament).setVisibility(GONE);
            findViewById(R.id.btnFinishTournament).setVisibility(VISIBLE);
        } else
            tvGameTitle.setText(tournamentMaster.getCurrentGameName());
    }

    private void setupHeaderInfo() {
        ((TextView) findViewById(R.id.tvCurrentBracket)).setText(DataManager.getInstance().getPlayerData().getTournamentRank().getRankDisplay().toUpperCase());
        ((TextView) findViewById(R.id.tvDifficulty)).setText(tournamentMaster.getTournamentDifficulty().getDisplayStr().toUpperCase());
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