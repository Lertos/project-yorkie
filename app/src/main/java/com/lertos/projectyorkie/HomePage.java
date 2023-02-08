package com.lertos.projectyorkie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lertos.projectyorkie.adapters.PackViewAdapter;
import com.lertos.projectyorkie.adapters.TalentsViewAdapter;
import com.lertos.projectyorkie.data.DataManager;

public class HomePage extends AppCompatActivity {

    static boolean hasStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        //Since this is the main/launcher activity, load the data here
        if (!hasStarted) {
            loadMainData();
            hasStarted = true;
        }

        setupBottomButtonBar();
        setupRecyclerViews();
        setupPageButtonBar();
        setupCharacterInfo();
    }

    private void loadMainData() {
        //Setup the data and have it all created on startup
        DataManager.getInstance().start();

        //Run the game loop - mainly for increasing the hearts per second
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DataManager.getInstance().setHeartsPerSecond();

                double currentHearts = DataManager.getInstance().getPlayerData().getCurrentHearts();
                double currentHeartsPerSecond = DataManager.getInstance().getPlayerData().getCurrentHeartsPerSecond();

                DataManager.getInstance().getPlayerData().setCurrentHearts(currentHearts + currentHeartsPerSecond);

                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }

    //To ensure the start logic only happens when the app initially starts or this activity is destroyed
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            hasStarted = false;
        }
    }

    private void setupRecyclerViews() {
        Helper.createNewRecyclerView(
                findViewById(R.id.recyclerViewTalents),
                DataManager.getInstance().getTalents(),
                new TalentsViewAdapter(),
                this
        );

        Helper.createNewRecyclerView(
                findViewById(R.id.recyclerViewPack),
                DataManager.getInstance().getPackDogs(),
                new PackViewAdapter(),
                this
        );
    }

    private void switchActivities(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        //Stop the animation of switching between activities
        overridePendingTransition(0, 0);
    }

    public void setupPageButtonBar() {
        findViewById(R.id.button_talents).setOnClickListener(v -> {
            findViewById(R.id.recyclerViewTalents).setVisibility(View.VISIBLE);
            findViewById(R.id.recyclerViewPack).setVisibility(View.GONE);
        });

        findViewById(R.id.button_pack).setOnClickListener(v -> {
            findViewById(R.id.recyclerViewTalents).setVisibility(View.GONE);
            findViewById(R.id.recyclerViewPack).setVisibility(View.VISIBLE);
        });
    }

    public void setupBottomButtonBar() {
        findViewById(R.id.button_home).setOnClickListener(v -> { switchActivities(HomePage.class); });
        findViewById(R.id.button_activities).setOnClickListener(v -> { switchActivities(ActivityPage.class); });
    }

    private void setupCharacterInfo() {
        TextView currentHearts = findViewById(R.id.tcCurrentHearts);
        TextView currentHeartTokens = findViewById(R.id.tcCurrentHeartTokens);
        TextView currentDogsCollected = findViewById(R.id.tcCurrentDogsCollected);

        currentHearts.setText(
                Helper.createSpannable(
                        getResources().getString(R.string.character_heart_amount),
                        " " + DataManager.getInstance().getPlayerData().getCurrentHearts(),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        currentHeartTokens.setText(
                Helper.createSpannable(
                        getResources().getString(R.string.character_heart_token_amount),
                        " " + DataManager.getInstance().getPlayerData().getCurrentHeartTokens(),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);

        currentDogsCollected.setText(
                Helper.createSpannable(
                        getResources().getString(R.string.character_dogs_collected),
                        " " + DataManager.getInstance().getPlayerData().getDogsCollected(),
                        DataManager.getInstance().getPlayerData().getHighlightColor()
                ),
                TextView.BufferType.SPANNABLE);
    }

}
