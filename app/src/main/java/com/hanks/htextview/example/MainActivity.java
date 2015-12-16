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

import com.hanks.htextview.AnvilText;
import com.hanks.htextview.BurnText;
import com.hanks.htextview.EvaporateText;
import com.hanks.htextview.FallText;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.PixelateText;
import com.hanks.htextview.ScaleText;
import com.hanks.htextview.SparkleText;

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
                        hTextView.setBackgroundColor(Color.WHITE);
                        hTextView.setAnimateType(new ScaleText());
                        break;
                    case R.id.evaporate:
                        hTextView.setBackgroundColor(Color.WHITE);
                        hTextView.setAnimateType(new EvaporateText());
                        break;
                    case R.id.fall:
                        hTextView.setBackgroundColor(Color.WHITE);
                        hTextView.setAnimateType(new FallText());
                        break;
                    case R.id.pixelate:
                        hTextView.setBackgroundColor(Color.WHITE);
                        hTextView.setAnimateType(new PixelateText());
                        break;
                    case R.id.sparkle:
                        hTextView.setBackgroundColor(Color.BLACK);
                        hTextView.setAnimateType(new SparkleText());
                        break;
                    case R.id.burn:
                        hTextView.setBackgroundColor(Color.BLACK);
                        hTextView.setAnimateType(new BurnText());
                        break;
                    case R.id.anvil:
                        hTextView.setBackgroundColor(Color.BLACK);
                        hTextView.setAnimateType(new AnvilText());
                        break;
                }
            }
        });

        radioGroup.check(R.id.sparkle);

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
