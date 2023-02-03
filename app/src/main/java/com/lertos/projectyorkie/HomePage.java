package com.lertos.projectyorkie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        setupBottomButtonBar();
    }

    private void switchActivities(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        //Stop the animation of switching between activities
        overridePendingTransition(0, 0);
    }

    public void setupBottomButtonBar() {
        findViewById(R.id.button_home).setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Switching to home page", Toast.LENGTH_SHORT).show();
            switchActivities(HomePage.class);
        });

        findViewById(R.id.button_activities).setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Switching to activities page", Toast.LENGTH_SHORT).show();
            switchActivities(ActivityPage.class);
        });
    }

}
