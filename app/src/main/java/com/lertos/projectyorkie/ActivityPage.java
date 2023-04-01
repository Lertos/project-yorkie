package com.lertos.projectyorkie;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lertos.projectyorkie.adapters.ActivityViewAdapter;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.MediaManager;

public class ActivityPage extends AppCompatActivity {

    static boolean isPageActive = false;

    private TextView tvCurrentHearts;
    private TextView tvCurrentHeartsPerSec;
    private TextView tvCurrentHeartTokens;
    private TextView tvCurrentHeartTokensPerSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_activities);

        tvCurrentHearts = findViewById(R.id.tvCurrentHearts);
        tvCurrentHeartsPerSec = findViewById(R.id.tvCurrentHeartsPerSec);
        tvCurrentHeartTokens = findViewById(R.id.tvCurrentHeartTokens);
        tvCurrentHeartTokensPerSec = findViewById(R.id.tvCurrentHeartTokensPerSec);

        Helper.setupBottomButtonBar(this);

        if (!isPageActive) {
            updateUIWithCurrentData();
            isPageActive = true;
        }

        Helper.createNewRecyclerView(
                findViewById(R.id.recyclerView),
                DataManager.getInstance().getActivities(),
                new ActivityViewAdapter(),
                this
        );
    }

    protected void onDestroy() {
        super.onDestroy();
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

    private void updateUIWithCurrentData() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tvCurrentHearts.setText(IdleNumber.getStrNumber(DataManager.getInstance().getPlayerData().getCurrentHearts()));
                tvCurrentHeartsPerSec.setText(IdleNumber.getStrNumber(DataManager.getInstance().getPlayerData().getCurrentHeartsPerSecond()));
                tvCurrentHeartTokens.setText(IdleNumber.getStrNumber(DataManager.getInstance().getPlayerData().getCurrentHeartTokens()));
                tvCurrentHeartTokensPerSec.setText(String.format("%.2f", DataManager.getInstance().getPlayerData().getCurrentHeartTokensPerSecond()));

                if (!isPageActive)
                    handler.removeCallbacks(this);
                else
                    handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }
}
