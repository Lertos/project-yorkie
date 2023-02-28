package com.lertos.projectyorkie.tournament;

import java.util.ArrayList;

public class TournamentMaster {

    private enum TournamentState {
        LOBBY,
        IN_GAME,
        POST_GAME,
        POST_TOURNAMENT
    }
    private TournamentState currentState;
    private ArrayList<TournamentContestant> contestants;

    private void createContestants() {
        //Fill the contestant list
        //Add the player first, then fill the rest with unique PackDogs - checking that it doesn't already exist in the list
    }

}