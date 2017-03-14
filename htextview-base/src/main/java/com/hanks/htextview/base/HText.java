package com.hanks.htextview.base;

import android.graphics.Canvas;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * abstract class
 * Created by hanks on 15-12-19.
 */
public abstract class HText implements IHText {

    protected int mHeight, mWidth;
    protected CharSequence mText, mOldText;
    protected TextPaint mPaint, mOldPaint;
    protected HTextView mHTextView;
    protected List<Float> gapList = new ArrayList<>();
    protected List<Float> oldGapList = new ArrayList<>();
    protected float progress; // 0~1

    public void setProgress(float progress) {
        this.progress = progress;
        mHTextView.invalidate();
    }

    @Override
    public void init(HTextView hTextView, AttributeSet attrs, int defStyle) {
        mHTextView = hTextView;
        mText = mOldText = hTextView.getText();

        mPaint = hTextView.getPaint();
        mPaint.setColor(hTextView.getCurrentTextColor());
        mOldPaint = new TextPaint(mPaint);

        initVariables();

        mHTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mHTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mHTextView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                mWidth = mHTextView.getWidth();
                mHeight = mHTextView.getHeight();
            }
        });
        mHTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                prepareAnimate();
            }
        }, 50);
    }


    private void prepareAnimate() {
        CharSequence text = mHTextView.getText();
        float textSize = mHTextView.getTextSize();
        mPaint.setTextSize(textSize);
        mPaint.setColor(mHTextView.getCurrentTextColor());

        gapList.clear();
        for (int i = 0; i < text.length(); i++) {
            gapList.add(mPaint.measureText(String.valueOf(text.charAt(i))));
        }

        mOldPaint.setTextSize(textSize);
        mOldPaint.setColor(mHTextView.getCurrentTextColor());
        oldGapList.clear();
        for (int i = 0; i < text.length(); i++) {
            oldGapList.add(mOldPaint.measureText(String.valueOf(text.charAt(i))));
        }
    }


    @Override
    public void animateText(CharSequence text) {
        mHTextView.setText(text);
        mOldText = mText;
        mText = text;
        prepareAnimate();
        animatePrepare(text);
        animateStart(text);
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawFrame(canvas);
    }

    protected abstract void initVariables();

    protected abstract void animateStart(CharSequence text);

    protected abstract void animatePrepare(CharSequence text);

    protected abstract void drawFrame(Canvas canvas);
}
