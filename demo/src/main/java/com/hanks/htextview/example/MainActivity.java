package com.hanks.htextview.example;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

public class MainActivity extends AppCompatActivity implements ViewSwitcher.ViewFactory {

    String[] sentences = new String[]{"What is design?", "Design", "Design is not just", "what it looks like", "and feels like.", "Design", "is how it works.", "- Steve Jobs", "Older people", "sit down and ask,", "'What is it?'", "but the boy asks,", "'What can I do with it?'.", "- Steve Jobs", "Swift", "Objective-C", "iPhone", "iPad", "Mac Mini", "MacBook Pro", "Mac Pro", "爱老婆", "老婆和女儿"};
    private int mCounter = 10;
    private TextSwitcher textSwitcher;
    private HTextView    hTextView;

    private SeekBar    seekBar;
    private RadioGroup radioGroup;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textSwitcher = (TextSwitcher) findViewById(R.id.text);
        textSwitcher.setFactory(this);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        textSwitcher.setInAnimation(in);
        textSwitcher.setOutAnimation(out);

        hTextView = (HTextView) findViewById(R.id.text2);

        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setMax(20);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8 + progress);
                hTextView.reset(hTextView.getText());
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setProgress(10);

        radioGroup = (RadioGroup) findViewById(R.id.typeGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.scale:
                        hTextView.setTextColor(Color.BLACK);
                        hTextView.setBackgroundColor(Color.WHITE);
                        hTextView.setAnimateType(HTextViewType.SCALE);
                        break;
                    case R.id.evaporate:
                        hTextView.setTextColor(Color.BLACK);
                        hTextView.setBackgroundColor(Color.WHITE);
                        hTextView.setAnimateType(HTextViewType.EVAPORATE);
                        break;
                    case R.id.fall:
                        hTextView.setTextColor(Color.BLACK);
                        hTextView.setBackgroundColor(Color.WHITE);
                        hTextView.setAnimateType(HTextViewType.FALL);
                        break;
                    case R.id.pixelate:
                        hTextView.setTextColor(Color.BLACK);
                        hTextView.setBackgroundColor(Color.WHITE);
                        hTextView.setAnimateType(HTextViewType.PIXELATE);
                        break;
                    case R.id.sparkle:
                        hTextView.setTextColor(Color.WHITE);
                        hTextView.setBackgroundColor(Color.BLACK);
                        hTextView.setAnimateType(HTextViewType.SPARKLE);
                        break;
                    case R.id.anvil:
                        hTextView.setTextColor(Color.WHITE);
                        hTextView.setBackgroundColor(Color.BLACK);
                        hTextView.setAnimateType(HTextViewType.ANVIL);
                        break;
                    case R.id.line:
                        hTextView.setTextColor(Color.WHITE);
                        hTextView.setBackgroundColor(Color.BLACK);
                        hTextView.setAnimateType(HTextViewType.LINE);
                        break;
                    case R.id.typer:
                        hTextView.setTextColor(Color.WHITE);
                        hTextView.setBackgroundColor(Color.BLACK);
                        hTextView.setAnimateType(HTextViewType.TYPER);
                        break;
                    case R.id.rainbow:
                        hTextView.setTextColor(Color.WHITE);
                        hTextView.setBackgroundColor(Color.BLACK);
                        hTextView.setAnimateType(HTextViewType.RAINBOW);
                        break;
                }

                onClick(radioGroup.findViewById(checkedId));
            }
        });
//
//        hTextView.setTextColor(Color.BLACK);
//        hTextView.setBackgroundColor(Color.WHITE);
//        hTextView.setAnimateType(HTextViewType.SCALE);
//        onClick(findViewById(R.id.scale));
    }

    public void onClick(View v) {
        updateCounter();
    }

    private void updateCounter() {
        mCounter = mCounter >= sentences.length - 1 ? 0 : mCounter + 1;
        textSwitcher.setText(sentences[mCounter]);
        hTextView.animateText(sentences[mCounter]);
    }

    @Override public View makeView() {

        TextView t = new TextView(this);
        t.setGravity(Gravity.CENTER);
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        return t;
    }
}
