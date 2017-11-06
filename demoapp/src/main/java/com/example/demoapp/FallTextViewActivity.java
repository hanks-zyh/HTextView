package com.example.demoapp;

import android.os.Bundle;
import android.widget.SeekBar;

import com.hanks.htextview.base.HTextView;

public class FallTextViewActivity extends BaseActivity {

    private HTextView textView, textView2, textView3, textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall_text_view);
        textView = (HTextView) findViewById(R.id.textview);
        textView1 = (HTextView) findViewById(R.id.textview1);
        textView2 = (HTextView) findViewById(R.id.textview2);
        textView3 = (HTextView) findViewById(R.id.textview3);

        textView.setOnClickListener(new ClickListener());
        textView1.setOnClickListener(new ClickListener());
        textView2.setOnClickListener(new ClickListener());
        textView3.setOnClickListener(new ClickListener());

        textView.setAnimationListener(new SimpleAnimationListener(this));
        textView1.setAnimationListener(new SimpleAnimationListener(this));
        textView2.setAnimationListener(new SimpleAnimationListener(this));
        textView3.setAnimationListener(new SimpleAnimationListener(this));

        textView3.animateText(getString(R.string.initStr));

        ((SeekBar) findViewById(R.id.seekbar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setProgress(progress / 100f);
                textView1.setProgress(progress / 100f);
                textView2.setProgress(progress / 100f);
                textView3.setProgress(progress / 100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
