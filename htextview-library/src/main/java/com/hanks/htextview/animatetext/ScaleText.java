package com.hanks.htextview.animatetext;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.hanks.htextview.animatetext.base.IHTextImpl;
import com.hanks.htextview.util.CharacterUtils;

public class ScaleText extends IHTextImpl {

    private static final float MOST_COUNT = 20;
    private static final float CHAR_TIME = 400;
    private long duration;
    private float progress;


    @Override
    protected void animateStart() {
        int n = mText.length();
        n = n <= 0 ? 1 : n;
        // 计算动画总时间
        duration = (long) (CHAR_TIME + CHAR_TIME / MOST_COUNT * (n - 1));
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, duration).setDuration(duration);
        //插值，两头慢中间快
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
    public void drawFrame(Canvas canvas) {
        float offset = startX;
        float oldOffset = oldStartX;
        // draw old text
        for (int i = 0; i < mOldText.length(); ++i) {
            float percent = progress / duration;
            int move = CharacterUtils.needMove(i, differentList);
            if (move != -1) {
                mOldPaint.setTextSize(mTextSize);
                mOldPaint.setAlpha(255);
                float p = percent * 2f;
                p = p > 1 ? 1 : p;
                float distX = CharacterUtils.getOffset(i, move, p, startX, oldStartX, gaps, oldGaps);
                canvas.drawText(mOldText.charAt(i) + "", 0, 1, distX, startY, mOldPaint);
                oldOffset += oldGaps[i];
                continue;
            }
            mOldPaint.setAlpha((int) ((1 - percent) * 255));
            mOldPaint.setTextSize(mTextSize * (1 - percent));
            float width = mOldPaint.measureText(mOldText.charAt(i) + "");
            canvas.drawText(mOldText.charAt(i) + "", 0, 1, oldOffset + (oldGaps[i] - width) / 2, startY, mOldPaint);
            oldOffset += oldGaps[i];
        }

        // draw new text
        for (int i = 0; i < mText.length(); ++i) {
            if (!CharacterUtils.stayHere(i, differentList)) {
                int alpha = (int) (255f / CHAR_TIME * (progress - CHAR_TIME * i / MOST_COUNT));
                if (alpha > 255) alpha = 255;
                if (alpha < 0) alpha = 0;
                float size = mTextSize * 1f / CHAR_TIME * (progress - CHAR_TIME * i / MOST_COUNT);
                if (size > mTextSize) size = mTextSize;
                if (size < 0) size = 0;
                mPaint.setAlpha(alpha);
                mPaint.setTextSize(size);
                float width = mPaint.measureText(mText.charAt(i) + "");
                canvas.drawText(mText.charAt(i) + "", 0, 1, offset + (gaps[i] - width) / 2, startY, mPaint);
            }
            offset += gaps[i];
        }
    }
}