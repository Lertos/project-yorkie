package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.ActivityPage;
import com.lertos.projectyorkie.HomePage;
import com.lertos.projectyorkie.PettingPage;
import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.TournamentPage;
import com.lertos.projectyorkie.tournament.games.CatchDogTreats;
import com.lertos.projectyorkie.tournament.games.DodgeTheCats;
import com.lertos.projectyorkie.tournament.games.TreatToss;
import com.lertos.projectyorkie.tournament.games.WhackTheCat;

import java.util.ArrayList;
import java.util.List;

public class TutorialManager {

    private final List<Tutorial> tutorials;

    public TutorialManager() {
        tutorials = new ArrayList<>();

        Tutorial homeScreenTutorial = new Tutorial(HomePage.class.getName(), R.layout.tutorial_home_page, R.id.vsHomePage, false);
        Tutorial activityScreenTutorial = new Tutorial(ActivityPage.class.getName(), R.layout.tutorial_activity_page, R.id.vsActivityPage, false);
        Tutorial pettingScreenTutorial = new Tutorial(PettingPage.class.getName(), R.layout.tutorial_petting_page, R.id.vsPettingPage, false);
        Tutorial tournamentScreenTutorial = new Tutorial(TournamentPage.class.getName(), R.layout.tutorial_tournament_page, R.id.vsTournamentPage, false);

        tutorials.add(homeScreenTutorial);
        tutorials.add(activityScreenTutorial);
        tutorials.add(pettingScreenTutorial);
        tutorials.add(tournamentScreenTutorial);

        //TODO: Once you have the layout's and view stubs created, change these to those
        Tutorial catchDogTreatsTutorial = new Tutorial(CatchDogTreats.class.getName(), R.layout.tutorial_home_page, R.id.vsHomePage, false);
        Tutorial dodgeTheCatsTutorial = new Tutorial(DodgeTheCats.class.getName(), R.layout.tutorial_home_page, R.id.vsHomePage, false);
        Tutorial treatTossTutorial = new Tutorial(TreatToss.class.getName(), R.layout.tutorial_home_page, R.id.vsHomePage, false);
        Tutorial whackTheCatTutorial = new Tutorial(WhackTheCat.class.getName(), R.layout.tutorial_home_page, R.id.vsHomePage, false);

        tutorials.add(catchDogTreatsTutorial);
        tutorials.add(dodgeTheCatsTutorial);
        tutorials.add(treatTossTutorial);
        tutorials.add(whackTheCatTutorial);
    }

    public boolean tutorialClassExists(String className) {
        for (Tutorial tutorial : tutorials) {
            if (tutorial.getMatchingClassName().equalsIgnoreCase(className))
                return true;
        }
        return false;
    }

    public Tutorial getTutorial(String className) {
        for (Tutorial tutorial : tutorials) {
            if (tutorial.getMatchingClassName().equalsIgnoreCase(className))
                return tutorial;
        }
        return null;
    }

    public void setTutorialAsSeen(String className) {
        for (Tutorial tutorial : tutorials) {
            if (tutorial.getMatchingClassName().equalsIgnoreCase(className))
                tutorial.setHasPlayerSeen(true);
        }
    }

    public void resetAllTutorials() {
        for (Tutorial tutorial : tutorials)
            tutorial.setHasPlayerSeen(false);
    }

}
