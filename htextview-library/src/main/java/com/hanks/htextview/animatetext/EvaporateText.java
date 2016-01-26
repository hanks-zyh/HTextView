package com.hanks.htextview.animatetext;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.hanks.htextview.animatetext.base.IHTextImpl;
import com.hanks.htextview.util.CharacterUtils;

/**
 * 蒸发效果
 * Created by hanks on 15-12-14.
 */
public class EvaporateText extends IHTextImpl {

    private static final float CHAR_TIME = 300; // 每个字符动画时间 300ms
    private static final int MOST_COUNT = 20; // 最多10个字符同时动画
    private int mTextHeight;
    private float progress;

    @Override
    protected void animateStart() {
        int textLength = mText.length();
        // 计算动画总时间
        long duration = (long) (CHAR_TIME + CHAR_TIME / MOST_COUNT * (textLength - 1));
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, duration).setDuration(duration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
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
    protected void animatePrepare() {
        Rect bounds = new Rect();
        mPaint.getTextBounds(mText.toString(), 0, mText.length(), bounds);
        mTextHeight = bounds.height();
    }

    @Override
    public void onDraw(Canvas canvas) {
        float offset = startX;
        float oldOffset = oldStartX;
        int maxLength = Math.max(mText.length(), mOldText.length());
        for (int i = 0; i < maxLength; i++) {
            // draw old text
            if (i < mOldText.length()) {
                //
                float pp = progress / (CHAR_TIME + CHAR_TIME / MOST_COUNT * (mText.length() - 1));

                mOldPaint.setTextSize(mTextSize);
                int move = CharacterUtils.needMove(i, differentList);
                if (move != -1) {
                    mOldPaint.setAlpha(255);
                    float p = pp * 2f;
                    p = p > 1 ? 1 : p;
                    float distX = CharacterUtils.getOffset(i, move, p, startX, oldStartX, gaps, oldGaps);
                    canvas.drawText(mOldText.charAt(i) + "", 0, 1, distX, startY, mOldPaint);
                } else {
                    mOldPaint.setAlpha((int) ((1 - pp) * 255));
                    float y = startY - pp * mTextHeight;
                    float width = mOldPaint.measureText(mOldText.charAt(i) + "");
                    canvas.drawText(mOldText.charAt(i) + "", 0, 1, oldOffset + (oldGaps[i] - width) / 2, y, mOldPaint);
                }
                oldOffset += oldGaps[i];
            }

            // draw new text
            if (i < mText.length()) {

                if (!CharacterUtils.stayHere(i, differentList)) {

                    int alpha = (int) (255f / CHAR_TIME * (progress - CHAR_TIME * i / MOST_COUNT));
                    alpha = alpha > 255 ? 255 : alpha;
                    alpha = alpha < 0 ? 0 : alpha;


                    mPaint.setAlpha(alpha);
                    mPaint.setTextSize(mTextSize);
                    float pp = progress / (CHAR_TIME + CHAR_TIME / MOST_COUNT * (mText.length() - 1));
                    float y = mTextHeight + startY - pp * mTextHeight;

                    float width = mPaint.measureText(mText.charAt(i) + "");
                    canvas.drawText(mText.charAt(i) + "", 0, 1, offset + (gaps[i] - width) / 2, y, mPaint);
                }

                offset += gaps[i];
            }
        }
    }

}
