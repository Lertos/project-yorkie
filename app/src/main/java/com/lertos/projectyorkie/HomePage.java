package com.lertos.projectyorkie;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lertos.projectyorkie.data.Talent;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    private ArrayList<Talent> talentList;
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
        setupBottomButtonBar();

        setupCharacterInfo();
        createAndLoadTalents();
    }

    private void switchActivities(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        //Stop the animation of switching between activities
        overridePendingTransition(0, 0);
    }

    public void setupBottomButtonBar() {
        findViewById(R.id.button_home).setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Switching to home page", Toast.LENGTH_SHORT).show();
            switchActivities(HomePage.class);
        });

        findViewById(R.id.button_activities).setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Switching to activities page", Toast.LENGTH_SHORT).show();
            switchActivities(ActivityPage.class);
        });
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

    private void createAndLoadTalents() {
        RecyclerView talentsRecyclerView = findViewById(R.id.recyclerViewTalents);
        TalentsViewAdapter talentsViewAdapter = new TalentsViewAdapter(this);

        talentList = new ArrayList<>();
        talentList.add(new Talent("Heart Beater", "Increases hearts per second.", 1, 10, 2));
        talentList.add(new Talent("Lucky Streak", "Increases chance at gaining heart tokens.", 1, 2, 1));
        talentList.add(new Talent("Bargain Master", "Decreases cost of upgrading talents.", 1, 16, 4));

        talentsViewAdapter.setTalentList(talentList);
        talentsRecyclerView.setAdapter(talentsViewAdapter);
        talentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
