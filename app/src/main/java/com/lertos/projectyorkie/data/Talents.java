package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.model.Talent;
import com.lertos.projectyorkie.model.TalentBonusType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Talents {

    private List<Talent> listTalents;
    public static final String talentHeartBeater = "Heart Beater";
    public static final String talentLuckyStreak = "Lucky Streak";
    public static final String talentBargainMaster = "Bargain Master";
    //public static final String talent = "";

    public Talents() {
        this.listTalents = new ArrayList<>();

        listTalents.add(
                new Talent(
                        talentHeartBeater,
                        "Increases hearts per second",
                        TalentBonusType.PERCENTAGE,
                        1,
                        5,
                        0,
                        3,
                        50
                )
        );

        listTalents.add(
                new Talent(
                        talentLuckyStreak,
                        "Increases chance at gaining heart tokens",
                        TalentBonusType.PERCENTAGE,
                        1,
                        5,
                        0,
                        3,
                        50
                )
        );

        listTalents.add(
                new Talent(
                        talentBargainMaster,
                        "Decreases cost of upgrading talents",
                        TalentBonusType.PERCENTAGE,
                        -1,
                        6,
                        0,
                        2,
                        25
                )
        );

    }

    public List<Talent> getListTalents() {
        return Collections.unmodifiableList(listTalents);
    }

    public Talent getTalentByName(String name) {
        for (Talent talent : this.listTalents) {
            if (talent.getName().equalsIgnoreCase(name))
                return talent;
        }
        return null;
    }

}
