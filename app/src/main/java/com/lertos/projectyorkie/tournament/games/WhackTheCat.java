package com.lertos.projectyorkie.tournament.games;

import android.view.View;
import android.widget.TextView;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.tournament.TournamentGame;

public class WhackTheCat extends TournamentGame {

    public WhackTheCat(View view) {
        super(view);

        ((TextView) view.findViewById(R.id.tvHeader)).setText("Changed from class");

        setupOnClickListeners();
    }

    public void setupOnClickListeners() {

    }

    protected void gameLoop() {
        //Run handler and constantly run it until game is over, then return
    }


}
