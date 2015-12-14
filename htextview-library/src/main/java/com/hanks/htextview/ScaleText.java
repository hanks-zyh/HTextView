package com.hanks.htextview;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.hanks.htextview.util.CharacterUtils;

import java.util.ArrayList;
import java.util.List;
/**
 * 缩放动画
 * Created by hanks on 15-12-14.
 */
public class ScaleText implements AnimateText {

    float progress = 0;
    Paint paint, oldPaint;
    float charTime  = 400; // 每个字符动画时间 500ms
    int   mostCount = 20; // 最多10个字符同时动画
    HTextView mHTextView;
    private float[] gaps    = new float[100];
    private float[] oldGaps = new float[100];
    private DisplayMetrics metrics;
    private float          textSize;
    private CharSequence   mText;
    private CharSequence   mOldText;
    private List<CharacterDiffResult> differentList = new ArrayList<>();
    private float oldStartX = 0;
    private float startX    = 0;
    private float startY    = 0;

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

                int move = CharacterUtils.needMove(i,differentList);
                if (move != -1) {
                    oldPaint.setTextSize(textSize);
                    oldPaint.setAlpha(255);
                    float p = pp * 2f;
                    p = p > 1 ? 1 : p;
                    float distX = CharacterUtils.getOffset(i, move, p,startX,oldStartX,gaps,oldGaps);
                    canvas.drawText(mOldText.charAt(i) + "", 0, 1, distX, startY, oldPaint);
                } else {
                    oldPaint.setAlpha((int) ((1 - pp) * 255));
                    oldPaint.setTextSize(textSize * (1 - pp));
                    float width = oldPaint.measureText(mOldText.charAt(i) + "");
                    canvas.drawText(mOldText.charAt(i) + "", 0, 1, oldOffset + (oldGaps[i] - width) / 2, startY, oldPaint);
                }
                oldOffset += oldGaps[i];
            }

            // draw new text
            if (i < mText.length()) {

                if (!CharacterUtils.stayHere(i,differentList)) {

                    int alpha = (int) (255f / charTime * (progress - charTime * i / mostCount));
                    alpha = alpha > 255 ? 255 : alpha;
                    alpha = alpha < 0 ? 0 : alpha;

                    float size = textSize * 1f / charTime * (progress - charTime * i / mostCount);
                    //                  float size = (textSize * progress * 6 - i * 5 * textSize/(mText.length()-1));
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
    }
}
