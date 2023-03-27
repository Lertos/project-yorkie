package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.ActivityPage;
import com.lertos.projectyorkie.HomePage;
import com.lertos.projectyorkie.PettingPage;
import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.TournamentPage;
import com.lertos.projectyorkie.data.file.FileTutorialsKeys;
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

        Tutorial homeScreenTutorial = new Tutorial(HomePage.class.getName(), R.layout.tutorial_home_page, R.id.vsHomePage, false, FileTutorialsKeys.DATA_SHOW_TUT_HOME);
        Tutorial activityScreenTutorial = new Tutorial(ActivityPage.class.getName(), R.layout.tutorial_activity_page, R.id.vsActivityPage, false, FileTutorialsKeys.DATA_SHOW_TUT_ACTIVITY);
        Tutorial pettingScreenTutorial = new Tutorial(PettingPage.class.getName(), R.layout.tutorial_petting_page, R.id.vsPettingPage, false, FileTutorialsKeys.DATA_SHOW_TUT_PETTING);
        Tutorial tournamentScreenTutorial = new Tutorial(TournamentPage.class.getName(), R.layout.tutorial_tournament_page, R.id.vsTournamentPage, false, FileTutorialsKeys.DATA_SHOW_TUT_TOURNAMENT);

        tutorials.add(homeScreenTutorial);
        tutorials.add(activityScreenTutorial);
        tutorials.add(pettingScreenTutorial);
        tutorials.add(tournamentScreenTutorial);

        Tutorial catchDogTreatsTutorial = new Tutorial(CatchDogTreats.class.getName(), R.layout.game_tutorial_catch_dog_treats, R.id.vsCatchDogTreatsTutorial, false, FileTutorialsKeys.DATA_SHOW_TUT_CATCH_DOG_TREATS);
        Tutorial dodgeTheCatsTutorial = new Tutorial(DodgeTheCats.class.getName(), R.layout.game_tutorial_dodge_the_cats, R.id.vsDodgeTheCatsTutorial, false, FileTutorialsKeys.DATA_SHOW_TUT_DODGE_THE_CATS);
        Tutorial treatTossTutorial = new Tutorial(TreatToss.class.getName(), R.layout.game_tutorial_treat_toss, R.id.vsTreatTossTutorial, false, FileTutorialsKeys.DATA_SHOW_TUT_TREAT_TOSS);
        Tutorial whackTheCatTutorial = new Tutorial(WhackTheCat.class.getName(), R.layout.game_tutorial_whack_the_cat, R.id.vsWhackTheCatTutorial, false, FileTutorialsKeys.DATA_SHOW_TUT_WHACK_THE_CAT);

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
