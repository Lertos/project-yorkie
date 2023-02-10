package com.lertos.projectyorkie;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;
import com.lertos.projectyorkie.data.MediaManager;

public class PettingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_petting);

        Helper.setupBottomButtonBar(this);

        setOnClickListeners();
    }

    private void setOnClickListeners() {

    }

}
