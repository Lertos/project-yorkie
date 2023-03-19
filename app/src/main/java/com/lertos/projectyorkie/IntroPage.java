package com.lertos.projectyorkie;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lertos.projectyorkie.data.DataManager;

public class IntroPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.page_intro);

        setOnClickListener();
    }

    private void setOnClickListener() {
        //Set the confirm button to bring the player to the normal screen they requested
        findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            DataManager.getInstance().setHasPlayedBefore(true);
            Helper.chooseActivityToSwitchTo(this, HomePage.class);
        });
    }
}
