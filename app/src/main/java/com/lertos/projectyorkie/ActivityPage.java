package com.lertos.projectyorkie;

import android.os.Bundle;

public class ActivityPage extends HomePage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_page);
        super.setupBottomButtonBar();
    }
}
