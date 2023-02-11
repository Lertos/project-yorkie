package com.lertos.projectyorkie;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class PettingPage extends AppCompatActivity {

    private Rect pettingLayout = new Rect();
    private ImageButton focusButton;
    private int xStart, yStart, xEnd, yEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_petting);

        focusButton = findViewById(R.id.btnPettingFocus);

        LinearLayout layout = (LinearLayout)findViewById(R.id.linPettingMainSection);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                layout.getGlobalVisibleRect(pettingLayout);

                int buttonHeight = focusButton.getMaxHeight();
                int layoutMargin = 60;

                xStart = pettingLayout.left;
                yStart = pettingLayout.top  - layoutMargin;
                xEnd = pettingLayout.right - buttonHeight;
                yEnd = pettingLayout.bottom  - layoutMargin - buttonHeight;
            }
        });

        Helper.setupBottomButtonBar(this);
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        ((Button) findViewById(R.id.btnStartPetting)).setOnClickListener( v -> {
            Random rng = new Random();
            int xPos = rng.nextInt(xEnd - xStart) + xStart;
            int yPos = rng.nextInt(yEnd - yStart) + yStart;

            //TODO: This needs to be so the start button begins the round and each click randomly moves the button
            focusButton.setX(xPos);
            focusButton.setY(yPos);
            focusButton.setVisibility(View.VISIBLE);
        });
    }

}

