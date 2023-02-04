package com.lertos.projectyorkie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    private ArrayList<String> talentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        setupBottomButtonBar();

        RecyclerView talentsRecyclerView = findViewById(R.id.recyclerViewTalents);
        TalentsViewAdapter talentsViewAdapter = new TalentsViewAdapter(this);

        talentList = new ArrayList<>();
        talentList.add("Talent 1");
        talentList.add("Talent 2");

        talentsViewAdapter.setTalentList(talentList);
        talentsRecyclerView.setAdapter(talentsViewAdapter);
        talentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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

}
