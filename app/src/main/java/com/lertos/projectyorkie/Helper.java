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

import java.util.Formatter;
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
        activity.findViewById(R.id.button_home).setOnClickListener(v -> {
            switchActivities(activity, HomePage.class);
        });
        activity.findViewById(R.id.button_activities).setOnClickListener(v -> {
            switchActivities(activity, ActivityPage.class);
        });
        activity.findViewById(R.id.button_petting).setOnClickListener(v -> {
            switchActivities(activity, PettingPage.class);
        });
        activity.findViewById(R.id.button_settings).setOnClickListener(v -> {
            switchActivities(activity, SettingsPage.class);
        });
    }

    private static void switchActivities(AppCompatActivity activity, Class cls) {
        Intent intent = new Intent(activity, cls);
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
