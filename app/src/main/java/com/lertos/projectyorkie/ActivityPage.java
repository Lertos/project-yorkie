package com.lertos.projectyorkie;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lertos.projectyorkie.adapters.ActivityViewAdapter;
import com.lertos.projectyorkie.data.DataManager;

public class ActivityPage extends AppCompatActivity {

    static boolean isPageActive = false;

    private TextView tvCurrentHearts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_activities);

        tvCurrentHearts = findViewById(R.id.tvCurrentHearts);

        Helper.setupBottomButtonBar(this);

        if (!isPageActive) {
            updateUIWithCurrentHearts();
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
    }

    protected void onPause() {
        super.onPause();
        isPageActive = false;
    }

    protected void onResume() {
        super.onResume();
        isPageActive = true;
    }

    private void updateUIWithCurrentHearts() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                tvCurrentHearts.setText(IdleNumber.getStrNumber(DataManager.getInstance().getPlayerData().getCurrentHearts()));

                if (!isPageActive)
                    handler.removeCallbacks(this);
                else
                    handler.postDelayed(this, 500);
            }
        };
        handler.post(runnable);
    }
}
