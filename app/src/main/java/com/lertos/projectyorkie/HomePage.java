package com.lertos.projectyorkie;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lertos.projectyorkie.adapters.BindDataToView;
import com.lertos.projectyorkie.adapters.PackViewAdapter;
import com.lertos.projectyorkie.adapters.TalentsViewAdapter;
import com.lertos.projectyorkie.data.Data;

import java.util.List;

public class HomePage extends AppCompatActivity {

    //TODO: Make a class to hold all of this player data
    private int highlightColor = Color.YELLOW;
    private int currentHearts = 450;
    private int currentHeartTokens = 14;
    private int dogsCollected = 12;
    private int maxDogsToCollect = 27;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        //Since this is the main/launcher activity, load the data here
        loadMainData();

        setupBottomButtonBar();
        setupPageButtonBar();
        setupCharacterInfo();
    }

    private void loadMainData() {
        Data.getInstance().start();

        createNewRecyclerView(
                findViewById(R.id.recyclerViewTalents),
                Data.getInstance().getTalents(),
                new TalentsViewAdapter()
        );

        createNewRecyclerView(
                findViewById(R.id.recyclerViewPack),
                Data.getInstance().getPackDogs(),
                new PackViewAdapter()
        );
    }

    private <T extends BindDataToView> void createNewRecyclerView(RecyclerView recyclerView, List<?> arrayList, T viewAdapter) {
        viewAdapter.setDataList(arrayList);
        recyclerView.setAdapter((RecyclerView.Adapter) viewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void switchActivities(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        //Stop the animation of switching between activities
        overridePendingTransition(0, 0);
    }

    public void setupPageButtonBar() {
        findViewById(R.id.button_talents).setOnClickListener(v -> {
            findViewById(R.id.recyclerViewTalents).setVisibility(View.VISIBLE);
            findViewById(R.id.recyclerViewPack).setVisibility(View.GONE);
        });

        findViewById(R.id.button_pack).setOnClickListener(v -> {
            findViewById(R.id.recyclerViewTalents).setVisibility(View.GONE);
            findViewById(R.id.recyclerViewPack).setVisibility(View.VISIBLE);
        });
    }

    public void setupBottomButtonBar() {
        findViewById(R.id.button_home).setOnClickListener(v -> { switchActivities(HomePage.class); });
        findViewById(R.id.button_activities).setOnClickListener(v -> { switchActivities(ActivityPage.class); });
    }

    private void setupCharacterInfo() {
        TextView currentHearts = findViewById(R.id.tcCurrentHearts);
        TextView currentHeartTokens = findViewById(R.id.tcCurrentHeartTokens);
        TextView currentDogsCollected = findViewById(R.id.tcCurrentDogsCollected);

        currentHearts.setText(
                createSpannable(
                        getResources().getString(R.string.character_heart_amount),
                        " " + String.valueOf(this.currentHearts),
                        highlightColor
                ),
                TextView.BufferType.SPANNABLE);

        currentHeartTokens.setText(
                createSpannable(
                        getResources().getString(R.string.character_heart_token_amount),
                        " " + String.valueOf(this.currentHeartTokens),
                        highlightColor
                ),
                TextView.BufferType.SPANNABLE);

        currentDogsCollected.setText(
                createSpannable(
                        getResources().getString(R.string.character_dogs_collected),
                        " " + String.valueOf(this.dogsCollected) + " / " + String.valueOf(this.maxDogsToCollect),
                        highlightColor
                ),
                TextView.BufferType.SPANNABLE);
    }

    private SpannableStringBuilder createSpannable(String str, String appended, int color) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(str + appended);
        spannable.setSpan(new ForegroundColorSpan(color), str.length(), str.length() + appended.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        return spannable;
    }

}
