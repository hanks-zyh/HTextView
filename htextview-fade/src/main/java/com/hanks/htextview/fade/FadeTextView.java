package com.hanks.htextview.fade;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.hanks.htextview.base.HTextView;


/**
 * Fade Effect
 * Created by hanks on 2017/3/14.
 */

public class FadeTextView extends HTextView {
    private FadeText fadeText;

    public FadeTextView(Context context) {
        this(context, null);
    }

    public FadeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FadeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        fadeText = new FadeText();
        fadeText.init(this, attrs, defStyleAttr);
    }

    public int getAnimationDuration() {
        return fadeText.getAnimationDuration();
    }

    public void setAnimationDuration(int animationDuration) {
        fadeText.setAnimationDuration(animationDuration);
    }

    @Override
    public void setProgress(float progress) {
        fadeText.setProgress(progress);
    }

    @Override
    public void animateText(CharSequence text) {
        fadeText.animateText(text);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        fadeText.onDraw(canvas);
    }
}
