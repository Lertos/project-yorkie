package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.model.Talent;
import com.lertos.projectyorkie.model.TalentBonusType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Talents {

    private List<Talent> listTalents;
    private final String separator;

    public static final Talent heartBeater = new Talent(
            "Heart Beater",
            "Increases hearts per second",
            TalentBonusType.PERCENTAGE,
            1,
            5,
            0,
            3,
            50
    );
    public static final Talent luckyStreak = new Talent(
            "Lucky Streak",
            "Increases chance at gaining heart tokens",
            TalentBonusType.PERCENTAGE,
            1,
            5,
            0,
            3,
            50
    );
    public static final Talent bargainMaster = new Talent(
            "Bargain Master",
            "Decreases cost of upgrading talents",
            TalentBonusType.PERCENTAGE,
            -1,
            6,
            0,
            2,
            25
    );
    public static final Talent petLover = new Talent(
            "Pet Lover",
            "Decreases cost of upgrading activities",
            TalentBonusType.PERCENTAGE,
            -1,
            6,
            0,
            2,
            25
    );
    public static final Talent greatMinds = new Talent(
            "Great Minds",
            "Decreases cost of unlocking activities",
            TalentBonusType.PERCENTAGE,
            -1,
            6,
            0,
            2,
            25
    );
    public static final Talent purrsuasion = new Talent(
            "Purrsuasion",
            "Use less heart tokens when playing minigames",
            TalentBonusType.PERCENTAGE,
            -1,
            6,
            0,
            5,
            25
    );
    public static final Talent strengthInNumbers = new Talent(
            "Strength In Numbers",
            "Get more hearts based on your pack bonus",
            TalentBonusType.PERCENTAGE,
            1,
            4.5,
            0,
            0.06,
            100
    );
    public static final Talent pettingPower = new Talent(
            "Petting Power",
            "Petting Minigame - Squares don't disappear as quick",
            TalentBonusType.FLAT,
            1,
            4.5,
            1,
            1,
            100
    );
    public static final Talent pupPrecision = new Talent(
            "Pup Precision",
            "Petting Minigame - Correct squares add more time to the timer",
            TalentBonusType.FLAT,
            1,
            5,
            0,
            0.05,
            50
    );
    public static final Talent laxTreatment = new Talent(
            "Lax Treat-ment",
            "Petting Minigame - Lose less time when missing squares",
            TalentBonusType.FLAT,
            1,
            5,
            0,
            0.06,
            50
    );
    public static final Talent canineFocus = new Talent(
            "Canine Focus",
            "Tournaments - Less reaction time needed for actions",
            TalentBonusType.FLAT,
            1,
            4.5,
            1,
            1,
            100
    );
    public static final Talent cutenessFactor = new Talent(
            "Cuteness Factor",
            "Tournaments - Get more points for each action",
            TalentBonusType.PERCENTAGE,
            1,
            4.5,
            0,
            0.06,
            100
    );

    public Talents() {
        this.listTalents = new ArrayList<>();
        this.separator = DataManager.getInstance().getFiles().getDataFile().getValueSeparator();

        listTalents.add(heartBeater);
        listTalents.add(luckyStreak);
        listTalents.add(bargainMaster);
        listTalents.add(petLover);
        listTalents.add(greatMinds);
        listTalents.add(purrsuasion);
        listTalents.add(strengthInNumbers);
        listTalents.add(pettingPower);
        listTalents.add(pupPrecision);
        listTalents.add(laxTreatment);
        listTalents.add(canineFocus);
        listTalents.add(cutenessFactor);
    }

    public void setInitialLevels(String strLevels) {
        if (strLevels.isEmpty())
            return;

        String[] levels = strLevels.split(separator);

        if (levels.length != listTalents.size())
            throw new RuntimeException("Talents: Saved value does not have equal # of elements");

        for (int i = 0; i < levels.length; i++) {
            int level = Integer.parseInt(levels[i]);

            listTalents.get(i).setCurrentLevel(level);
        }
    }

    public String getLevelsAsString() {
        StringBuilder sb = new StringBuilder();

        for (Talent talent : listTalents) {
            sb.append(talent.getCurrentLevel()).append(separator.replace("\\", ""));
        }
        String finalString = sb.toString();
        return finalString.substring(0, finalString.length() - 1);
    }

    public List<Talent> getListTalents() {
        return Collections.unmodifiableList(listTalents);
    }

}
