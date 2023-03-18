package com.lertos.projectyorkie;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class TutorialPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String className = getIntent().getStringExtra("CLASS_NAME");
        int layoutId = getIntent().getIntExtra("LAYOUT_ID", -1);

        if (layoutId == -1)
            super.finish();

        Log.d("class", className);
        Log.d("layout", String.valueOf(layoutId));

        //TODO: Need to load the correct tutorial page
        //Also need to set that the page has been viewed in the tutorial (that method would also need to save that
        //to the player data file) once that confirm button is clicked

        setContentView(R.layout.page_petting);
    }
}
