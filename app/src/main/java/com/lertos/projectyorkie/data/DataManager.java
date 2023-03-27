package com.lertos.projectyorkie.data;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.lertos.projectyorkie.ActivityPage;
import com.lertos.projectyorkie.HomePage;
import com.lertos.projectyorkie.PettingPage;
import com.lertos.projectyorkie.TournamentPage;
import com.lertos.projectyorkie.data.file.FileManager;
import com.lertos.projectyorkie.data.file.FilePlayerKeys;
import com.lertos.projectyorkie.data.file.FileSettingsKeys;
import com.lertos.projectyorkie.data.file.FileTutorialsKeys;
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
    //TODO: Change to something more reasonable (30s / 60s)
    private final long millisecondsPerSave = 10000;
    private FileManager fileManager;
    private TutorialManager tutorialManager;
    private SettingsManager settingsManager;
    private Player playerData;
    private Talents talents;
    private PackDogs packDogs;
    private Activities activities;
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

        setPlayerValues();

        //TODO: Add hearts for the time gone
        String strLastTimeSaved = fileManager.getDataFile().getString(FilePlayerKeys.DATA_LAST_TIME_ON);

        if (!strLastTimeSaved.isEmpty()) {
            long currentTime = System.currentTimeMillis();
            long lastOnTime = Long.parseLong(strLastTimeSaved);

            Log.d("><><><><", "Time Difference (in seconds): " + (currentTime - lastOnTime) / 1000);
        }

        String separator = DataManager.getInstance().getFiles().getDataFile().getValueSeparator();

        talents = new Talents(separator);
        packDogs = new PackDogs(separator);
        activities = new Activities(separator);

        packDogList = packDogs.getListPackDogs();
        //TODO: Set unlocked dogs - feed in String with pipes

        talents.setInitialLevels(DataManager.getInstance().getFiles().getDataFile().getString(FilePlayerKeys.DATA_TALENT_LEVELS));
        talentList = talents.getListTalents();

        activityList = activities.getListActivities();
        //TODO: Set activity levels - feed in String with pipes

        setHeartsPerSecond();
        setHeartTokensPerSecond();
    }

    private void setTutorialValues() {
        boolean showHomePageTut = fileManager.getTutorialFile().getBoolean(FileTutorialsKeys.DATA_SHOW_TUT_HOME);
        boolean showActivityPageTut = fileManager.getTutorialFile().getBoolean(FileTutorialsKeys.DATA_SHOW_TUT_ACTIVITY);
        boolean showPettingPageTut = fileManager.getTutorialFile().getBoolean(FileTutorialsKeys.DATA_SHOW_TUT_PETTING);
        boolean showTournamentPageTut = fileManager.getTutorialFile().getBoolean(FileTutorialsKeys.DATA_SHOW_TUT_TOURNAMENT);

        boolean showCatchDogTreatsGameTut = fileManager.getTutorialFile().getBoolean(FileTutorialsKeys.DATA_SHOW_TUT_CATCH_DOG_TREATS);
        boolean showDodgeTheCatsGameTut = fileManager.getTutorialFile().getBoolean(FileTutorialsKeys.DATA_SHOW_TUT_DODGE_THE_CATS);
        boolean showTreatTossGameTut = fileManager.getTutorialFile().getBoolean(FileTutorialsKeys.DATA_SHOW_TUT_TREAT_TOSS);
        boolean showWhackTheCatGameTut = fileManager.getTutorialFile().getBoolean(FileTutorialsKeys.DATA_SHOW_TUT_WHACK_THE_CAT);

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
        double currentHearts = fileManager.getDataFile().getDouble(FilePlayerKeys.DATA_CURRENT_HEARTS);
        double currentHeartTokens = fileManager.getDataFile().getDouble(FilePlayerKeys.DATA_CURRENT_HEART_TOKENS);

        playerData = new Player(currentHearts, currentHeartTokens);

        int highestThreshold = fileManager.getDataFile().getInt(FilePlayerKeys.DATA_HIGH_SCORE_THRESHOLD);
        int highestSquare = fileManager.getDataFile().getInt(FilePlayerKeys.DATA_HIGH_SCORE_SQUARE);

        playerData.setPettingHighestThreshold(highestThreshold);
        playerData.setPettingHighestSquare(highestSquare);

        String currentDivision = fileManager.getDataFile().getString(FilePlayerKeys.DATA_CURRENT_RANK_NAME);
        int currentTier = fileManager.getDataFile().getInt(FilePlayerKeys.DATA_CURRENT_RANK_TIER);

        TournamentRank rank = new TournamentRank();

        rank.setDivision(getDivisionFromString(currentDivision));
        rank.setTier(currentTier);

        playerData.setTournamentRank(rank);

        //Start the auto-saving runnable
        autoSaveRunnable();
    }

    private void autoSaveRunnable() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                double currentHearts = playerData.getCurrentHearts();
                double currentHeartTokens = playerData.getCurrentHeartTokens();
                String timeSinceSave = String.valueOf(System.currentTimeMillis());

                //Saving these here as they are updated every second; not writing to a file every second...
                fileManager.getDataFile().setValue(FilePlayerKeys.DATA_CURRENT_HEARTS, currentHearts);
                fileManager.getDataFile().setValue(FilePlayerKeys.DATA_CURRENT_HEART_TOKENS, currentHeartTokens);
                fileManager.getDataFile().setValue(FilePlayerKeys.DATA_LAST_TIME_ON, timeSinceSave);

                fileManager.getDataFile().setValue(FilePlayerKeys.DATA_TALENT_LEVELS, talents.getLevelsAsString());

                fileManager.saveFiles();

                handler.postDelayed(this, millisecondsPerSave);
            }
        };
        handler.post(runnable);
    }

    public boolean hasPlayedBefore() {
        return hasPlayedBefore;
    }

    public void setHasPlayedBefore(boolean hasPlayedBefore) {
        this.hasPlayedBefore = hasPlayedBefore;
    }

    public FileManager getFiles() {
        return fileManager;
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
