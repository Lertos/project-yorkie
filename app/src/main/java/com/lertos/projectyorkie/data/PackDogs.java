package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.model.PackDog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PackDogs {

    private List<PackDog> listPackDogs;

    public PackDogs() {
        this.listPackDogs = new ArrayList<>();

        listPackDogs.add(new PackDog("Border Collie", R.mipmap.pack_border_collie));
        listPackDogs.add(new PackDog("Boxer", R.mipmap.pack_boxer));
        listPackDogs.add(new PackDog("Bull Terrier", R.mipmap.pack_bull_terrier));

        //TODO: Read from the file - or update the list after reading the player data "dogs_unlocked" section
        listPackDogs.get(0).setUnlocked(true);
    }

    public List<PackDog> getListPackDogs() {
        return Collections.unmodifiableList(listPackDogs);
    }
}
