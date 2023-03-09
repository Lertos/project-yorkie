package com.lertos.projectyorkie.tournament;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.TournamentLobbyPage;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.model.PackDog;
import com.lertos.projectyorkie.tournament.games.CatchDogTreats;
import com.lertos.projectyorkie.tournament.games.WhackTheCat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class TournamentMaster {

    private final TournamentLobbyPage lobbyPage;
    private View inflatedStub;
    private final int maxAIContestants = 3;
    private final double maxScoreModifier = 0.3;
    private ArrayList<TournamentGame> listOfGames;
    private TournamentGame currentGame;
    private final ArrayList<TournamentContestant> contestants;
    private final TournamentDifficulty tournamentDifficulty;
    private final double initialBet;
    private Random rng;

    public TournamentMaster(TournamentLobbyPage lobbyPage, String difficulty, double initialBet) {
        this.lobbyPage = lobbyPage;
        this.contestants = createContestants();
        this.tournamentDifficulty = getDifficultyFromString(difficulty);
        this.initialBet = initialBet;

        this.rng = new Random();

        this.listOfGames = createListOfGames();

        //Pick the first game randomly to show the title in the lobby
        pickRandomGame();
    }

    private ArrayList<TournamentGame> createListOfGames() {
        ArrayList<TournamentGame> list = new ArrayList<>();

        //Create each game object
        WhackTheCat gameWhackTheCat = new WhackTheCat(this, tournamentDifficulty, lobbyPage, "Whack the Cat");
        CatchDogTreats gameCatchDogTreats = new CatchDogTreats(this, tournamentDifficulty, lobbyPage, "Catch the Dog Treats");

        //Add each game object to our games list
        list.add(gameWhackTheCat);
        list.add(gameCatchDogTreats);

        return list;
    }

    private void pickRandomGame() {
        int randIndex = rng.nextInt(listOfGames.size());
        TournamentGame nextGame = listOfGames.get(randIndex);

        currentGame = nextGame;

        listOfGames.remove(randIndex);
    }

    public String getCurrentGameName() {
        String title = "";

        if (currentGame != null)
            title = currentGame.getGameTitle();

        return title;
    }

    private void inflateGameStub() {
        ViewStub stub = null;

        if (currentGame instanceof WhackTheCat) {
            stub = lobbyPage.findViewById(R.id.vsWhackTheCat);
            stub.setLayoutResource(R.layout.game_page_whack_the_cat);
        } else if (currentGame instanceof CatchDogTreats) {
            stub = lobbyPage.findViewById(R.id.vsCatchDogTreats);
            stub.setLayoutResource(R.layout.game_page_catch_dog_treats);
        }

        inflatedStub = stub.inflate();
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

    public void startNextGame() {
        inflateGameStub();

        currentGame.startGame();
    }

    //TODO: Add method to switch between states and handle the visibility of screens there instead
    public void showEndGameScreen() {
        //Remove the embedded game screen
        ((ViewGroup) inflatedStub.getParent()).removeView(inflatedStub);

        //Set the title of the end game screen of the game title
        ((TextView) lobbyPage.findViewById(R.id.tvGameTitleHeader)).setText(currentGame.getGameTitle());

        //Set the score textview to the final score
        ((TextView) lobbyPage.findViewById(R.id.tvFinalScore)).setText(String.format("%.2f", currentGame.score));

        //Show the end game screen with the score
        lobbyPage.findViewById(R.id.relGameOverScreen).setVisibility(View.VISIBLE);

        //Hide the shared header that holds the timer and score
        lobbyPage.findViewById(R.id.linGameHeader).setVisibility(View.GONE);

        //Add scores to all contestants
        updateContestantScores();

        //Pick the next game so we can display the title
        pickRandomGame();
    }

    private void updateContestantScores() {
        int averageScore = currentGame.getAverageScore();
        int scoreBound = (int) Math.round(averageScore * maxScoreModifier);
        int lowScoreBound = averageScore - scoreBound;
        int highScoreBound = averageScore + scoreBound;

        for (TournamentContestant contestant : contestants) {
            if (contestant.isPlayer())
                contestant.addToCurrentScore(currentGame.score);
            else {
                int scoreToAdd = rng.nextInt(highScoreBound - lowScoreBound) + lowScoreBound;
                contestant.addToCurrentScore(scoreToAdd);
            }
        }
    }

    public class SortByScore implements Comparator<TournamentContestant> {

        @Override
        public int compare(TournamentContestant c1, TournamentContestant c2) {
            double totalScore1 = c1.getTotalScore();
            double totalScore2 = c2.getTotalScore();

            int totalScoreCompare = Double.compare(totalScore1, totalScore2);

            if (totalScoreCompare != 0)
                return totalScoreCompare * -1;

            double currentScore1 = c1.getCurrentScore();
            double currentScore2 = c2.getCurrentScore();

            return Double.compare(currentScore1, currentScore2) * -1;
        }

    }

}