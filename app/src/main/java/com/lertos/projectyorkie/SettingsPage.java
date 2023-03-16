package com.lertos.projectyorkie;

import android.os.Bundle;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;
import com.lertos.projectyorkie.data.MediaManager;

public class SettingsPage extends AppCompatActivity {

    private Slider sliderMusicVolume;
    private Slider sliderEffectsVolume;
    private CheckBox cbAppearAnimationsInTournament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_settings);

        Helper.setupBottomButtonBar(this);

        sliderMusicVolume = findViewById(R.id.sliderMusicVolume);
        sliderEffectsVolume = findViewById(R.id.sliderEffectsVolume);
        cbAppearAnimationsInTournament = findViewById(R.id.cbAppearAnimationsInTournament);

        setOnClickListeners();
    }

    private void setOnClickListeners() {
        sliderMusicVolume.addOnChangeListener((slider, value, fromUser) -> {
            MediaManager.getInstance().changeSongTrackVolume(value / 100);
        });

        sliderEffectsVolume.addOnChangeListener((slider, value, fromUser) -> {
            MediaManager.getInstance().changeEffectTrackVolume(value / 100);
        });

        cbAppearAnimationsInTournament.setOnClickListener((checkBox) -> {

        });
    }

}
