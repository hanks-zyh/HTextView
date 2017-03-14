package com.example.demoapp;

import android.os.Bundle;

public class RainbowTextViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rainbow_text_view);
        findViewById(R.id.textview).setOnClickListener(new ClickListener());
        findViewById(R.id.textview2).setOnClickListener(new ClickListener());
    }
}
