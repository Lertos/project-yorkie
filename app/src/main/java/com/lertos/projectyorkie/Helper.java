package com.lertos.projectyorkie;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lertos.projectyorkie.adapters.BindDataToView;
import com.lertos.projectyorkie.data.DataManager;
import com.lertos.projectyorkie.data.MediaManager;
import com.lertos.projectyorkie.data.Tutorial;

import java.util.List;

public class Helper {

    //A wrapper function to create a spannable text object. For convenience
    public static SpannableStringBuilder createSpannable(String str, String appended, int color) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(str + appended);
        spannable.setSpan(new ForegroundColorSpan(color), str.length(), str.length() + appended.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        return spannable;
    }

    //A wrapper function to create a RecyclerView and set the adapter. For convenience
    public static <T extends BindDataToView> void createNewRecyclerView(RecyclerView recyclerView, List<?> arrayList, T viewAdapter, Context context) {
        viewAdapter.setDataList(arrayList);
        recyclerView.setAdapter((RecyclerView.Adapter) viewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public static void setupBottomButtonBar(AppCompatActivity activity) {
        activity.findViewById(R.id.btnHome).setOnClickListener(v -> {
            chooseActivityToSwitchTo(activity, HomePage.class);
        });
        activity.findViewById(R.id.btnActivities).setOnClickListener(v -> {
            chooseActivityToSwitchTo(activity, ActivityPage.class);
        });
        activity.findViewById(R.id.btnPetting).setOnClickListener(v -> {
            chooseActivityToSwitchTo(activity, PettingPage.class);
        });
        activity.findViewById(R.id.btnTournament).setOnClickListener(v -> {
            chooseActivityToSwitchTo(activity, TournamentPage.class);
        });
        activity.findViewById(R.id.btnSettings).setOnClickListener(v -> {
            chooseActivityToSwitchTo(activity, SettingsPage.class);
        });
    }

    public static void chooseActivityToSwitchTo(AppCompatActivity activity, Class desiredClass) {
        String className = desiredClass.getName();
        boolean tutorialPageExist = DataManager.getInstance().getTutorials().tutorialClassExists(className);

        if (tutorialPageExist) {
            Tutorial tutorial = DataManager.getInstance().getTutorials().getTutorial(className);

            if (tutorial != null) {
                //If they haven't seen the tutorial show them that first, passing what class to switch to next time
                if (!tutorial.hasPlayerSeen()) {
                    switchActivities(activity, desiredClass, tutorial);
                    return;
                }
            }
        }
        //If no tutorial was needed, load the class as normal
        switchActivities(activity, desiredClass, null);
    }

    private static void switchActivities(AppCompatActivity activity, Class desiredClass, Tutorial tutorial) {
        Intent intent;

        //If there needs to be an extra, add it
        if (tutorial != null) {
            intent = new Intent(activity, TutorialPage.class);

            intent.putExtra("SIMPLE_CLASS_NAME", desiredClass.getSimpleName());
            intent.putExtra("CLASS_NAME", desiredClass.getName());
            intent.putExtra("LAYOUT_ID", tutorial.getLayoutId());
            intent.putExtra("VIEW_STUB_ID", tutorial.getViewStubId());
        } else {
            intent = new Intent(activity, desiredClass);
        }

        MediaManager.getInstance().switchedScreens = true;

        activity.startActivity(intent);
        //Stop the animation of switching between activities
        activity.overridePendingTransition(0, 0);
    }

    public static boolean canAffordUpgradeWithHearts(double upgradeCost) {
        double currentHearts = DataManager.getInstance().getPlayerData().getCurrentHearts();

        if (currentHearts < upgradeCost)
            return false;

        DataManager.getInstance().addHearts(-upgradeCost);
        return true;
    }

    public static boolean canAffordHeartTokens(double tokens) {
        double currentHeartTokens = DataManager.getInstance().getPlayerData().getCurrentHeartTokens();

        if (currentHeartTokens < tokens)
            return false;

        DataManager.getInstance().addHeartTokens(-tokens);
        return true;
    }

}
