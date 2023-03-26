package com.lertos.projectyorkie.data;

import android.content.Context;

import com.lertos.projectyorkie.ActivityPage;
import com.lertos.projectyorkie.HomePage;
import com.lertos.projectyorkie.PettingPage;
import com.lertos.projectyorkie.TournamentPage;
import com.lertos.projectyorkie.data.file.FileManager;
import com.lertos.projectyorkie.data.file.FilePlayerKeys;
import com.lertos.projectyorkie.data.file.FileSettingsKeys;
import com.lertos.projectyorkie.model.Activity;
import com.lertos.projectyorkie.model.PackDog;
import com.lertos.projectyorkie.model.Talent;
import com.lertos.projectyorkie.tournament.TournamentDivision;
import com.lertos.projectyorkie.tournament.TournamentRank;
import com.lertos.projectyorkie.tournament.games.CatchDogTreats;
import com.lertos.projectyorkie.tournament.games.DodgeTheCats;
import com.lertos.projectyorkie.tournament.games.TreatToss;
import com.lertos.projectyorkie.tournament.games.WhackTheCat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DataManager {

    private boolean hasPlayedBefore;
    private static DataManager instance;
    private FileManager fileManager;
    private TutorialManager tutorialManager;
    private SettingsManager settingsManager;
    private Player playerData;
    private List<Talent> talentList = new ArrayList<>();
    private List<PackDog> packDogList = new ArrayList<>();
    private List<Activity> activityList = new ArrayList<>();

    private DataManager() {
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void start(Context context) {
        //Setup the FileManager so we can load all settings and initial starting data
        fileManager = new FileManager(context);

        hasPlayedBefore = fileManager.getDataFile().getBoolean(FilePlayerKeys.DATA_HAS_PLAYED_BEFORE);

        tutorialManager = new TutorialManager();
        setTutorialValues();

        settingsManager = new SettingsManager();
        setSettingValues();

        //Now that we have the settings loaded, set settings where need be
        MediaManager.getInstance().setVolumesFromUserPrefs();

        //TODO: Load (and save) the player data in a file and populate the Player object on startup
        playerData = new Player(100000, 100);

        playerData.setPettingHighestThreshold(0);
        playerData.setPettingHighestSquare(0);

        TournamentRank rank = new TournamentRank();
        //TODO: Get from FileManager
        rank.setDivision(getDivisionFromString("Wood"));
        rank.setTier(5);

        playerData.setTournamentRank(rank);

        PackDogs packDogs = new PackDogs();
        packDogList = packDogs.getListPackDogs();
        //TODO: Set unlocked dogs - feed in String with pipes

        Talents talents = new Talents();
        talentList = talents.getListTalents();
        //TODO: Set talent levels - feed in String with pipes

        Activities activities = new Activities();
        activityList = activities.getListActivities();
        //TODO: Set activity levels - feed in String with pipes

        setHeartsPerSecond();
        setHeartTokensPerSecond();
    }

    private void setTutorialValues() {
        boolean showHomePageTut = fileManager.getDataFile().getBoolean(FilePlayerKeys.DATA_SHOW_TUT_HOME);
        boolean showActivityPageTut = fileManager.getDataFile().getBoolean(FilePlayerKeys.DATA_SHOW_TUT_ACTIVITY);
        boolean showPettingPageTut = fileManager.getDataFile().getBoolean(FilePlayerKeys.DATA_SHOW_TUT_PETTING);
        boolean showTournamentPageTut = fileManager.getDataFile().getBoolean(FilePlayerKeys.DATA_SHOW_TUT_TOURNAMENT);

        boolean showCatchDogTreatsGameTut = fileManager.getDataFile().getBoolean(FilePlayerKeys.DATA_SHOW_TUT_CATCH_DOG_TREATS);
        boolean showDodgeTheCatsGameTut = fileManager.getDataFile().getBoolean(FilePlayerKeys.DATA_SHOW_TUT_DODGE_THE_CATS);
        boolean showTreatTossGameTut = fileManager.getDataFile().getBoolean(FilePlayerKeys.DATA_SHOW_TUT_TREAT_TOSS);
        boolean showWhackTheCatGameTut = fileManager.getDataFile().getBoolean(FilePlayerKeys.DATA_SHOW_TUT_WHACK_THE_CAT);

        if (!showHomePageTut)
            tutorialManager.setTutorialAsSeen(HomePage.class.getName());
        if (!showActivityPageTut)
            tutorialManager.setTutorialAsSeen(ActivityPage.class.getName());
        if (!showPettingPageTut)
            tutorialManager.setTutorialAsSeen(PettingPage.class.getName());
        if (!showTournamentPageTut)
            tutorialManager.setTutorialAsSeen(TournamentPage.class.getName());
        if (!showCatchDogTreatsGameTut)
            tutorialManager.setTutorialAsSeen(CatchDogTreats.class.getName());
        if (!showDodgeTheCatsGameTut)
            tutorialManager.setTutorialAsSeen(DodgeTheCats.class.getName());
        if (!showTreatTossGameTut)
            tutorialManager.setTutorialAsSeen(TreatToss.class.getName());
        if (!showWhackTheCatGameTut)
            tutorialManager.setTutorialAsSeen(WhackTheCat.class.getName());
    }

    private void setSettingValues() {
        float effectVolume = fileManager.getSettingsFile().getFloat(FileSettingsKeys.SETTING_EFFECT_VOLUME);
        float musicVolume = fileManager.getSettingsFile().getFloat(FileSettingsKeys.SETTING_MUSIC_VOLUME);
        boolean showAnimationsInTournament = fileManager.getSettingsFile().getBoolean(FileSettingsKeys.SETTING_SHOW_ANIMATIONS_IN_TOURNAMENT);

        settingsManager.setTrackEffectVolume(effectVolume);
        settingsManager.setTrackSongVolume(musicVolume);
        settingsManager.setShowAppearAnimationsInTournament(showAnimationsInTournament);
    }

    private void setPlayerValues() {

    }

    public boolean hasPlayedBefore() {
        return hasPlayedBefore;
    }

    public void setHasPlayedBefore(boolean hasPlayedBefore) {
        this.hasPlayedBefore = hasPlayedBefore;
    }

    public TutorialManager getTutorials() {
        return tutorialManager;
    }

    public SettingsManager getSettings() {
        return settingsManager;
    }

    public Player getPlayerData() {
        return playerData;
    }

    public double getTotalPackMultiplier() {
        double bonus = 0;
        double multiplier = Talents.strengthInNumbers.getCurrentBonus();

        for (PackDog packDog : packDogList) {
            if (!packDog.isUnlocked())
                break;
            bonus += packDog.getAddedBonus();
        }

        if (multiplier != 0)
            bonus *= multiplier;

        return bonus;
    }

    public PackDog getRandomPackDog() {
        Random rng = new Random();
        int randomIndex = rng.nextInt(packDogList.size());

        return packDogList.get(randomIndex);
    }

    public void setHeartsPerSecond() {
        double heartsPerSecond = 0;

        for (Activity activity : activityList) {
            if (!activity.isUnlocked())
                break;
            heartsPerSecond += activity.getCurrentIncome();
        }
        playerData.setCurrentHeartsPerSecond(heartsPerSecond);
    }

    public void setHeartTokensPerSecond() {
        double heartTokensPerSecond = 0;

        for (Activity activity : activityList) {
            if (!activity.isUnlocked())
                break;
            heartTokensPerSecond += activity.getBaseHeartTokensPerSecond();
        }

        double multiplier = Talents.luckyStreak.getCurrentBonus();

        if (multiplier != 0)
            heartTokensPerSecond *= multiplier;

        playerData.setCurrentHeartTokensPerSecond(heartTokensPerSecond);
    }

    public void addHearts(double hearts) {
        if (hearts < 0 && hearts > playerData.getCurrentHearts())
            return;
        playerData.setCurrentHearts(playerData.getCurrentHearts() + hearts);
    }

    public void addHeartTokens(double heartTokens) {
        if (heartTokens < 0 && heartTokens > playerData.getCurrentHeartTokens())
            return;
        playerData.setCurrentHeartTokens(playerData.getCurrentHeartTokens() + heartTokens);
    }


    public void calculateHeartsPerSecond() {
        DataManager.getInstance().setHeartsPerSecond();
        addHearts(playerData.getCurrentHeartsPerSecond());
    }

    public void calculateHeartTokensPerSecond() {
        DataManager.getInstance().setHeartTokensPerSecond();
        addHeartTokens(playerData.getCurrentHeartTokensPerSecond());
    }

    private TournamentDivision getDivisionFromString(String str) {
        for (TournamentDivision division : TournamentDivision.BRONZE.getDeclaringClass().getEnumConstants()) {
            if (str.equalsIgnoreCase(division.getDisplayStr()))
                return division;
        }
        return null;
    }

    public void resortPackDogs() {
        packDogList.sort(new PackDogs.SortByName());
    }

    public List<PackDog> getPackDogs() {
        return Collections.unmodifiableList(packDogList);
    }

    public List<Talent> getTalents() {
        return Collections.unmodifiableList(talentList);
    }

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(activityList);
    }
}
