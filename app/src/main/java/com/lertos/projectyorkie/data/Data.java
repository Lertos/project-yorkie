package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.model.PackDog;
import com.lertos.projectyorkie.model.Talent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Data {

    private static Data instance;
    private List<Talent> talentList = new ArrayList<>();
    private List<PackDog> packDogList = new ArrayList<>();

    private Data() {}

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }

        return instance;
    }

    public void start() {
        PackDogs packDogs = new PackDogs();
        packDogList = packDogs.getListPackDogs();

        Talents talents = new Talents();
        talentList = talents.getListTalents();
    }

    public List<PackDog> getPackDogs() {
        return Collections.unmodifiableList(packDogList);
    }

    public List<Talent> getTalents() {
        return Collections.unmodifiableList(talentList);
    }
}
