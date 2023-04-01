package com.lertos.projectyorkie;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.MediaManager;

public class IntroPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.page_intro);

        setOnClickListener();
        showSlowly();
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
        MediaManager.getInstance().startSong();
    }

    private void setOnClickListener() {
        //Set the confirm button to bring the player to the normal screen they requested
        findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            DataManager.getInstance().setHasPlayedBefore(true);
            Helper.chooseActivityToSwitchTo(this, HomePage.class);
        });
    }

    private void showSlowly() {
        int timeBetween = 3000;

        TextView tvHeader = findViewById(R.id.tvHeader);
        TextView tvLine1 = findViewById(R.id.tvLine1);
        TextView tvLine2 = findViewById(R.id.tvLine2);
        TextView tvLine3 = findViewById(R.id.tvLine3);
        TextView tvLine4 = findViewById(R.id.tvLine4);
        TextView tvLine5 = findViewById(R.id.tvLine5);
        TextView tvLine6 = findViewById(R.id.tvLine6);

        tvHeader.setAlpha(0f);
        tvLine1.setAlpha(0f);
        tvLine2.setAlpha(0f);
        tvLine3.setAlpha(0f);
        tvLine4.setAlpha(0f);
        tvLine5.setAlpha(0f);
        tvLine6.setAlpha(0f);

        tvHeader.animate().alpha(1f).setDuration(timeBetween).withEndAction(() -> {
            tvLine1.animate().alpha(1f).setDuration(timeBetween).withEndAction(() -> {
                tvLine2.animate().alpha(1f).setDuration(timeBetween).withEndAction(() -> {
                    tvLine3.animate().alpha(1f).setDuration(timeBetween).withEndAction(() -> {
                        tvLine4.animate().alpha(1f).setDuration(timeBetween).withEndAction(() -> {
                            MediaManager.getInstance().stopSong();
                            MediaManager.getInstance().playEffectTrack(R.raw.effect_gong);

                            tvLine5.animate().alpha(1f).setDuration(timeBetween * 2).withEndAction(() -> {
                                MediaManager.getInstance().playSongTrack(R.raw.music_main_loop, true);

                                tvLine6.animate().alpha(1f).setDuration(timeBetween / 2).withEndAction(() -> findViewById(R.id.btnConfirm).setVisibility(View.VISIBLE));
                            });
                        });
                    });
                });
            });
        });
    }
}
