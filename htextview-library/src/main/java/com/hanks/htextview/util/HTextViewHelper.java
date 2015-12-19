package com.hanks.htextview.util;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.util.AttributeSet;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.animatetext.AnimateText;
import com.hanks.htextview.animatetext.CharacterDiffResult;

import java.util.ArrayList;
import java.util.List;
/**
 * HTextViewHelper
 * Created by hanks on 15-12-18.
 */
public abstract class HTextViewHelper implements AnimateText {

    public static final int ANIMATE_TYPE_SCALE     = 0;
    public static final int ANIMATE_TYPE_EVAPORATE = 1;
    public static final int ANIMATE_TYPE_FALL      = 2;
    public static final int ANIMATE_TYPE_SPARKLE   = 3;
    public static final int ANIMATE_TYPE_ANVIL     = 4;
    public static final int ANIMATE_TYPE_LINE      = 5;
    public static final int ANIMATE_TYPE_DEFAULT   = ANIMATE_TYPE_SCALE;

    private HTextView mHTextView;
    private final float ANIMATE_DURATION = 400;
    private final int   mostCount        = 20; // 最多10个字符同时动画
    /**
     * if the value is false , {@link HTextView} can be used as TextView
     * 如果设置为false,HTextView将和TextView一样
     */
    public boolean isAnimate;
    /**
     * 动画进度 [0..1]
     */
    float progress = 0;

    Paint mPaint, mOldPaint;

    private float[] gaps    = new float[100];
    private float[] oldGaps = new float[100];

    private float mTextSize;

    private CharSequence mText;
    private CharSequence mOldText;

    private List<CharacterDiffResult> differentList = new ArrayList<>();

    private float oldStartX = 0; // 原来的字符串开始画的x位置
    private float startX    = 0; // 新的字符串开始画的x位置
    private float startY    = 0; // 字符串开始画的Y, baseline

    private HTextViewHelper mHTextViewHelper;

    /**
     * animate type
     */
    private int mAnimateType;

    /**
     * primaryColor is the value of android:textColor in xml
     */
    private int primaryColor;


    private void init() {
        mPaint = mHTextView.getPaint();
        mOldPaint = new Paint(mPaint);
        mText = "";
        mOldText = "";
        mTextSize = mHTextView.getTextSize();
        initVariables();
    }

    /**
     * here subClass to childInitVariables
     */
    public abstract void initVariables();

    @Override public void init(HTextView hTextView, AttributeSet attrs, int defStyle) {
        init();
    }

    public void animateText(CharSequence text) {
        mOldText = mText;
        mText = text;
        calc();
    }

    private void calc() {
        mTextSize = mHTextView.getTextSize();

        mPaint.setTextSize(mTextSize);
        for (int i = 0; i < mText.length(); i++) {
            gaps[i] = mPaint.measureText(mText.charAt(i) + "");
        }

        mOldPaint.setTextSize(mTextSize);
        for (int i = 0; i < mOldText.length(); i++) {
            oldGaps[i] = mOldPaint.measureText(mOldText.charAt(i) + "");
        }

        oldStartX = (mHTextView.getWidth() - mOldPaint.measureText(mOldText.toString())) / 2f;

        startX = (mHTextView.getWidth() - mPaint.measureText(mText.toString())) / 2f;
        startY = mHTextView.getBaseline();

        differentList.clear();
        differentList.addAll(CharacterUtils.diff(mOldText, mText));
    }

    public void onDraw(Canvas canvas) {
        mHTextViewHelper.onDraw(canvas);
    }

    public void reset(CharSequence text) {
        mHTextViewHelper.reset(text);
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {

    }




    /**
     * set different animate type
     *
     * @param mAnimateType see {@link HTextViewHelper}
     */
    public void setAnimateType(@IntRange(from = 0, to = 5) int mAnimateType) {
        this.mAnimateType = mAnimateType;
    }

    public void setPrimaryColor(@ColorInt int primaryColor) {
        this.primaryColor = primaryColor;
    }

}
