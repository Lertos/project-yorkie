package com.lertos.projectyorkie;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_settings);

        Helper.setupBottomButtonBar(this);
    }
}
