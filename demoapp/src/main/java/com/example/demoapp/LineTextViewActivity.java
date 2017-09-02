package com.example.demoapp;

import android.os.Bundle;
import android.widget.SeekBar;

import com.hanks.htextview.line.LineTextView;

public class LineTextViewActivity extends BaseActivity {

    private LineTextView hTextView, hTextView2, hTextView3, hTextView4;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_text_view);

        hTextView = (LineTextView) findViewById(R.id.textview);
        hTextView.setOnClickListener(new ClickListener());
        hTextView.setAnimationListener(new SimpleAnimationListener(this));

        hTextView2 = (LineTextView) findViewById(R.id.textview2);
        hTextView2.setOnClickListener(new ClickListener());
        hTextView2.setAnimationListener(new SimpleAnimationListener(this));

        hTextView3 = (LineTextView) findViewById(R.id.textview3);
        hTextView3.setOnClickListener(new ClickListener());
        hTextView3.setAnimationListener(new SimpleAnimationListener(this));

        hTextView4 = (LineTextView) findViewById(R.id.textview4);
        hTextView4.setOnClickListener(new ClickListener());
        hTextView4.setAnimationListener(new SimpleAnimationListener(this));

        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hTextView.setProgress(progress / 100f);
                hTextView2.setProgress(progress / 100f);
                hTextView3.setProgress(progress / 100f);
                hTextView4.setProgress(progress / 100f);
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
