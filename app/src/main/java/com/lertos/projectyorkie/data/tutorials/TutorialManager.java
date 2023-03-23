package com.lertos.projectyorkie.data.tutorials;

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

        ScreenTutorial homeScreenTutorial = new ScreenTutorial(HomePage.class.getName(), R.layout.tutorial_home_page, R.id.vsHomePage, false);
        ScreenTutorial activityScreenTutorial = new ScreenTutorial(ActivityPage.class.getName(), R.layout.tutorial_activity_page, R.id.vsActivityPage, false);
        ScreenTutorial pettingScreenTutorial = new ScreenTutorial(PettingPage.class.getName(), R.layout.tutorial_petting_page, R.id.vsPettingPage, false);
        ScreenTutorial tournamentScreenTutorial = new ScreenTutorial(TournamentPage.class.getName(), R.layout.tutorial_tournament_page, R.id.vsTournamentPage, false);

        tutorials.add(homeScreenTutorial);
        tutorials.add(activityScreenTutorial);
        tutorials.add(pettingScreenTutorial);
        tutorials.add(tournamentScreenTutorial);

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
