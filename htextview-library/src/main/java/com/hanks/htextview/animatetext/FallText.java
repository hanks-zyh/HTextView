package com.hanks.htextview.animatetext;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.hanks.htextview.util.CharacterUtils;
import com.hanks.htextview.util.HLog;
/**
 * 悬挂坠落效果
 * Created by hanks on 15-12-14.
 */
public class FallText extends HText {

    private float charTime    = 400; // 每个字符动画时间 500ms
    private int   mostCount   = 20; // 最多10个字符同时动画
    private float mTextHeight = 0;
    private float progress ;
    private OvershootInterpolator interpolator;

    @Override protected void initVariables() {
        interpolator = new OvershootInterpolator();

    }

    @Override protected void animateStart(CharSequence text) {
        int n = mText.length();
        n = n <= 0 ? 1 : n;

        // 计算动画总时间
        long duration = (long) (charTime + charTime / mostCount * (n - 1));

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, duration).setDuration(duration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                mHTextView.invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override protected void animatePrepare(CharSequence text) {
        Rect bounds = new Rect();
        mPaint.getTextBounds(mText.toString(), 0, mText.length(), bounds);
        mTextHeight = bounds.height();
    }

    @Override protected void drawFrame(Canvas canvas) {

        float offset = startX;
        float oldOffset = oldStartX;

        int maxLength = Math.max(mText.length(), mOldText.length());

        for (int i = 0; i < maxLength; i++) {

            // draw old text
            if (i < mOldText.length()) {
                //
                float percent = progress / (charTime + charTime / mostCount * (mText.length() - 1));

                mOldPaint.setTextSize(mTextSize);
                int move = CharacterUtils.needMove(i, differentList);
                if (move != -1) {
                    mOldPaint.setAlpha(255);
                    float p = percent * 2f;
                    p = p > 1 ? 1 : p;
                    float distX = CharacterUtils.getOffset(i, move, p, startX, oldStartX, gaps, oldGaps);
                    canvas.drawText(mOldText.charAt(i) + "", 0, 1, distX, startY, mOldPaint);
                } else {

                    mOldPaint.setAlpha(255);
                    float centerX = oldOffset + oldGaps[i] / 2;
                    float width = mOldPaint.measureText(mOldText.charAt(i) + "");

                    float p = percent * 1.4f;
                    p = p > 1 ? 1 : p;

                    p = interpolator.getInterpolation(p);
                    double angle = (1 - p) * (Math.PI);
                    if (i % 2 == 0) {
                        angle = (p * Math.PI) + Math.PI;
                    }
                    float disX = centerX + (float) (width / 2 * Math.cos(angle));
                    float disY = startY + (float) (width / 2 * Math.sin(angle));
                    mOldPaint.setStyle(Paint.Style.STROKE);
                    Path path = new Path();
                    path.moveTo(disX, disY);
                    //求点A（m,n)关于点P(a,b)的对称点B(x,y)
                    // (m+x)/2=a ,x=2a-m
                    // (n+y)/2=b ,y=2b-n
                    path.lineTo(2 * centerX - disX, 2 * startY - disY);
                    if (percent <= 0.7) {
                        // 旋转
                        canvas.drawTextOnPath(mOldText.charAt(i) + "", path, 0, 0, mOldPaint);
                    } else {
                        // 下落
                        float p2 = (float) ((percent - 0.7) / 0.3f);
                        mOldPaint.setAlpha((int) ((1 - p2) * 255));
                        float y = (float) ((p2) * mTextHeight);
                        HLog.i(y);
                        Path path2 = new Path();
                        path2.moveTo(disX, disY + y);
                        path2.lineTo(2 * centerX - disX, 2 * startY - disY + y);
                        canvas.drawTextOnPath(mOldText.charAt(i) + "", path2, 0, 0, mOldPaint);
                    }

                }
                oldOffset += oldGaps[i];
            }

            // draw new text
            if (i < mText.length()) {

                if (!CharacterUtils.stayHere(i, differentList)) {

                    int alpha = (int) (255f / charTime * (progress - charTime * i / mostCount));
                    alpha = alpha > 255 ? 255 : alpha;
                    alpha = alpha < 0 ? 0 : alpha;

                    float size = mTextSize * 1f / charTime * (progress - charTime * i / mostCount);
                    size = size > mTextSize ? mTextSize : size;
                    size = size < 0 ? 0 : size;

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
