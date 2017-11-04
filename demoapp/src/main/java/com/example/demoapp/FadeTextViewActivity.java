package com.example.demoapp;

import android.os.Bundle;
import android.widget.SeekBar;

import com.hanks.htextview.fade.FadeTextView;

public class FadeTextViewActivity extends BaseActivity {

    private FadeTextView textView,textview2;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fade_text_view);

        textView = (FadeTextView) findViewById(R.id.textview);
        textView.setOnClickListener(new ClickListener());
        textView.setAnimationListener(new SimpleAnimationListener(this));
        textview2 = (FadeTextView) findViewById(R.id.textview2);
        textview2.setOnClickListener(new ClickListener());
        textview2.setAnimationListener(new SimpleAnimationListener(this));

        textView.animateText(getString(R.string.initStr));

        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setProgress(progress / 100f);
                textview2.setProgress(progress / 100f);
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
