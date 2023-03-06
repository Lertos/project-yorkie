package com.lertos.projectyorkie;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.lertos.projectyorkie.adapters.PackViewAdapter;
import com.lertos.projectyorkie.adapters.TalentsViewAdapter;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.MediaManager;

public class HomePage extends AppCompatActivity {

    static boolean hasStarted = false;
    static boolean isPageActive = false;

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
            updateUIWithCurrentData();
            isPageActive = true;
        }

        Helper.setupBottomButtonBar(this);
        setupRecyclerViews();
        setupPageButtonBar();
    }

    private void loadMainData() {
        //Setup the data and have it all created on startup
        MediaManager.getInstance().start(this);
        DataManager.getInstance().start();
        DataManager.getInstance().getPlayerData().setHighlightColor(ContextCompat.getColor(this, R.color.main_text_color));

        //Run the game loop
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DataManager.getInstance().calculateHeartsPerSecond();
                DataManager.getInstance().calculateHeartTokensPerSecond();

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

    protected void onPause() {
        super.onPause();
        isPageActive = false;
    }

    protected void onResume() {
        super.onResume();
        isPageActive = true;
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

    public void setupPageButtonBar() {
        findViewById(R.id.btnTalents).setOnClickListener(v -> {
            findViewById(R.id.recyclerViewTalents).setVisibility(View.VISIBLE);
            findViewById(R.id.recyclerViewPack).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tvHomepageTabHeader)).setText("Talents");
        });

        findViewById(R.id.btnPackDogs).setOnClickListener(v -> {
            findViewById(R.id.recyclerViewTalents).setVisibility(View.GONE);
            findViewById(R.id.recyclerViewPack).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tvHomepageTabHeader)).setText("Your Pack");
        });
    }

    private void updateUIWithCurrentData() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TextView currentHearts = findViewById(R.id.tvCurrentHearts);
                TextView currentHeartTokens = findViewById(R.id.tvCurrentHeartTokens);

                currentHearts.setText(IdleNumber.getStrNumber(DataManager.getInstance().getPlayerData().getCurrentHearts()));
                currentHeartTokens.setText(IdleNumber.getStrNumber(DataManager.getInstance().getPlayerData().getCurrentHeartTokens()));

                if (!isPageActive)
                    handler.removeCallbacks(this);
                else
                    handler.postDelayed(this, 500);
            }
        };
        handler.post(runnable);
    }

}
