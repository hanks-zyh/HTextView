package com.example.demoapp;

import android.os.Bundle;

import com.hanks.htextview.base.HTextView;

public class TyperTextViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typer_text_view);
        final HTextView textView1 = (HTextView) findViewById(R.id.textview);
        textView1.setOnClickListener(new ClickListener());
        textView1.setAnimationListener(new SimpleAnimationListener(this));

        final HTextView textView2 = (HTextView) findViewById(R.id.textview2);
        textView2.setOnClickListener(new ClickListener());
        textView2.setAnimationListener(new SimpleAnimationListener(this));
    }
}
