package com.lertos.projectyorkie;

import android.os.Bundle;
import android.view.ViewStub;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

        //TODO: Also need to set that the page has been viewed in the tutorial (that method would also need to save that
        //to the player data file) once that confirm button is clicked

        setContentView(R.layout.page_tutorial);

        //Set the header
        ((TextView) findViewById(R.id.tvPageName)).setText(simpleClassName);

        //Inflate the specific layout for the chosen page
        inflateStub();
    }

    private void inflateStub() {
        ViewStub stub = findViewById(viewStubId);

        stub.setLayoutResource(layoutId);

        if (stub != null)
            stub.inflate();
    }
}
