package com.example.demoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import com.hanks.htextview.line.LineTextView;

public class LineTextViewActivity extends AppCompatActivity {

    String[] sentences = {"What is design?",
            "Design",
            "Design is not just",
            "what it looks like and feels like.",
            "Design is how it works. \n- Steve Jobs",
            "Older people",
            "sit down and ask,",
            "'What is it?'",
            "but the boy asks,",
            "'What can I do with it?'. \n- Steve Jobs",
            "Swift",
            "Objective-C",
            "iPhone",
            "iPad",
            "Mac Mini", "MacBook Pro", "Mac Pro", "爱老婆", "老婆和女儿"};

    private LineTextView hTextView, hTextView2, hTextView3, hTextView4;
    private int index;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hTextView = (LineTextView) findViewById(R.id.textview);
        hTextView.setOnClickListener(new ClickListener());

        hTextView2 = (LineTextView) findViewById(R.id.textview2);
        hTextView2.setOnClickListener(new ClickListener());

        hTextView3 = (LineTextView) findViewById(R.id.textview3);
        hTextView3.setOnClickListener(new ClickListener());
        hTextView4 = (LineTextView) findViewById(R.id.textview4);
        hTextView4.setOnClickListener(new ClickListener());

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


    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v instanceof LineTextView) {
                if (index + 1 >= sentences.length) {
                    index = 0;
                }
                ((LineTextView) v).animateText(sentences[index++]);
            }
        }
    }


}
