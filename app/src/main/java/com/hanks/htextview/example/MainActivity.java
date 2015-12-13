package com.hanks.htextview.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.hanks.htextview.HTextView;

public class MainActivity extends AppCompatActivity implements ViewSwitcher.ViewFactory {

    String[] sentences = new String[]{"Design", "Design is not just.", "what it looks like", "and feels like,", "- Steve Jobs", "小乖乖", "小哎呀吃屁"};
    private int mCounter = 10;
    private TextSwitcher textSwitcher;
    private HTextView    text2;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textSwitcher = (TextSwitcher) findViewById(R.id.text);
        textSwitcher.setFactory(this);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        textSwitcher.setInAnimation(in);
        textSwitcher.setOutAnimation(out);

        text2 = (HTextView) findViewById(R.id.text2);

    }

    public void onClick(View v) {
        updateCounter();
    }

    private void updateCounter() {
        mCounter = mCounter >= sentences.length - 1 ? 0 : mCounter + 1;
        textSwitcher.setText(sentences[mCounter]);
        text2.animateText(sentences[mCounter]);
    }

    @Override public View makeView() {

        TextView t = new TextView(this);
        t.setGravity(Gravity.CENTER);
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        return t;
    }
}
