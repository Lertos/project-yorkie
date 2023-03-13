package com.lertos.projectyorkie;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.MediaManager;
import com.lertos.projectyorkie.model.PackDog;
import com.lertos.projectyorkie.tournament.TournamentContestant;
import com.lertos.projectyorkie.tournament.TournamentMaster;

import java.util.ArrayList;
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

        findViewById(R.id.btnSeeEndResults).setOnClickListener(v -> {
            showTournamentEndScreen();
        });

        findViewById(R.id.btnEndTournament).setOnClickListener(v -> {
            super.finish();
        });
    }

    private void showTournamentEndScreen() {
        findViewById(R.id.relScreen).setVisibility(GONE);
        findViewById(R.id.relTournamentEndScreen).setVisibility(VISIBLE);

        updateTournamentEndScreenInfo();

        if (DataManager.getInstance().getSettings().isShowAppearAnimationsInTournament()) {
            animateTournamentEndScreenInfo();
        }
    }

    private void updateTournamentEndScreenInfo() {
        String playerPosition = tournamentMaster.getPlayerPosition();
        String initialBet = IdleNumber.getStrNumber(tournamentMaster.getInitialBet());
        String endReward = IdleNumber.getStrNumber(tournamentMaster.getPlayerFinalReward());

        ((TextView) findViewById(R.id.tvFinalPosition)).setText(playerPosition + " place");
        ((TextView) findViewById(R.id.tvInitialBet)).setText(initialBet);
        ((TextView) findViewById(R.id.tvHeartsGained)).setText(endReward);

        int rankDirection = tournamentMaster.getRankDirection();

        //If there is no rank movement
        if (rankDirection == 0) {
            ((TextView) findViewById(R.id.tvPreviousRank)).setText("NONE");
            findViewById(R.id.tvRankChangeSpacer).setVisibility(GONE);
            findViewById(R.id.tvNewRank).setVisibility(GONE);
        }
        //If there is rank movement
        else {
            ((TextView) findViewById(R.id.tvPreviousRank)).setText(tournamentMaster.getPreviousRank());
            ((TextView) findViewById(R.id.tvNewRank)).setText(tournamentMaster.getNewRank());
        }

        PackDog randomDog = tournamentMaster.getRandomDog();

        //If the player placed below first, they did not unlock a dog
        if (randomDog == null) {
            findViewById(R.id.ivDogUnlocked).setVisibility(GONE);
            ((TextView) findViewById(R.id.tvUnlockedDog)).setText("Place 1st to unlock new dogs");
        }
        //If the player won first place and unlocked a dog
        else {
            ((ImageView) findViewById(R.id.ivDogUnlocked)).setImageResource(randomDog.getAvatar());

            //If they already unlocked it
            if (randomDog.isUnlocked()) {
                ((TextView) findViewById(R.id.tvUnlockedDog)).setText("Already Unlocked...");
            } else {
                ((TextView) findViewById(R.id.tvUnlockedDog)).setText("NEW UNLOCK: " + randomDog.getName());
                randomDog.setUnlocked(true);
            }
        }
    }

    private void animateTournamentEndScreenInfo() {
        int timePerAction = 800;

        ViewGroup linTournamentEndScreen = findViewById(R.id.linTournamentEndScreen);
        ArrayList<View> views = new ArrayList<>();

        for (int i = 0; i < linTournamentEndScreen.getChildCount(); i++) {
            View childView = linTournamentEndScreen.getChildAt(i);

            childView.setAlpha(0);
            views.add(childView);
        }

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (views.isEmpty())
                    return;

                View currentView = views.get(0);

                views.remove(0);

                currentView.animate().alpha(1).setDuration(timePerAction).withEndAction(() -> {
                    MediaManager.getInstance().playEffectTrack(R.raw.effect_correct);
                });

                handler.postDelayed(this, timePerAction);
            }
        };
        handler.post(runnable);
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

        if (DataManager.getInstance().getSettings().isShowAppearAnimationsInTournament()) {
            showContestantsSlowly();
        } else {
            showBottomSectionInfo();
        }
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
        if (DataManager.getInstance().getSettings().isShowAppearAnimationsInTournament()) {
            findViewById(R.id.linNextGame).animate().alpha(1).setDuration(400);
            findViewById(R.id.linButtonGroup).animate().alpha(1).setDuration(400);
        } else {
            findViewById(R.id.linNextGame).setAlpha(1);
            findViewById(R.id.linButtonGroup).setAlpha(1);
        }
    }

    private void changeBottomSectionInfo() {
        TextView tvGameTitle = findViewById(R.id.tvNextGameTitle);

        if (tournamentMaster.isGameListEmpty()) {
            findViewById(R.id.linNextGame).setVisibility(View.INVISIBLE);

            findViewById(R.id.btnStartNextGame).setVisibility(GONE);
            findViewById(R.id.btnLeaveTournament).setVisibility(GONE);
            findViewById(R.id.btnSeeEndResults).setVisibility(VISIBLE);
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