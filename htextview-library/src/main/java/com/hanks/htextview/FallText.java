package com.hanks.htextview;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.hanks.htextview.util.CharacterUtils;
import com.hanks.htextview.util.HLog;

import java.util.ArrayList;
import java.util.List;
/**
 * 蒸发效果
 * Created by hanks on 15-12-14.
 */
public class FallText implements AnimateText {

    float progress = 0;
    Paint paint, oldPaint;
    float charTime  = 400; // 每个字符动画时间 500ms
    int   mostCount = 20; // 最多10个字符同时动画
    HTextView mHTextView;
    float upDistance = 0;
    private float[] gaps    = new float[100];
    private float[] oldGaps = new float[100];
    private DisplayMetrics metrics;
    private float          textSize;
    private CharSequence   mText;
    private CharSequence   mOldText;
    private List<CharacterDiffResult> differentList = new ArrayList<>();
    private float                     oldStartX     = 0;
    private float                     startX        = 0;
    private float                     startY        = 0;

    public void init(HTextView hTextView) {
        mHTextView = hTextView;

        mText = "";
        mOldText = "";

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        oldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oldPaint.setColor(Color.BLACK);
        oldPaint.setStyle(Paint.Style.FILL);

        metrics = new DisplayMetrics();
        WindowManager windowManger = (WindowManager) hTextView.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManger.getDefaultDisplay().getMetrics(metrics);

        textSize = hTextView.getTextSize();

    }

    @Override public void reset(CharSequence text) {
        progress = 1;
        calc();
        mHTextView.invalidate();
    }

    @Override public void animateText(CharSequence text) {
        mOldText = mText;
        mText = text;

        calc();

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

    @Override public void onDraw(Canvas canvas) {
        float offset = startX;
        float oldOffset = oldStartX;

        int maxLength = Math.max(mText.length(), mOldText.length());

        for (int i = 0; i < maxLength; i++) {

            // draw old text
            if (i < mOldText.length()) {
                //
                float pp = progress / (charTime + charTime / mostCount * (mText.length() - 1));

                oldPaint.setTextSize(textSize);
                int move = CharacterUtils.needMove(i, differentList);
                if (move != -1) {
                    oldPaint.setAlpha(255);
                    float p = pp * 2f;
                    p = p > 1 ? 1 : p;
                    float distX = CharacterUtils.getOffset(i, move, p, startX, oldStartX, gaps, oldGaps);
                    canvas.drawText(mOldText.charAt(i) + "", 0, 1, distX, startY, oldPaint);
                } else {

                    oldPaint.setAlpha(255);
                    float centerX = oldOffset + oldGaps[i] / 2;
                    float width = oldPaint.measureText(mOldText.charAt(i) + "");

                    float p = pp * 1.5f;
                    p = p > 1 ? 1 : p;
                    double angle = (1 - p) * (Math.PI);
                    if (i % 2 == 0) {
                        angle = ( p * Math.PI) + Math.PI;
                    }
                    float disX = centerX + (float) (width / 2 * Math.cos(angle));
                    float disY = startY + (float) (width / 2 * Math.sin(angle));
                    oldPaint.setStyle(Paint.Style.STROKE);
                    Path path = new Path();
                    path.moveTo(disX, disY);
                    //求点A（m,n)关于点P(a,b)的对称点B(x,y)
                    // (m+x)/2=a ,x=2a-m
                    // (n+y)/2=b ,y=2b-n
                    path.lineTo(2 * centerX - disX, 2 * startY - disY);
                    if (pp <= 0.7) {
                        canvas.drawTextOnPath(mOldText.charAt(i) + "", path, 0, 0, oldPaint);
                    } else {
                        float p2 = (float) ((pp - 0.7) / 0.3f);
                        oldPaint.setAlpha((int) ((1 - p2) * 255));
                        float y = (float) ((p2) * upDistance);
                        HLog.i(y);
                        Path path2 = new Path();
                        path2.moveTo(disX, disY + y);
                        //求点A（m,n)关于点P(a,b)的对称点B(x,y)
                        // (m+x)/2=a ,x=2a-m
                        // (n+y)/2=b ,y=2b-n
                        path2.lineTo(2 * centerX - disX, 2 * startY - disY + y);
                        canvas.drawTextOnPath(mOldText.charAt(i) + "", path2, 0, 0, oldPaint);
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

                    float size = textSize * 1f / charTime * (progress - charTime * i / mostCount);
                    size = size > textSize ? textSize : size;
                    size = size < 0 ? 0 : size;

                    paint.setAlpha(alpha);
                    paint.setTextSize(size);
                    float width = paint.measureText(mText.charAt(i) + "");
                    canvas.drawText(mText.charAt(i) + "", 0, 1, offset + (gaps[i] - width) / 2, startY, paint);
                }

                offset += gaps[i];
            }
        }
    }

    private void calc() {
        textSize = mHTextView.getTextSize();
        paint.setTextSize(textSize);

        for (int i = 0; i < mText.length(); i++) {
            gaps[i] = paint.measureText(mText.charAt(i) + "");
        }

        oldPaint.setTextSize(textSize);

        for (int i = 0; i < mOldText.length(); i++) {
            oldGaps[i] = oldPaint.measureText(mOldText.charAt(i) + "");
        }

        oldStartX = (mHTextView.getWidth() - oldPaint.measureText(mOldText.toString())) / 2f;

        startX = (mHTextView.getWidth() - paint.measureText(mText.toString())) / 2f;
        startY = (int) ((mHTextView.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        differentList.clear();
        differentList.addAll(CharacterUtils.diff(mOldText, mText));

        Rect bounds = new Rect();
        paint.getTextBounds(mText.toString(), 0, mText.length(), bounds);
        upDistance = bounds.height();
    }
}
