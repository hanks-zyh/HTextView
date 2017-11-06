package com.example.demoapp;

import android.os.Bundle;

import com.hanks.htextview.rainbow.RainbowTextView;

public class RainbowTextViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rainbow_text_view);
        RainbowTextView textView = (RainbowTextView) findViewById(R.id.textview);
        textView.setOnClickListener(new ClickListener());
        findViewById(R.id.textview2).setOnClickListener(new ClickListener());

        textView.animateText(getString(R.string.initStr));

    }
}
