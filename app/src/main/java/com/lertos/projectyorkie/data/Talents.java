package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.model.Talent;
import com.lertos.projectyorkie.model.TalentBonusType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Talents {

    private List<Talent> listTalents;
    //Finished adding through code
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
    //Finished adding through code
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
    //Finished adding through code
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
    //Finished adding through code
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
    //Finished adding through code
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
            "Tournaments - Focus to slow down time",
            TalentBonusType.FLAT,
            1,
            5,
            0,
            0.06,
            50
    );

    public static final Talent cutenessFactor = new Talent(
            "Cuteness Factor",
            "Tournaments - Get more points for each action",
            TalentBonusType.FLAT,
            1,
            5,
            0,
            0.06,
            50
    );

    public Talents() {
        this.listTalents = new ArrayList<>();

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

    public List<Talent> getListTalents() {
        return Collections.unmodifiableList(listTalents);
    }

}
