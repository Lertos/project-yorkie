package com.lertos.projectyorkie;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.MediaManager;

public class TutorialPage extends AppCompatActivity {

    private String simpleClassName;
    private String className;
    private int layoutId;
    private int viewStubId;
    private Button btnContinue;
    private Button btnConfirm;
    private TextView tvPageExplanation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simpleClassName = getIntent().getStringExtra("SIMPLE_CLASS_NAME");
        className = getIntent().getStringExtra("CLASS_NAME");
        layoutId = getIntent().getIntExtra("LAYOUT_ID", -1);
        viewStubId = getIntent().getIntExtra("VIEW_STUB_ID", -1);

        if (layoutId == -1 || viewStubId == -1)
            super.finish();

        setContentView(R.layout.page_tutorial);

        btnContinue = findViewById(R.id.btnContinue);
        btnConfirm = findViewById(R.id.btnConfirm);
        tvPageExplanation = findViewById(R.id.tvPageExplanation);

        setTutorialHeader();
        setOnClickListener();
        setExplanation();
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
            new PopupTimeAway(this, R.id.btnConfirm);
            DataManager.getInstance().setMinimized(false);
        }

        MediaManager.getInstance().startSong();
    }

    private void setTutorialHeader() {
        StringBuilder sb = new StringBuilder();
        String upperStr = simpleClassName.toUpperCase();

        for (int i = 0; i < simpleClassName.length(); i++) {
            char ch = simpleClassName.charAt(i);

            if (ch == upperStr.charAt(i))
                sb.append(" ");
            sb.append(ch);
        }

        //Set the header
        ((TextView) findViewById(R.id.tvPageName)).setText(sb.toString());
    }

    private void setOnClickListener() {
        //Set the continue button to hide the explanation and show the actual tutorial
        btnContinue.setOnClickListener(v -> {
            tvPageExplanation.setVisibility(View.GONE);

            //Inflate the specific layout for the chosen page
            inflateStub();

            btnContinue.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.VISIBLE);
        });

        //Set the confirm button to bring the player to the normal screen they requested
        btnConfirm.setOnClickListener(v -> {
            try {
                Class<?> activity = Class.forName(className);

                DataManager.getInstance().getTutorials().setTutorialAsSeen(className);

                Helper.chooseActivityToSwitchTo(this, activity);
            } catch (ClassNotFoundException e) {
                Helper.chooseActivityToSwitchTo(this, HomePage.class);
            }
        });
    }

    private void setExplanation() {
        if (simpleClassName.equalsIgnoreCase(HomePage.class.getSimpleName()))
            tvPageExplanation.setText(R.string.tutorial_home_page);
        else if (simpleClassName.equalsIgnoreCase(ActivityPage.class.getSimpleName()))
            tvPageExplanation.setText(R.string.tutorial_activity_page);
        else if (simpleClassName.equalsIgnoreCase(PettingPage.class.getSimpleName()))
            tvPageExplanation.setText(R.string.tutorial_petting_page);
        else if (simpleClassName.equalsIgnoreCase(TournamentPage.class.getSimpleName()))
            tvPageExplanation.setText(R.string.tutorial_tournament_page);
    }

    private void inflateStub() {
        ViewStub stub = findViewById(viewStubId);

        stub.setLayoutResource(layoutId);
        stub.inflate();
    }
}
