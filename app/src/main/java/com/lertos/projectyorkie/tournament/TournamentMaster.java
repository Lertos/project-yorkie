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
import com.lertos.projectyorkie.tournament.games.DodgeTheCats;
import com.lertos.projectyorkie.tournament.games.TreatToss;
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
    private String previousRank;
    private String newRank;
    //-1 means a decrease in rank, 0 means no change, 1 means increase in rank
    private int rankDirection;

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
        DodgeTheCats gameDodgeTheCats = new DodgeTheCats(this, tournamentDifficulty, lobbyPage, "Dodge the Cats", "Swipe to Move. Dodge the Cats");
        TreatToss gameTreatToss = new TreatToss(this, tournamentDifficulty, lobbyPage, "Treat Toss", "Tap to Throw Treat at Dog");

        //Add each game object to our games list
        list.add(gameWhackTheCat);
        list.add(gameCatchDogTreats);
        list.add(gameDodgeTheCats);
        list.add(gameTreatToss);

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
        } else if (currentGame instanceof DodgeTheCats) {
            stub = lobbyPage.findViewById(R.id.vsDodgeTheCats);
            stub.setLayoutResource(R.layout.game_page_dodge_the_cats);
        } else if (currentGame instanceof TreatToss) {
            stub = lobbyPage.findViewById(R.id.vsTreatToss);
            stub.setLayoutResource(R.layout.game_page_treat_toss);
        }

        inflatedStub = stub.inflate();
    }

    private void inflateTutorialStub() {
        ViewStub stub = null;

        if (currentGame instanceof WhackTheCat) {
            stub = lobbyPage.findViewById(R.id.vsWhackTheCatTutorial);
            stub.setLayoutResource(R.layout.game_tutorial_whack_the_cat);
        } else if (currentGame instanceof CatchDogTreats) {
            stub = lobbyPage.findViewById(R.id.vsCatchDogTreatsTutorial);
            stub.setLayoutResource(R.layout.game_tutorial_catch_dog_treats);
        } else if (currentGame instanceof DodgeTheCats) {
            stub = lobbyPage.findViewById(R.id.vsDodgeTheCatsTutorial);
            stub.setLayoutResource(R.layout.game_tutorial_dodge_the_cats);
        } else if (currentGame instanceof TreatToss) {
            stub = lobbyPage.findViewById(R.id.vsTreatTossTutorial);
            stub.setLayoutResource(R.layout.game_tutorial_treat_toss);
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

        PackDog player = new PackDog("You", R.mipmap.portrait_loki);
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
        return contestants;
    }

    public void processEndOfTournament() {
        playerFinalPosition = getPlayerFinalIndex();

        processFinalReward();
        processNewRank();
    }

    private void processFinalReward() {
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

    private void processNewRank() {
        previousRank = DataManager.getInstance().getPlayerData().getTournamentRank().getRankDisplay();

        if (playerFinalPosition == 0) {
            rankDirection = 1;
            DataManager.getInstance().getPlayerData().getTournamentRank().increaseTier();
        } else if (playerFinalPosition == 1) {
            rankDirection = 0;
        } else if (playerFinalPosition == 2) {
            rankDirection = 0;
        } else {
            rankDirection = -1;
            DataManager.getInstance().getPlayerData().getTournamentRank().decreaseTier();
        }

        newRank = DataManager.getInstance().getPlayerData().getTournamentRank().getRankDisplay();
    }

    public String getPreviousRank() {
        return previousRank;
    }

    public String getNewRank() {
        return newRank;
    }

    public int getRankDirection() {
        return rankDirection;
    }

    public PackDog getRandomDog() {
        int playerPosition = getPlayerFinalIndex();

        if (playerPosition != 0)
            return null;

        List<TournamentContestant> finalContestants = getContestants();
        int randIndex = rng.nextInt(finalContestants.size()) + 1;

        return finalContestants.get(randIndex).getPackDog();
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

    public double getInitialBet() {
        return initialBet;
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

    public boolean isGameListEmpty() {
        return listOfGames.isEmpty();
    }

    public void startNextGame() {
        //Make sure to reset the current scores of each contestant
        for (TournamentContestant contestant : contestants)
            contestant.resetCurrentScore();

        inflateGameStub();
        currentGame.startGame();
    }

    public void removeTutorialStub() {
        ((ViewGroup) inflatedStub.getParent()).removeView(inflatedStub);
    }

    public void loadGameTutorial() {
        lobbyPage.findViewById(R.id.linGameTutorialHeader).setVisibility(View.VISIBLE);

        inflateTutorialStub();
    }

    public TournamentGame getCurrentGame() {
        return currentGame;
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
                //Math.max because bounds must be positive; so if they get 0 it will crash
                int scoreToAdd = rng.nextInt(Math.max(1, highScoreBound - lowScoreBound)) + lowScoreBound;
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