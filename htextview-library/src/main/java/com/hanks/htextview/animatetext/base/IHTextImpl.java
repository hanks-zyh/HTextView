package com.hanks.htextview.animatetext.base;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.util.CharacterDiffResult;
import com.hanks.htextview.util.CharacterUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * base class
 * Created by hanks on 15-12-19.
 */
public abstract class IHTextImpl implements IHText {

    protected Paint mPaint, mOldPaint;

    //字符间距
    protected float[] gaps = new float[100];
    protected float[] oldGaps = new float[100];

    //当前字体大小
    protected float mTextSize;

    protected CharSequence mText;
    protected CharSequence mOldText;

    protected List<CharacterDiffResult> differentList = new ArrayList<>();

    // 原来的字符串开始画的x位置
    protected float oldStartX = 0;
    // 新的字符串开始画的x位置
    protected float startX = 0;
    // 字符串开始画的y, baseline
    protected float startY = 0;

    protected HTextView mHTextView;


    @Override
    public void init(HTextView hTextView, AttributeSet attrs, int defStyle) {

        mHTextView = hTextView;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mHTextView.getCurrentTextColor());
        mPaint.setStyle(Paint.Style.FILL);

        mOldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOldPaint.setColor(mHTextView.getCurrentTextColor());
        mOldPaint.setStyle(Paint.Style.FILL);

        mText = mHTextView.getText();
        mOldText = mHTextView.getText();

        mTextSize = mHTextView.getTextSize();

        initVariables();
        mHTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                prepareAnimate();
            }
        }, 50);

    }

    @Override
    public void animateText(CharSequence text) {
        mHTextView.setText(text);
        mOldText = mText;
        mText = text;
        prepareAnimate();
        animatePrepare();
        animateStart();
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawFrame(canvas);
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
        animatePrepare();
        mHTextView.invalidate();
    }

    /**
     * 具体实现动画
     */
    protected abstract void animateStart();

    /**
     * 动画每次刷新界面时调用
     *
     * @param canvas 画布
     */
    protected void drawFrame(Canvas canvas) {

    }



    /**
     * 类被实例化时初始化
     */
    protected void initVariables() {

    }

    /**
     * 每次动画前初始化调用
     */
    protected void animatePrepare() {

    }


}
