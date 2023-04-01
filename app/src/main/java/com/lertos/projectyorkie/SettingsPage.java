package com.lertos.projectyorkie;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.MediaManager;

public class SettingsPage extends AppCompatActivity {

    private final float sliderMultiplier = 100f;
    private Slider sliderMusicVolume;
    private Slider sliderEffectsVolume;
    private CheckBox cbAppearAnimationsInTournament;
    private Button btnResetTutorials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_settings);

        Helper.setupBottomButtonBar(this);

        sliderMusicVolume = findViewById(R.id.sliderMusicVolume);
        sliderEffectsVolume = findViewById(R.id.sliderEffectsVolume);
        cbAppearAnimationsInTournament = findViewById(R.id.cbAppearAnimationsInTournament);
        btnResetTutorials = findViewById(R.id.btnResetTutorials);

        setInitialValues();
        setOnClickListeners();
    }

    protected void onDestroy() {
        super.onDestroy();
        MediaManager.getInstance().stopSong();
    }

    protected void onPause() {
        super.onPause();

        if (!DataManager.getInstance().switchedScreens) {
            MediaManager.getInstance().pauseSong();
            DataManager.getInstance().setMinimized(true);
        }
        DataManager.getInstance().switchedScreens = false;
    }

    protected void onResume() {
        super.onResume();

        if (DataManager.getInstance().isMinimized()) {
            if (DataManager.getInstance().getTimeAwayTotalTime() != null)
                new PopupTimeAway(this, R.id.relScreen);
            DataManager.getInstance().setMinimized(false);
        }
        MediaManager.getInstance().startSong();
    }

    private void setInitialValues() {
        sliderMusicVolume.setValue(DataManager.getInstance().getSettings().getTrackSongVolume() * sliderMultiplier);
        sliderEffectsVolume.setValue(DataManager.getInstance().getSettings().getTrackEffectVolume() * sliderMultiplier);
        cbAppearAnimationsInTournament.setChecked(DataManager.getInstance().getSettings().isShowAppearAnimationsInTournament());
    }

    private void setOnClickListeners() {
        sliderMusicVolume.addOnChangeListener((slider, value, fromUser) -> MediaManager.getInstance().changeSongTrackVolume(value / sliderMultiplier));
        sliderEffectsVolume.addOnChangeListener((slider, value, fromUser) -> MediaManager.getInstance().changeEffectTrackVolume(value / sliderMultiplier));
        cbAppearAnimationsInTournament.setOnCheckedChangeListener((checkBox, isChecked) -> DataManager.getInstance().getSettings().setShowAppearAnimationsInTournament(isChecked));

        btnResetTutorials.setOnClickListener(v -> {
            DataManager.getInstance().getTutorials().resetAllTutorials();

            Toast.makeText(this, "Tutorials Reset!", Toast.LENGTH_SHORT).show();
        });
    }

}
