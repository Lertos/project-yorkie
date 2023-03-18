package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.ActivityPage;
import com.lertos.projectyorkie.HomePage;
import com.lertos.projectyorkie.PettingPage;
import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.TournamentPage;

import java.util.ArrayList;
import java.util.List;

public class TutorialManager {

    private List<Tutorial> tutorials;

    public TutorialManager() {
        tutorials = new ArrayList<>();

        Tutorial homeTutorial = new Tutorial(HomePage.class.getName(), R.layout.page_home, false);
        Tutorial activityTutorial = new Tutorial(ActivityPage.class.getName(), R.layout.page_activities, false);
        Tutorial pettingTutorial = new Tutorial(PettingPage.class.getName(), R.layout.page_petting, false);
        Tutorial tournamentTutorial = new Tutorial(TournamentPage.class.getName(), R.layout.page_tournament, false);

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
}
