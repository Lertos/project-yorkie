package com.lertos.projectyorkie;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.lertos.projectyorkie.adapters.ActivityViewAdapter;
import com.lertos.projectyorkie.data.DataManager;

public class ActivityPage extends HomePage {

    TextView activityCurrentHearts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_page);

        activityCurrentHearts = findViewById(R.id.activityCurrentHearts);

        setupBottomButtonBar();
        continuouslyUpdateHearts();

        Helper.createNewRecyclerView(
                findViewById(R.id.recyclerViewActivities),
                DataManager.getInstance().getActivities(),
                new ActivityViewAdapter(),
                this
        );
    }

    private void continuouslyUpdateHearts() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                double currentHearts = DataManager.getInstance().getPlayerData().getCurrentHearts();

                activityCurrentHearts.setText(String.valueOf(currentHearts));
                DataManager.getInstance().getPlayerData().setCurrentHearts(currentHearts + 10);

                handler.postDelayed(this, 500);
            }
        };
        handler.post(runnable);
    }
}
