package com.lertos.projectyorkie.tournament;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.TournamentLobbyPage;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.MediaManager;
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
    private int playerFinalPosition;
    private double playerFinalReward;

    public TournamentMaster(TournamentLobbyPage lobbyPage, String difficulty, double initialBet) {
        this.lobbyPage = lobbyPage;
        this.contestants = createContestants();
        this.tournamentDifficulty = getDifficultyFromString(difficulty);
        this.initialBet = initialBet;

        this.rng = new Random();

        this.listOfGames = createListOfGames();

        //Pay the initial bet
        DataManager.getInstance().addHearts(-initialBet);

        //Pick the first game randomly to show the title in the lobby
        pickRandomGame();
    }

    private ArrayList<TournamentGame> createListOfGames() {
        ArrayList<TournamentGame> list = new ArrayList<>();

        //Create each game object
        WhackTheCat gameWhackTheCat = new WhackTheCat(this, tournamentDifficulty, lobbyPage, "Whack the Cat", "Whack Cats. Get Points.");
        CatchDogTreats gameCatchDogTreats = new CatchDogTreats(this, tournamentDifficulty, lobbyPage, "Catch the Dog Treats", "Click Squares When Overlapping");

        //Add each game object to our games list
        list.add(gameWhackTheCat);
        list.add(gameCatchDogTreats);

        return list;
    }

    private void pickRandomGame() {
        if (currentGame != null)
            listOfGames.remove(currentGame);

        if (listOfGames.isEmpty())
            return;

        int randIndex = rng.nextInt(listOfGames.size());
        TournamentGame nextGame = listOfGames.get(randIndex);

        currentGame = nextGame;
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

        PackDog player = new PackDog("Yorkie [YOU]", R.mipmap.portrait_loki);
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

    public void processEndOfTournament() {
        playerFinalPosition = getPlayerFinalIndex();

        processPlayerPosition();
    }

    private void processPlayerPosition() {
        double rewardMultiplier = 0.0;

        if (playerFinalPosition == 0) {
            rewardMultiplier = 1.75;
        } else if (playerFinalPosition == 1) {
            rewardMultiplier = 1.25;
        } else if (playerFinalPosition == 2) {
            rewardMultiplier = 1.0;
        }

        playerFinalReward = initialBet * rewardMultiplier;

        DataManager.getInstance().addHearts(playerFinalReward);
    }

    public String getPlayerPosition() {
        int playerPosition = getPlayerFinalIndex();
        String position;

        switch (playerPosition) {
            case 0:
                position = "1st";
                break;
            case 1:
                position = "2nd";
                break;
            case 2:
                position = "3rd";
                break;
            default:
                position = (playerPosition + 1) + "th";
                break;
        }
        return position;
    }

    public double getPlayerFinalReward() {
        return playerFinalReward;
    }

    private int getPlayerFinalIndex() {
        List<TournamentContestant> finalContestants = getContestants();

        for (int i = 0; i < finalContestants.size(); i++) {
            if (finalContestants.get(i).isPlayer())
                return i;
        }
        return -1;
    }

    public TournamentDifficulty getTournamentDifficulty() {
        return tournamentDifficulty;
    }

    public double getInitialBet() {
        return initialBet;
    }

    public boolean isGameListEmpty() {
        return listOfGames.isEmpty();
    }

    public void startNextGame() {
        inflateGameStub();
        currentGame.startGame();
    }

    public void showEndGameScreen() {
        MediaManager.getInstance().playEffectTrack(R.raw.effect_end_screen);

        //Remove the embedded game screen
        ((ViewGroup) inflatedStub.getParent()).removeView(inflatedStub);

        //Set the title of the end game screen of the game title
        ((TextView) lobbyPage.findViewById(R.id.tvGameTitleHeader)).setText(currentGame.getGameTitle());

        //Show the end game screen with the score
        lobbyPage.findViewById(R.id.relGameOverScreen).setVisibility(View.VISIBLE);

        //Hide the shared header that holds the timer and score
        lobbyPage.findViewById(R.id.linGameHeader).setVisibility(View.GONE);

        showEndScore();

        //Add scores to all contestants
        updateContestantScores();

        //Pick the next game so we can display the title
        pickRandomGame();
    }

    private void showEndScore() {
        TextView tvFinalScore = lobbyPage.findViewById(R.id.tvFinalScore);

        tvFinalScore.setText(String.valueOf(Math.round(currentGame.score)));

        tvFinalScore.setAlpha(0);

        tvFinalScore.animate().alpha(1).scaleX(3).scaleY(3).setDuration(1200).withEndAction(() -> {
            tvFinalScore.animate().scaleX(1).scaleY(1).setDuration(400);
        });
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