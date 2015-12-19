package com.hanks.htextview.animatetext;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.hanks.htextview.util.CharacterUtils;
/**
 * 缩放动画
 * Created by hanks on 15-12-14.
 */
public class ScaleText extends HText {

    float mostCount = 20;

    @Override protected void initVariables() {

    }

    @Override protected void animateStart(CharSequence text) {
        int n = mText.length();
        n = n <= 0 ? 1 : n;
        // 计算动画总时间
        final long duration = (long) (ANIMATE_DURATION + ANIMATE_DURATION / mostCount * (n - 1));

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, duration).setDuration(duration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue() / duration;
                mHTextView.invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override protected void animatePrepare(CharSequence text) {

    }

    @Override public void drawFrame(Canvas canvas) {
        float offset = startX;
        float oldOffset = oldStartX;

        int maxLength = Math.max(mText.length(), mOldText.length());

        for (int i = 0; i < maxLength; i++) {

            // draw old text
            if (i < mOldText.length()) {

                int move = CharacterUtils.needMove(i, differentList);
                if (move != -1) {
                    mOldPaint.setTextSize(mTextSize);
                    mOldPaint.setAlpha(255);
                    float p = progress * 2f;
                    p = p > 1 ? 1 : p;
                    float distX = CharacterUtils.getOffset(i, move, p, startX, oldStartX, gaps, oldGaps);
                    canvas.drawText(mOldText.charAt(i) + "", 0, 1, distX, startY, mOldPaint);
                } else {
                    mOldPaint.setAlpha((int) ((1 - progress) * 255));
                    mOldPaint.setTextSize(mTextSize * (1 - progress));
                    float width = mOldPaint.measureText(mOldText.charAt(i) + "");
                    canvas.drawText(mOldText.charAt(i) + "", 0, 1, oldOffset + (oldGaps[i] - width) / 2, startY, mOldPaint);
                }
                oldOffset += oldGaps[i];
            }

            // draw new text
            if (i < mText.length()) {

                if (!CharacterUtils.stayHere(i, differentList)) {

                    int alpha = (int) (255f / ANIMATE_DURATION * (progress - ANIMATE_DURATION * i / mostCount));
                    alpha = alpha > 255 ? 255 : alpha;
                    alpha = alpha < 0 ? 0 : alpha;

                    float size = mTextSize * 1f / ANIMATE_DURATION * (progress - ANIMATE_DURATION * i / mostCount);
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

}
