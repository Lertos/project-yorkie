package com.lertos.projectyorkie;

import android.os.Bundle;
import android.view.ViewStub;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lertos.projectyorkie.data.DataManager;

public class TutorialPage extends AppCompatActivity {

    private String simpleClassName;
    private String className;
    private int layoutId;
    private int viewStubId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simpleClassName = getIntent().getStringExtra("SIMPLE_CLASS_NAME");
        className = getIntent().getStringExtra("CLASS_NAME");
        layoutId = getIntent().getIntExtra("LAYOUT_ID", -1);
        viewStubId = getIntent().getIntExtra("VIEW_STUB_ID", -1);

        if (layoutId == -1 || viewStubId == -1)
            super.finish();

        setContentView(R.layout.page_tutorial);

        //Set the header
        ((TextView) findViewById(R.id.tvPageName)).setText(simpleClassName);

        setOnClickListener();

        //Inflate the specific layout for the chosen page
        inflateStub();
    }

    private void setOnClickListener() {
        //Set the confirm button to bring the player to the normal screen they requested
        findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            try {
                Class<?> activity = Class.forName(className);

                DataManager.getInstance().getTutorials().setTutorialAsSeen(className);

                Helper.chooseActivityToSwitchTo(this, activity);
            } catch (ClassNotFoundException e) {
                Helper.chooseActivityToSwitchTo(this, HomePage.class);
            }
        });
    }

    private void inflateStub() {
        ViewStub stub = findViewById(viewStubId);

        stub.setLayoutResource(layoutId);

        if (stub != null)
            stub.inflate();
    }
}
