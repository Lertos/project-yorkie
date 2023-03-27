package com.lertos.projectyorkie.data;

import com.lertos.projectyorkie.R;
import com.lertos.projectyorkie.model.PackDog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PackDogs {

    private List<PackDog> listPackDogs;
    private final String separator;

    public PackDogs(String separator) {
        this.listPackDogs = new ArrayList<>();
        this.separator = separator;

        listPackDogs.add(new PackDog("Border Collie", R.mipmap.pack_border_collie));
        listPackDogs.add(new PackDog("Boxer", R.mipmap.pack_boxer));
        listPackDogs.add(new PackDog("Bull Terrier", R.mipmap.pack_bull_terrier));
        listPackDogs.add(new PackDog("Bulldog", R.mipmap.pack_bulldog));
        listPackDogs.add(new PackDog("King Charles Spaniel", R.mipmap.pack_cavalier_king_charles_spaniel));
        listPackDogs.add(new PackDog("Chihuahua", R.mipmap.pack_chihuahua));
        listPackDogs.add(new PackDog("Cocker Spaniel", R.mipmap.pack_cocker_spaniel));
        listPackDogs.add(new PackDog("Dachshund", R.mipmap.pack_dachshund));
        listPackDogs.add(new PackDog("Dalmation", R.mipmap.pack_dalmation));
        listPackDogs.add(new PackDog("Doberman", R.mipmap.pack_doberman));
        listPackDogs.add(new PackDog("French Bulldog", R.mipmap.pack_french_bulldog));
        listPackDogs.add(new PackDog("German Shepherd", R.mipmap.pack_german_shepherd));
        listPackDogs.add(new PackDog("Golden Retriever", R.mipmap.pack_golden_retriever));
        listPackDogs.add(new PackDog("Great Dane", R.mipmap.pack_great_dane));
        listPackDogs.add(new PackDog("Hound", R.mipmap.pack_hound));
        listPackDogs.add(new PackDog("Jack Russell Terrier", R.mipmap.pack_jack_russell_terrier));
        listPackDogs.add(new PackDog("Labrador Retriever", R.mipmap.pack_labrador_retriever));
        listPackDogs.add(new PackDog("Pitbull", R.mipmap.pack_pitbull));
        listPackDogs.add(new PackDog("Pomeranian", R.mipmap.pack_pomeranian));
        listPackDogs.add(new PackDog("Poodle", R.mipmap.pack_poodle));
        listPackDogs.add(new PackDog("Pug", R.mipmap.pack_pug));
        listPackDogs.add(new PackDog("Rottweiler", R.mipmap.pack_rottweiler));
        listPackDogs.add(new PackDog("Saint Bernard", R.mipmap.pack_saint_bernard));
        listPackDogs.add(new PackDog("Saluki", R.mipmap.pack_saluki));
        listPackDogs.add(new PackDog("Schnauzer", R.mipmap.pack_schnauzer));
        listPackDogs.add(new PackDog("Shih Tzu", R.mipmap.pack_shih_tzu));
        listPackDogs.add(new PackDog("Siberian Husky", R.mipmap.pack_siberian_husky));

        //TODO: Read from the file - or update the list after reading the player data "dogs_unlocked" section
        listPackDogs.get(0).setUnlocked(true);
    }

    public List<PackDog> getListPackDogs() {
        listPackDogs.sort(new SortByName());
        return listPackDogs;
    }

    static class SortByName implements Comparator<PackDog> {
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
