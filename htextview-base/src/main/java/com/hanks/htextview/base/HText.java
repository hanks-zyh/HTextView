package com.hanks.htextview.base;

import android.graphics.Canvas;
import android.graphics.Paint;
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
    protected float mTextSize;
    protected float oldStartX = 0;

    public void setProgress(float progress) {
        this.progress = progress;
        mHTextView.invalidate();
    }

    @Override
    public void init(HTextView hTextView, AttributeSet attrs, int defStyle) {
        mHTextView = hTextView;
        mOldText = "";
        mText = hTextView.getText();
        progress = 1;

        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mOldPaint = new TextPaint(mPaint);

        mHTextView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mHTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mHTextView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                mTextSize = mHTextView.getTextSize();
                mWidth = mHTextView.getWidth();
                mHeight = mHTextView.getHeight();
                oldStartX = mHTextView.getLayout().getLineLeft(0);
                initVariables();
            }
        });
        prepareAnimate();
    }

    private void prepareAnimate() {
        mTextSize = mHTextView.getTextSize();
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mHTextView.getCurrentTextColor());
        mPaint.setTypeface(mHTextView.getTypeface());
        gapList.clear();
        for (int i = 0; i < mText.length(); i++) {
            gapList.add(mPaint.measureText(String.valueOf(mText.charAt(i))));
        }
        mOldPaint.setTextSize(mTextSize);
        mOldPaint.setColor(mHTextView.getCurrentTextColor());
        mOldPaint.setTypeface(mHTextView.getTypeface());
        oldGapList.clear();
        for (int i = 0; i < mOldText.length(); i++) {
            oldGapList.add(mOldPaint.measureText(String.valueOf(mOldText.charAt(i))));
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
