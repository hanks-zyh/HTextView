package com.hanks.htextview.animatetext;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.hanks.htextview.animatetext.base.IHTextImpl;
import com.hanks.htextview.util.CharacterUtils;
import com.hanks.htextview.util.MathUtils;

/**
 * 悬挂坠落效果
 * Created by hanks on 15-12-14.
 */
public class FallText extends IHTextImpl {

    private static final float CHAR_TIME = 400; // 每个字符动画时间 400ms
    private static final int MOST_COUNT = 20; // 最多20个字符同时动画
    private float mTextHeight;
    private float progress;
    private OvershootInterpolator interpolator = new OvershootInterpolator();
    private Path path = new Path();

    @Override
    protected void animateStart() {
        int n = mText.length() < 1 ? 1 : mText.length();
        // 计算动画总时间
        long duration = (long) (CHAR_TIME + CHAR_TIME / MOST_COUNT * (n - 1));
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
    protected void drawFrame(Canvas canvas) {
        float offset = startX;
        float percent = progress / (CHAR_TIME + CHAR_TIME / MOST_COUNT * (mText.length() - 1));
        // draw old text
        float oldOffset = oldStartX;
        for (int i = 0; i < mOldText.length(); ++i) {
            mOldPaint.setTextSize(mTextSize);
            mOldPaint.setAlpha(255);
            int move = CharacterUtils.needMove(i, differentList);
            if (move != -1) {
                float p = percent > 0.5f ? 1 : percent * 2f;
                float distX = CharacterUtils.getOffset(i, move, p, startX, oldStartX, gaps, oldGaps);
                canvas.drawText(mOldText.charAt(i) + "", 0, 1, distX, startY, mOldPaint);
                oldOffset += oldGaps[i];
                continue;
            }
            float centerX = oldOffset + oldGaps[i] / 2;
            float width = mOldPaint.measureText(mOldText.charAt(i) + "");
            float percentX = percent * 1.4f;
            percentX = percentX > 1 ? 1 : percentX;
            float interpolation = interpolator.getInterpolation(percentX);
            double angle = (1 - interpolation) * (Math.PI);
            if (i % 2 == 0) {
                angle = (interpolation * Math.PI) + Math.PI;
            }
            float disX = centerX + (float) (width / 2 * Math.cos(angle));
            float disY = startY + (float) (width / 2 * Math.sin(angle));
            mOldPaint.setStyle(Paint.Style.STROKE);
            path.reset();
            path.moveTo(disX, disY);
            //求点A（m,n)关于点P(a,b)的对称点B(x,y)
            // (m+x)/2=a ,x=2a-m
            // (n+y)/2=b ,y=2b-n
            path.lineTo(2 * centerX - disX, 2 * startY - disY);
            oldOffset += oldGaps[i];
            // 旋转
            if (percent <= 0.7f) {
                canvas.drawTextOnPath(mOldText.charAt(i) + "", path, 0, 0, mOldPaint);
                continue;
            }
            // 下落
            float p2 = (percent - 0.7f) / 0.3f;
            float y = (p2) * mTextHeight;
            mOldPaint.setAlpha((int) ((1 - p2) * 255));
            path.reset();
            path.moveTo(disX, disY + y);
            path.lineTo(2f * centerX - disX, 2f * startY - disY + y);
            canvas.drawTextOnPath(mOldText.charAt(i) + "", path, 0, 0, mOldPaint);
        }

        // draw new text
        for (int i = 0; i < mText.length(); ++i) {
            if (CharacterUtils.stayHere(i, differentList)) {
                offset += gaps[i];
                continue;
            }
            float width = mPaint.measureText(mText.charAt(i) + "");
            int alpha = (int) (255f / CHAR_TIME * (progress - CHAR_TIME * i / MOST_COUNT));
            float size = mTextSize * 1f / CHAR_TIME * (progress - CHAR_TIME * i / MOST_COUNT);
            alpha = MathUtils.constrain(0, 255, alpha);
            size = MathUtils.constrain(0, mTextSize, size);
            mPaint.setAlpha(alpha);
            mPaint.setTextSize(size);
            canvas.drawText(mText.charAt(i) + "", 0, 1, offset + (gaps[i] - width) / 2, startY, mPaint);
            offset += gaps[i];
        }
    }
}
