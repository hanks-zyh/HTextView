package com.hanks.htextview.fade;

import android.animation.ValueAnimator;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.hanks.htextview.base.HText;
import com.hanks.htextview.base.HTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * fade effect
 * Created by hanks on 2017/3/14.
 */

public class FadeText extends HText {

    private Random random;
    private int animationDuration;
    private List<Integer> alphaList;
    private int DEFAULT_DURATION = 2000;

    @Override
    public void init(HTextView hTextView, AttributeSet attrs, int defStyle) {
        super.init(hTextView, attrs, defStyle);
        TypedArray typedArray = hTextView.getContext().obtainStyledAttributes(attrs, R.styleable.FadeTextView);
        animationDuration = typedArray.getInt(R.styleable.FadeTextView_animationDuration, DEFAULT_DURATION);
        typedArray.recycle();
    }

    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }

    public int getAnimationDuration() {
        return animationDuration;
    }

    @Override
    protected void initVariables() {
        random = new Random();
        if (alphaList == null) {
            alphaList = new ArrayList<>();
        }
        // generate random alpha
        alphaList.clear();
        for (int i = 0; i < mHTextView.getText().length(); i++) {
            int randomNumber = random.nextInt(2);// 0 or 1
            if ((i + 1) % (randomNumber + 2) == 0) { // 2 or 3
                if ((i + 1) % (randomNumber + 4) == 0) { // 4 or 5
                    alphaList.add(55);
                } else {
                    alphaList.add(255);
                }
            } else {
                if ((i + 1) % (randomNumber + 4) == 0) { // 4 or 5
                    alphaList.add(55);
                } else {
                    alphaList.add(0);
                }
            }
        }
    }


    @Override
    protected void animateStart(CharSequence text) {
        initVariables();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1)
                .setDuration((long) animationDuration);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                mHTextView.invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void animatePrepare(CharSequence text) {

    }

    @Override
    protected void drawFrame(Canvas canvas) {
        Layout layout = mHTextView.getLayout();
        int gapIndex = 0;
        for (int i = 0; i < layout.getLineCount(); i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            float lineLeft = layout.getLineLeft(i);
            float lineBaseline = layout.getLineBaseline(i);
            String lineText = mText.subSequence(lineStart, lineEnd).toString();
            for (int c = 0; c < lineText.length(); c++) {
                int alpha = alphaList.get(gapIndex);
                mPaint.setAlpha((int) ((255 - alpha) * progress + alpha));
                canvas.drawText(String.valueOf(lineText.charAt(c)), lineLeft, lineBaseline, mPaint);
                lineLeft += gapList.get(gapIndex++);
            }
        }
    }
}
