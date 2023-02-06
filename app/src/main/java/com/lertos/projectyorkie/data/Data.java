package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.model.PackDog;

import java.util.ArrayList;
import java.util.List;

public class Data {

    private static Data instance;
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
    }

    public List<PackDog> getPackDogs() {
        return packDogList;
    }
}
