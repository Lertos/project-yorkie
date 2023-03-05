package com.lertos.projectyorkie.tournament;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import androidx.appcompat.app.AppCompatActivity;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.TournamentLobbyPage;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.model.PackDog;
import com.lertos.projectyorkie.tournament.games.WhackTheCat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TournamentMaster {

    private enum TournamentState {
        LOBBY,
        IN_GAME,
        POST_GAME,
        POST_TOURNAMENT
    }
    private final TournamentLobbyPage lobbyPage;
    private View inflatedStub;
    private final int maxAIContestants = 3;
    private TournamentState currentState;
    private final ArrayList<TournamentContestant> contestants;
    private final TournamentDifficulty tournamentDifficulty;
    private final double initialBet;

    public TournamentMaster(TournamentLobbyPage lobbyPage, String difficulty, double initialBet) {
        this.lobbyPage = lobbyPage;
        this.currentState = TournamentState.LOBBY;
        this.contestants = createContestants();
        this.tournamentDifficulty = getDifficultyFromString(difficulty);
        this.initialBet = initialBet;
    }

    private TournamentDifficulty getDifficultyFromString(String str) {
        for (TournamentDifficulty difficulty : TournamentDifficulty.values()) {
            if (str.equalsIgnoreCase(difficulty.getDisplayStr()))
                return difficulty;
        }
        return null;
    }

    private ArrayList<TournamentContestant> createContestants() {
        ArrayList<TournamentContestant> contestantList = new ArrayList<>();

        PackDog player = new PackDog("Yorkie", R.mipmap.portrait_loki);
        contestantList.add(new TournamentContestant(player, true));

        for (int i=0; i < maxAIContestants; i++) {
            PackDog newPackDog = null;
            boolean alreadyExists = true;

            while (alreadyExists) {
                newPackDog = DataManager.getInstance().getRandomPackDog();
                alreadyExists = false;

                for (TournamentContestant contestant : contestantList) {
                    if (newPackDog.equals(contestant.getPackDog())) {
                        alreadyExists = true;
                        break;
                    }
                }
            }
            contestantList.add(new TournamentContestant(newPackDog, false));
        }
        return contestantList;
    }

    public List<TournamentContestant> getContestants() {
        contestants.sort(new SortByScore());
        return Collections.unmodifiableList(contestants);
    }

    public TournamentDifficulty getTournamentDifficulty() {
        return tournamentDifficulty;
    }

    public double getInitialBet() {
        return initialBet;
    }

    public void startNextGame(AppCompatActivity view) {
        //TODO: Load from a list, randomly
        ViewStub stub = view.findViewById(R.id.viewStubGame);
        stub.setLayoutResource(R.layout.page_game_whack_the_cat);
        inflatedStub = stub.inflate();

        WhackTheCat game = new WhackTheCat(this, tournamentDifficulty, inflatedStub);
        game.startGame();
    }

    //TODO: Add method to switch between states and handle the visibility of screens there instead
    public void showEndGameScreen() {
        //Remove the embedded game screen
        ((ViewGroup) inflatedStub.getParent()).removeView(inflatedStub);

        //Show the end game screen with the score
        lobbyPage.findViewById(R.id.relGameOverScreen).setVisibility(View.VISIBLE);
    }

    public class SortByScore implements Comparator<TournamentContestant> {

        @Override
        public int compare(TournamentContestant c1, TournamentContestant c2) {
            double totalScore1 = c1.getTotalScore();
            double totalScore2 = c2.getTotalScore();

            int totalScoreCompare = Double.compare(totalScore1, totalScore2);

            if (totalScoreCompare != 0)
                return totalScoreCompare;

            double currentScore1 = c1.getCurrentScore();
            double currentScore2 = c2.getCurrentScore();

            return Double.compare(currentScore1, currentScore2);
        }

    }

}