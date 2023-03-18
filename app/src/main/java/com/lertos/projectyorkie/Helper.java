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
        boolean tutorialPageExist = DataManager.getInstance().tutorialClassExists(desiredClass.getName());

        if (tutorialPageExist) {
            boolean hasSeenTutorialPage = DataManager.getInstance().hasSeenTutorial(desiredClass.getName());

            //If they haven't seen the tutorial show them that first, passing what class to switch to next time
            if (!hasSeenTutorialPage) {
                switchActivities(activity, TutorialPage.class, desiredClass.getName());
                return;
            }
        }

        //If no tutorial was needed, load the class as normal
        switchActivities(activity, desiredClass, null);
    }

    private static void switchActivities(AppCompatActivity activity, Class desiredClass, String extraClassName) {
        Intent intent = new Intent(activity, desiredClass);

        //If there needs to be an extra, add it
        if (extraClassName != null)
            intent.putExtra("CLASS_NAME", extraClassName);

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

}
