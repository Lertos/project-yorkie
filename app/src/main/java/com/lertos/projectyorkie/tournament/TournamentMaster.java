package com.lertos.projectyorkie.tournament;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.model.PackDog;

import java.util.ArrayList;

public class TournamentMaster {

    private enum TournamentState {
        LOBBY,
        IN_GAME,
        POST_GAME,
        POST_TOURNAMENT
    }
    private final int maxAIContestants = 3;
    private TournamentState currentState;
    private ArrayList<TournamentContestant> contestants;
    private TournamentDifficulty tournamentDifficulty;

    public TournamentMaster(String difficulty) {
        this.currentState = TournamentState.LOBBY;
        this.contestants = createContestants();
        this.tournamentDifficulty = getDifficultyFromString(difficulty);
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

}