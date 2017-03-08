package com.hanks.htextview.animatetext;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.util.CharacterUtils;

import java.util.ArrayList;
import java.util.List;
/**
 * base class
 * Created by hanks on 15-12-19.
 */
public abstract class HText implements IHText {

    protected Paint mPaint, mOldPaint;

    /**
     * the gap between characters
     */
    protected float[] gaps    = new float[100];
    protected float[] oldGaps = new float[100];

    /**
     * current text size
     */
    protected float mTextSize;

    protected CharSequence mText;
    protected CharSequence mOldText;

    protected List<CharacterDiffResult> differentList = new ArrayList<>();

    protected float oldStartX = 0;
    protected float startX    = 0;
    protected float startY    = 0;

    protected HTextView mHTextView;


    @Override public void init(HTextView hTextView, AttributeSet attrs, int defStyle) {

        mHTextView = hTextView;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mHTextView.getCurrentTextColor());
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTypeface(mHTextView.getTypeface());

        mOldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOldPaint.setColor(mHTextView.getCurrentTextColor());
        mOldPaint.setStyle(Paint.Style.FILL);
        mOldPaint.setTypeface(mHTextView.getTypeface());

        mText = mHTextView.getText();
        mOldText = mHTextView.getText();

        mTextSize = mHTextView.getTextSize();

        initVariables();
        mHTextView.postDelayed(new Runnable() {
            @Override public void run() {
                prepareAnimate();
            }
        },50);

    }

    @Override public void animateText(CharSequence text) {
        mHTextView.setText(text);
        mOldText = mText;
        mText = text;
        prepareAnimate();
        animatePrepare(text);
        animateStart(text);
    }

    @Override public void onDraw(Canvas canvas) {
        mPaint.setColor(mHTextView.getCurrentTextColor());
        mOldPaint.setColor(mHTextView.getCurrentTextColor());
        drawFrame(canvas);
    }

    public void setTextColor(int color){
        mHTextView.setTextColor(color);
    }
    
    private void prepareAnimate() {
        mTextSize = mHTextView.getTextSize();
        mPaint.setTextSize(mTextSize);
        for (int i = 0; i < mText.length(); i++) {
            gaps[i] = mPaint.measureText(mText.charAt(i) + "");
        }

        mOldPaint.setTextSize(mTextSize);
        for (int i = 0; i < mOldText.length(); i++) {
            oldGaps[i] = mOldPaint.measureText(mOldText.charAt(i) + "");
        }

        oldStartX = (mHTextView.getMeasuredWidth() - mHTextView.getCompoundPaddingLeft() - mHTextView.getPaddingLeft() - mOldPaint
                .measureText(mOldText.toString())) / 2f;
        startX = (mHTextView.getMeasuredWidth() - mHTextView.getCompoundPaddingLeft() - mHTextView.getPaddingLeft() - mPaint
                .measureText(mText.toString())) / 2f;
        startY = mHTextView.getBaseline();

        differentList.clear();
        differentList.addAll(CharacterUtils.diff(mOldText, mText));
    }

    public void reset(CharSequence text) {
        animatePrepare(text);
        mHTextView.invalidate();
    }

    protected abstract void initVariables();
    protected abstract void animateStart(CharSequence text);
    protected abstract void animatePrepare(CharSequence text);
    protected abstract void drawFrame(Canvas canvas);



}
