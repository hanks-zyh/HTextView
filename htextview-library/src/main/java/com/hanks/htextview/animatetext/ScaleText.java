package com.hanks.htextview.animatetext;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.hanks.htextview.animatetext.base.IHTextImpl;
import com.hanks.htextview.util.CharacterUtils;
import com.hanks.htextview.util.MathUtils;

/**
 * keynote 默认变小然后淡出效果
 * Created by hanks on 15-12-14.
 */
public class ScaleText extends IHTextImpl {

    private static final float MOST_COUNT = 20;
    private static final float CHAR_TIME = 400;
    private long duration;
    private float progress;

    @Override
    protected void animateStart() {
        int textLength = mText.length();
        textLength = textLength < 1 ? 1 : textLength;
        // 计算动画总时间
        duration = (long) (CHAR_TIME + CHAR_TIME / MOST_COUNT * (textLength - 1));
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
        // draw old text
        float oldOffset = oldStartX;
        for (int i = 0; i < mOldText.length(); ++i) {
            float percent = progress / duration;
            int move = CharacterUtils.needMove(i, differentList);
            if (move != CharacterUtils.NEED_TO_DISCUSS) {
                mOldPaint.setTextSize(mTextSize);
                mOldPaint.setAlpha(255);
                float progress2X = percent > 0.5 ? 1 : percent * 2;
                float distX = CharacterUtils.getOffset(i, move, progress2X, startX, oldStartX, gaps, oldGaps);
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
        float offset = startX;
        for (int i = 0; i < mText.length(); ++i) {
            if (CharacterUtils.stayHere(i, differentList)) {
                offset += gaps[i];
                continue;
            }
            int alpha = (int) (255f / CHAR_TIME * (progress - CHAR_TIME * i / MOST_COUNT));
            float size = mTextSize * 1f / CHAR_TIME * (progress - CHAR_TIME * i / MOST_COUNT);
            float width = mPaint.measureText(mText.charAt(i) + "");
            mPaint.setAlpha(MathUtils.constrain(0, 255, alpha));
            mPaint.setTextSize(MathUtils.constrain(0, size, mTextSize));
            canvas.drawText(mText.charAt(i) + "", 0, 1, offset + (gaps[i] - width) / 2, startY, mPaint);
            offset += gaps[i];
        }
    }
}