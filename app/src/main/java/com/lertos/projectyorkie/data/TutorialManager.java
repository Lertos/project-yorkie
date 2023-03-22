package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.ActivityPage;
import com.lertos.projectyorkie.HomePage;
import com.lertos.projectyorkie.PettingPage;
import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.TournamentPage;

import java.util.ArrayList;
import java.util.List;

public class TutorialManager {

    private final List<Tutorial> tutorials;

    public TutorialManager() {
        tutorials = new ArrayList<>();

        Tutorial homeTutorial = new Tutorial(HomePage.class.getName(), R.layout.tutorial_home_page, R.id.vsHomePage, true);
        Tutorial activityTutorial = new Tutorial(ActivityPage.class.getName(), R.layout.tutorial_activity_page, R.id.vsActivityPage, true);
        Tutorial pettingTutorial = new Tutorial(PettingPage.class.getName(), R.layout.tutorial_petting_page, R.id.vsPettingPage, true);
        Tutorial tournamentTutorial = new Tutorial(TournamentPage.class.getName(), R.layout.tutorial_tournament_page, R.id.vsTournamentPage, true);

        tutorials.add(homeTutorial);
        tutorials.add(activityTutorial);
        tutorials.add(pettingTutorial);
        tutorials.add(tournamentTutorial);
    }

    public boolean tutorialClassExists(String className) {
        for (Tutorial tutorial : tutorials) {
            if (tutorial.getClassName().equalsIgnoreCase(className))
                return true;
        }
        return false;
    }

    public Tutorial getTutorial(String className) {
        for (Tutorial tutorial : tutorials) {
            if (tutorial.getClassName().equalsIgnoreCase(className))
                return tutorial;
        }
        return null;
    }

    public void setTutorialAsSeen(String className) {
        for (Tutorial tutorial : tutorials) {
            if (tutorial.getClassName().equalsIgnoreCase(className))
                tutorial.setHasPlayerSeen(true);
        }
    }

    public void resetAllTutorials() {
        for (Tutorial tutorial : tutorials)
            tutorial.setHasPlayerSeen(false);
    }
}
