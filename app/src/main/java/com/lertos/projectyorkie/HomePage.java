package com.lertos.projectyorkie;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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

        if (!hasStarted) {
            if (DataManager.getInstance().getTimeAwayTotalTime() != null)
                prepareToLoadPopup();
            hasStarted = true;
        }
    }

    private void prepareToLoadPopup() {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.relScreen);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                View view = loadTimeAwayPopup();

                String timeAwayTotalTime = DataManager.getInstance().getTimeAwayTotalTime();
                double timeAwayHeartsGained = DataManager.getInstance().getTimeAwayHeartsGained();
                double timeAwayTokensGained = DataManager.getInstance().getTimeAwayTokensGained();

                ((TextView) view.findViewById(R.id.tvPopupTimeAway)).setText(timeAwayTotalTime);
                ((TextView) view.findViewById(R.id.tvPopupHeartsGained)).setText(IdleNumber.getStrNumber(timeAwayHeartsGained) + " Hearts");
                ((TextView) view.findViewById(R.id.tvPopupTokensGained)).setText(IdleNumber.getStrNumber(timeAwayTokensGained) + " Tokens");

                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_on_off);
                view.findViewById(R.id.tvCloseMessage).startAnimation(anim);
            }
        });
    }

    private void loadMainData() {
        //Setup the data and have it all created on startup
        MediaManager.getInstance().start(this);
        DataManager.getInstance().start(this);
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

    private View loadTimeAwayPopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_time_away, null);

        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int height = RelativeLayout.LayoutParams.MATCH_PARENT;

        //4th argument ignores clicks/taps outside the popup
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        //The view passed doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(findViewById(R.id.relScreen), Gravity.CENTER, 0, 0);

        //Dismiss the popup when ignore clicks to anything else
        popupView.setOnClickListener(v -> {
            v.findViewById(R.id.tvCloseMessage).animate().cancel();

            popupWindow.dismiss();
        });

        return popupWindow.getContentView();
    }

    //To ensure the start logic only happens when the app initially starts or this activity is destroyed
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing())
            hasStarted = false;
        isPageActive = false;
        MediaManager.getInstance().stopSong();
    }

    protected void onPause() {
        super.onPause();
        isPageActive = false;
        if (MediaManager.getInstance().switchedScreens == false)
            MediaManager.getInstance().pauseSong();
        MediaManager.getInstance().switchedScreens = false;
    }

    protected void onResume() {
        super.onResume();
        isPageActive = true;
        MediaManager.getInstance().startSong();
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
                    handler.postDelayed(this, 500);
            }
        };
        handler.post(runnable);
    }

}
