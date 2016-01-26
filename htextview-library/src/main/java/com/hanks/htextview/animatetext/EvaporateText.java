package com.hanks.htextview.animatetext;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.hanks.htextview.animatetext.base.IHTextImpl;
import com.hanks.htextview.util.CharacterUtils;
import com.hanks.htextview.util.MathUtils;

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
        // draw old text
        float oldOffset = oldStartX;
        float pp = progress / (CHAR_TIME + CHAR_TIME * mText.length() / MOST_COUNT);
        for (int i = 0; i < mOldText.length(); ++i) {
            mOldPaint.setTextSize(mTextSize);
            int move = CharacterUtils.needMove(i, differentList);
            if (move != -1) {
                mOldPaint.setAlpha(255);
                float p = pp > 0.5 ? 1 : pp * 2;
                float distX = CharacterUtils.getOffset(i, move, p, startX, oldStartX, gaps, oldGaps);
                canvas.drawText(mOldText.charAt(i) + "", 0, 1, distX, startY, mOldPaint);
                oldOffset += oldGaps[i];
                continue;
            }
            mOldPaint.setAlpha((int) ((1 - pp) * 255));
            float y = startY - pp * mTextHeight;
            float width = mOldPaint.measureText(mOldText.charAt(i) + "");
            canvas.drawText(mOldText.charAt(i) + "", 0, 1, oldOffset + (oldGaps[i] - width) / 2, y, mOldPaint);
            oldOffset += oldGaps[i];
        }
        // draw new text
        for (int i = 0; i < mText.length(); ++i) {
            if (CharacterUtils.stayHere(i, differentList)) {
                offset += gaps[i];
                continue;
            }
            int alpha = (int) (255 / CHAR_TIME * (progress - CHAR_TIME * i / MOST_COUNT));
            mPaint.setAlpha(MathUtils.constrain(0, 255, alpha));
            mPaint.setTextSize(mTextSize);
            float y = mTextHeight + startY - pp * mTextHeight;
            float width = mPaint.measureText(mText.charAt(i) + "");
            canvas.drawText(mText.charAt(i) + "", 0, 1, offset + (gaps[i] - width) / 2, y, mPaint);
            offset += gaps[i];
        }
    }
}
