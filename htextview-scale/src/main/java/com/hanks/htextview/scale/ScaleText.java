package com.hanks.htextview.scale;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.hanks.htextview.base.CharacterDiffResult;
import com.hanks.htextview.base.CharacterUtils;
import com.hanks.htextview.base.HText;
import com.hanks.htextview.base.HTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Scale
 * Created by hanks on 2017/3/15.
 */

public class ScaleText extends HText {

    private List<CharacterDiffResult> differentList = new ArrayList<>();
    private float oldStartX = 0;
    float mostCount = 20;
    float charTime = 400;
    private long duration;
    private float progress;
    private ValueAnimator animator;

    @Override
    public void init(HTextView hTextView, AttributeSet attrs, int defStyle) {
        super.init(hTextView, attrs, defStyle);
        animatePrepare(hTextView.getText());
        animator = new ValueAnimator();
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                mHTextView.invalidate();
            }
        });
    }


    @Override
    public void animateText(CharSequence text) {
        oldStartX = mHTextView.getLayout().getLineLeft(0);
        super.animateText(text);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void animateStart(CharSequence text) {
        int n = mText.length();
        n = n <= 0 ? 1 : n;
        duration = (long) (charTime + charTime / mostCount * (n - 1));
        animator.cancel();
        animator.setFloatValues(0, duration);
        animator.setDuration(duration);
        animator.start();
    }

    @Override
    protected void animatePrepare(CharSequence text) {
        differentList.clear();
        differentList.addAll(CharacterUtils.diff(mOldText, mText));
    }

    @Override
    public void drawFrame(Canvas canvas) {
        float startX = mHTextView.getLayout().getLineLeft(0);
        float startY = mHTextView.getBaseline();
        float textSize = mHTextView.getTextSize();
        float offset = startX;
        float oldOffset = oldStartX;
        int maxLength = Math.max(mText.length(), mOldText.length());
        for (int i = 0; i < maxLength; i++) {
            // draw old text
            if (i < mOldText.length()) {
                float percent = progress / duration;
                int move = CharacterUtils.needMove(i, differentList);
                if (move != -1) {
                    mOldPaint.setTextSize(textSize);
                    mOldPaint.setAlpha(255);
                    float p = percent * 2f;
                    p = p > 1 ? 1 : p;
                    float distX = CharacterUtils.getOffset(i, move, p, startX, oldStartX, gapList, oldGapList);
                    canvas.drawText(mOldText.charAt(i) + "", 0, 1, distX, startY, mOldPaint);
                } else {
                    mOldPaint.setAlpha((int) ((1 - percent) * 255));
                    mOldPaint.setTextSize(textSize * (1 - percent));
                    float width = mOldPaint.measureText(mOldText.charAt(i) + "");
                    canvas.drawText(mOldText.charAt(i) + "", 0, 1, oldOffset + (oldGapList.get(i) - width) / 2, startY, mOldPaint);
                }
                oldOffset += oldGapList.get(i);
            }

            // draw new text
            if (i < mText.length()) {
                if (!CharacterUtils.stayHere(i, differentList)) {
                    int alpha = (int) (255f / charTime * (progress - charTime * i / mostCount));
                    if (alpha > 255) alpha = 255;
                    if (alpha < 0) alpha = 0;

                    float size = textSize * 1f / charTime * (progress - charTime * i / mostCount);
                    if (size > textSize) size = textSize;
                    if (size < 0) size = 0;

                    mPaint.setAlpha(alpha);
                    mPaint.setTextSize(size);

                    float width = mPaint.measureText(mText.charAt(i) + "");
                    canvas.drawText(mText.charAt(i) + "", 0, 1, offset + (gapList.get(i) - width) / 2, startY, mPaint);
                }
                offset += gapList.get(i);
            }
        }
    }

}
