package com.lertos.projectyorkie.tournament.games;

import static android.view.View.GONE;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.tournament.TournamentGame;

import java.util.ArrayList;

public class WhackTheCat extends TournamentGame {

    private ArrayList<View> squares;

    public WhackTheCat(View view) {
        super(view);

        squares = new ArrayList<>();

        ((TextView) view.findViewById(R.id.tvHeader)).setText("Changed from class");

        addImagesToView();
        setupOnClickListeners();
    }

    public void addImagesToView() {
        ViewGroup viewGroup = ((ViewGroup) view);

        for (int i=0; i<9; i++) {
            View newView = LayoutInflater.from(view.getContext()).inflate(R.layout.subpage_whack_cat_item, null);
            newView.setX(80 * (i+1));
            newView.setY(80 * (i+1));

            viewGroup.addView(newView);

            squares.add(newView);
        }
    }

    public void setupOnClickListeners() {
        for (View image : squares) {
            image.setOnClickListener(v -> {
                v.setVisibility(GONE);
            });
        }
    }

    protected void gameLoop() {
        //Run handler and constantly run it until game is over, then return
    }


}
