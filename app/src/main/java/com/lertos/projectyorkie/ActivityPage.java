package com.lertos.projectyorkie;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lertos.projectyorkie.adapters.ActivityViewAdapter;
import com.lertos.projectyorkie.data.DataManager;

public class ActivityPage extends AppCompatActivity {

    static boolean isPageActive = false;

    TextView activityCurrentHearts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_activities);

        activityCurrentHearts = findViewById(R.id.activityCurrentHearts);

        Helper.setupBottomButtonBar(this);

        if (!isPageActive) {
            updateUIWithCurrentHearts();
            isPageActive = true;
        }

        Helper.createNewRecyclerView(
                findViewById(R.id.recyclerViewActivities),
                DataManager.getInstance().getActivities(),
                new ActivityViewAdapter(),
                this
        );
    }

    protected void onDestroy() { super.onDestroy(); isPageActive = false; }

    protected void onPause() { super.onPause(); isPageActive = false; }

    protected void onResume() { super.onResume(); isPageActive = true; }

    private void updateUIWithCurrentHearts() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                activityCurrentHearts.setText(Helper.formatNumber(DataManager.getInstance().getPlayerData().getCurrentHearts()));

                if(!isPageActive)
                    handler.removeCallbacks(this);
                else
                    handler.postDelayed(this, 500);
            }
        };
        handler.post(runnable);
    }
}
