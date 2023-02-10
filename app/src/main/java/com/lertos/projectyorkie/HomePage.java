package com.lertos.projectyorkie;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lertos.projectyorkie.adapters.PackViewAdapter;
import com.lertos.projectyorkie.adapters.TalentsViewAdapter;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.MediaManager;

public class HomePage extends AppCompatActivity {

    static boolean hasStarted = false;
    static boolean isPageActive = false;

    MediaPlayer mainSongLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_home);

        //Since this is the main/launcher activity, load the data here
        if (!hasStarted) {
            loadMainData();

            MediaManager.getInstance().playSongTrack(R.raw.music_main_loop, true);

            hasStarted = true;
        }

        if (!isPageActive) {
            updateUIWithCurrentHearts();
            isPageActive = true;
        }

        Helper.setupBottomButtonBar(this);
        setupRecyclerViews();
        setupPageButtonBar();
        setupCharacterInfo();
    }

    private void loadMainData() {
        //Setup the data and have it all created on startup
        DataManager.getInstance().start();
        MediaManager.getInstance().start(this);

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
        if (isFinishing())
            hasStarted = false;
        isPageActive = false;
    }

    protected void onPause() { super.onPause(); isPageActive = false; }

    protected void onResume() { super.onResume(); isPageActive = true; }

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

    private void updateUIWithCurrentHearts() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TextView currentHearts = findViewById(R.id.tcCurrentHearts);

                currentHearts.setText(
                        Helper.createSpannable(
                                getResources().getString(R.string.character_heart_amount),
                                " " + DataManager.getInstance().getPlayerData().getCurrentHearts(),
                                DataManager.getInstance().getPlayerData().getHighlightColor()
                        ),
                        TextView.BufferType.SPANNABLE);

                if(!isPageActive)
                    handler.removeCallbacks(this);
                else
                    handler.postDelayed(this, 500);
            }
        };
        handler.post(runnable);
    }

}
