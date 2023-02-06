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
                        1,
                        10,
                        2
                )
        );

        listTalents.add(
                new Talent(
                        "Lucky Streak",
                        "Increases chance at gaining heart tokens.",
                        1,
                        2,
                        1
                )
        );

        listTalents.add(
                new Talent(
                        "Bargain Master",
                        "Decreases cost of upgrading talents.",
                        1,
                        16,
                        4
                )
        );
    }

    public List<Talent> getListTalents() {
        return Collections.unmodifiableList(listTalents);
    }

}
