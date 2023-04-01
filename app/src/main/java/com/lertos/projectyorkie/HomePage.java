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

        //Since this is the main/launcher activity, load the data here
        if (!hasStarted) {
            loadMainData();
            MediaManager.getInstance().playSongTrack(R.raw.music_main_loop, true);
        }

        //Now that the data has loaded, check if this is their first time
        if (!DataManager.getInstance().hasPlayedBefore())
            Helper.chooseActivityToSwitchTo(this, IntroPage.class);

        setContentView(R.layout.page_home);

        if (!isPageActive) {
            updateUIWithCurrentData();
            isPageActive = true;
        }

        Helper.setupBottomButtonBar(this);
        setupRecyclerViews();
        setupPageButtonBar();

        if (!hasStarted)
            hasStarted = true;
    }

    protected void onDestroy() {
        super.onDestroy();
        //To ensure the start logic only happens when the app initially starts or this activity is destroyed
        if (isFinishing())
            hasStarted = false;

        isPageActive = false;
        MediaManager.getInstance().stopSong();
    }

    protected void onPause() {
        super.onPause();
        isPageActive = false;

        if (!DataManager.getInstance().switchedScreens) {
            MediaManager.getInstance().pauseSong();
            DataManager.getInstance().setMinimized(true);
        }
        DataManager.getInstance().switchedScreens = false;
    }

    protected void onResume() {
        super.onResume();
        isPageActive = true;

        if (DataManager.getInstance().isMinimized()) {
            new PopupTimeAway(this, R.id.relScreen);
            DataManager.getInstance().setMinimized(false);
        }
        updateUIWithCurrentData();
        MediaManager.getInstance().startSong();
    }

    private void loadMainData() {
        //Setup the data and have it all created on startup
        MediaManager.getInstance().start(this);
        DataManager.getInstance().start(this);
        DataManager.getInstance().getPlayerData().setHighlightColor(ContextCompat.getColor(this, R.color.main_text_color));
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
        TextView tvTotalPackBonus = findViewById(R.id.tvTotalPackBonus);

        findViewById(R.id.btnTalents).setOnClickListener(v -> {
            findViewById(R.id.recyclerViewTalents).setVisibility(View.VISIBLE);
            findViewById(R.id.recyclerViewPack).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tvHomepageTabHeader)).setText("Talents");

            tvTotalPackBonus.setVisibility(View.GONE);
        });

        findViewById(R.id.btnPackDogs).setOnClickListener(v -> {
            findViewById(R.id.recyclerViewTalents).setVisibility(View.GONE);
            findViewById(R.id.recyclerViewPack).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tvHomepageTabHeader)).setText("Your Pack");

            tvTotalPackBonus.setText(Helper.createSpannable(
                    "Total Pack Bonus: ",
                    String.format("%.1f", DataManager.getInstance().getTotalPackMultiplier()) + "%",
                    DataManager.getInstance().getPlayerData().getHighlightColor()
            ));
            tvTotalPackBonus.setVisibility(View.VISIBLE);
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
                    handler.postDelayed(this, 250);
            }
        };
        handler.post(runnable);
    }

}
