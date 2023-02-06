package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.model.PackDog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        listPackDogs.sort(new SortByName());
        return Collections.unmodifiableList(listPackDogs);
    }

    class SortByName implements Comparator<PackDog> {
        public int compare(PackDog o1, PackDog o2) {
            boolean b1 = o1.isUnlocked();
            boolean b2 = o2.isUnlocked();
            int bCompare = Boolean.compare(b1, b2);

            //Want to show unlocked first
            if (bCompare != 0)
                return -bCompare;

            //If both are the same boolean value, sort by name
            String s1 = o1.getName();
            String s2 = o2.getName();

            return s1.compareTo(s2);
        }
    }
}
