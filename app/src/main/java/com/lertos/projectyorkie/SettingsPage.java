package com.lertos.projectyorkie;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;
import com.lertos.projectyorkie.data.MediaManager;

public class SettingsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_settings);

        Helper.setupBottomButtonBar(this);

        setOnClickListeners();
    }

    private void setOnClickListeners() {
        ((Slider) findViewById(R.id.sliderMusicVolume)).addOnChangeListener((slider, value, fromUser) -> {
            MediaManager.getInstance().changeSongTrackVolume(value / 100);
        });

        ((Slider) findViewById(R.id.sliderEffectsVolume)).addOnChangeListener((slider, value, fromUser) -> {
            MediaManager.getInstance().changeEffectTrackVolume(value / 100);
        });
    }

}
