package com.lertos.projectyorkie.tournament;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.model.PackDog;

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
    private final int maxAIContestants = 3;
    private TournamentState currentState;
    private final ArrayList<TournamentContestant> contestants;
    private final TournamentDifficulty tournamentDifficulty;
    private final double initialBet;

    public TournamentMaster(String difficulty, double initialBet) {
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