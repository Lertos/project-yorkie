package com.lertos.projectyorkie;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.lertos.projectyorkie.adapters.ActivityViewAdapter;
import com.lertos.projectyorkie.data.DataManager;

public class ActivityPage extends HomePage {

    static boolean isPageActive = false;

    TextView activityCurrentHearts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_page);

        activityCurrentHearts = findViewById(R.id.activityCurrentHearts);
        isPageActive = true;

        setupBottomButtonBar();
        updateUIWithCurrentHearts();

        Helper.createNewRecyclerView(
                findViewById(R.id.recyclerViewActivities),
                DataManager.getInstance().getActivities(),
                new ActivityViewAdapter(),
                this
        );
    }

    protected void onDestroy() { super.onDestroy(); isPageActive = false; }

    protected void onResume() { super.onResume(); isPageActive = true; }

    protected void onPause() { super.onPause(); isPageActive = false; }

    private void updateUIWithCurrentHearts() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                activityCurrentHearts.setText(String.valueOf(DataManager.getInstance().getPlayerData().getCurrentHearts()));

                handler.postDelayed(this, 100);
            }
        };
        handler.post(runnable);
    }
}
