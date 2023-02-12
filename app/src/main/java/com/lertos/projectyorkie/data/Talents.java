package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.model.Talent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Talents {

    private List<Talent> listTalents;

    public Talents() {
        this.listTalents = new ArrayList<>();

        listTalents.add(
                new Talent(
                        "Heart Beater",
                        "Increases hearts per second.",
                        8,
                        1.19,
                        2,
                        1
                )
        );

        listTalents.add(
                new Talent(
                        "Lucky Streak",
                        "Increases chance at gaining heart tokens.",
                        80,
                        1.18,
                        4,
                        1.5
                )
        );

        listTalents.add(
                new Talent(
                        "Bargain Master",
                        "Decreases cost of upgrading talents.",
                        800,
                        1.17,
                        6,
                        2.4
                )
        );
    }

    public List<Talent> getListTalents() {
        return Collections.unmodifiableList(listTalents);
    }

}
