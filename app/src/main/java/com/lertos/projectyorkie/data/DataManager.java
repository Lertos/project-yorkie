package com.lertos.projectyorkie.data;

import android.content.Context;
import android.os.Handler;

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
    public boolean switchedScreens = false;
    private boolean isMinimized = true;
    private String timeAwayTotalTime;
    private double timeAwayHeartsGained;
    private double timeAwayTokensGained;
    private static DataManager instance;
    private final long millisecondsPerSave = 3000;
    private final long millisecondsPerSaveForced = 15000;
    private long currentMillisecondsSinceSave = 0;
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

        //Make sure they don't see the intro past the first time
        if (!hasPlayedBefore)
            fileManager.getDataFile().setValue(FilePlayerKeys.DATA_HAS_PLAYED_BEFORE, true);

        tutorialManager = new TutorialManager();
        setTutorialValues();

        settingsManager = new SettingsManager();
        setSettingValues();

        //Now that we have the settings loaded, set settings where need be
        MediaManager.getInstance().setVolumesFromUserPrefs();

        setPlayerValues();

        String separator = DataManager.getInstance().getFiles().getDataFile().getValueSeparator();

        talents = new Talents(separator);
        packDogs = new PackDogs(separator);
        activities = new Activities(separator);

        packDogs.setInitialUnlocks(DataManager.getInstance().getFiles().getDataFile().getString(FilePlayerKeys.DATA_PACK_DOGS_UNLOCKED));
        packDogList = packDogs.getListPackDogs();

        talents.setInitialLevels(DataManager.getInstance().getFiles().getDataFile().getString(FilePlayerKeys.DATA_TALENT_LEVELS));
        talentList = talents.getListTalents();

        activities.setInitialLevels(DataManager.getInstance().getFiles().getDataFile().getString(FilePlayerKeys.DATA_ACTIVITY_LEVELS));
        activityList = activities.getListActivities();

        setHeartsPerSecond();
        setHeartTokensPerSecond();
    }

    private void processTimeAwayRewards() {
        String strLastTimeSaved = fileManager.getDataFile().getString(FilePlayerKeys.DATA_LAST_TIME_ON);

        if (!strLastTimeSaved.isEmpty()) {
            long currentTime = System.currentTimeMillis();
            long lastOnTime = Long.parseLong(strLastTimeSaved);
            long timeAwayInSeconds = (currentTime - lastOnTime) / 1000;

            timeAwayTotalTime = getTimeFromSeconds(timeAwayInSeconds);
            timeAwayHeartsGained = timeAwayInSeconds * playerData.getCurrentHeartsPerSecond();
            timeAwayTokensGained = timeAwayInSeconds * playerData.getCurrentHeartTokensPerSecond();

            addHearts(timeAwayHeartsGained);
            addHeartTokens(timeAwayTokensGained);
        }

        setTimeAwayValue();
    }

    private void setTimeAwayValue() {
        fileManager.getDataFile().setValue(FilePlayerKeys.DATA_LAST_TIME_ON, String.valueOf(System.currentTimeMillis()));
    }

    private String getTimeFromSeconds(long totalSeconds) {
        long runningSeconds = totalSeconds;
        long hours = (int) Math.floor(runningSeconds / 3600.0);

        runningSeconds -= hours * 3600;

        long minutes = (int) Math.floor(runningSeconds / 60.0);

        runningSeconds -= minutes * 60;

        StringBuilder sb = new StringBuilder();

        if (hours > 0)
            sb.append(hours).append("h ");
        if (minutes > 0)
            sb.append(minutes).append("m ");
        sb.append(runningSeconds).append("s");

        return sb.toString();
    }

    public boolean isMinimized() {
        return isMinimized;
    }

    public void setMinimized(boolean minimized) {
        this.isMinimized = minimized;

        //If application was reopened, start all loops back up
        if (!minimized) {
            processTimeAwayRewards();

            startAutoSaveRunnable();
            startMainGameLoop();
        }
        //If minimized, make sure to save all data
        else {
            saveImportantData();
            fileManager.saveFiles();
        }
    }

    public String getTimeAwayTotalTime() {
        return timeAwayTotalTime;
    }

    public double getTimeAwayHeartsGained() {
        return timeAwayHeartsGained;
    }

    public double getTimeAwayTokensGained() {
        return timeAwayTokensGained;
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
    }

    private void startAutoSaveRunnable() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //If a forced save is needed, update the important info
                if (currentMillisecondsSinceSave >= millisecondsPerSaveForced) {
                    saveImportantData();
                }
                //If any changes were made, save them
                if (fileManager.isAnySaveNeeded()) {
                    fileManager.saveFiles();

                    //Reset the time since saved
                    currentMillisecondsSinceSave = 0;
                } else {
                    currentMillisecondsSinceSave += millisecondsPerSave;
                }

                if (isMinimized)
                    handler.removeCallbacks(this);
                else
                    handler.postDelayed(this, millisecondsPerSave);
            }
        };
        handler.post(runnable);
    }

    private void saveImportantData() {
        double currentHearts = playerData.getCurrentHearts();
        double currentHeartTokens = playerData.getCurrentHeartTokens();

        //Saving these here as they are updated every second; not writing to a file every second...
        fileManager.getDataFile().setValue(FilePlayerKeys.DATA_CURRENT_HEARTS, currentHearts);
        fileManager.getDataFile().setValue(FilePlayerKeys.DATA_CURRENT_HEART_TOKENS, currentHeartTokens);

        setTimeAwayValue();

        fileManager.getDataFile().setValue(FilePlayerKeys.DATA_PACK_DOGS_UNLOCKED, packDogs.getUnlocksAsString());
        fileManager.getDataFile().setValue(FilePlayerKeys.DATA_TALENT_LEVELS, talents.getLevelsAsString());
        fileManager.getDataFile().setValue(FilePlayerKeys.DATA_ACTIVITY_LEVELS, activities.getLevelsAsString());
    }

    private void startMainGameLoop() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DataManager.getInstance().calculateHeartsPerSecond();
                DataManager.getInstance().calculateHeartTokensPerSecond();

                if (isMinimized)
                    handler.removeCallbacks(this);
                else
                    handler.postDelayed(this, 1000);
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