package com.hanks.htextview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Animate TextView
 */
public class HTextView extends TextView {

    float progress = 0;
    Paint paint, oldPaint;
    float charTime  = 400; // 每个字符动画时间 500ms
    int   mostCount = 20; // 最多10个字符同时动画
    private float[] gaps    = new float[100];
    private float[] oldGaps = new float[100];
    private DisplayMetrics metrics;
    private float          textSize;
    private CharSequence mText;
    private CharSequence mOldText;
    private float oldStartX = 0;
    private float startX    = 0;
    private float startY    = 0;
    private List<Different> differentList = new ArrayList<>();

    public HTextView(Context context) {
        super(context);
        init(null, 0);
    }

    public HTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public HTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        mText = "";
        mOldText = "";

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        oldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oldPaint.setColor(Color.BLACK);
        oldPaint.setStyle(Paint.Style.FILL);

        metrics = new DisplayMetrics();
        WindowManager windowManger = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManger.getDefaultDisplay().getMetrics(metrics);

        textSize = getTextSize();

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.HTextView, defStyle, 0);

        a.recycle();

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
      /*  mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;*/
    }

    public void animateText(CharSequence text) {
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
                invalidate();
            }
        });
        valueAnimator.start();

    }

    private void calc() {
        textSize = getTextSize();
        paint.setTextSize(textSize);

        for (int i = 0; i < mText.length(); i++) {
            gaps[i] = paint.measureText(mText.charAt(i) + "");
        }

        oldPaint.setTextSize(textSize);

        for (int i = 0; i < mOldText.length(); i++) {
            oldGaps[i] = oldPaint.measureText(mOldText.charAt(i) + "");
        }

        oldStartX = (getWidth() - oldPaint.measureText(mOldText.toString())) / 2f;

        startX = (getWidth() - paint.measureText(mText.toString())) / 2f;
        startY = (int) ((getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        differentList.clear();
        differentList.addAll(diff(mOldText, mText));
    }

    @Override protected void onDraw(Canvas canvas) {

        float offset = startX;
        float oldOffset = oldStartX;

        int maxLength = Math.max(mText.length(), mOldText.length());

        for (int i = 0; i < maxLength; i++) {

            // draw old text
            if (i < mOldText.length()) {
//
                float pp = progress / (charTime + charTime / mostCount * (mText.length() - 1));

                int move = needMove(i);
                if (move != -1) {
                    oldPaint.setTextSize(textSize);
                    oldPaint.setAlpha(255);
                    float p = pp * 2f;
                    p = p > 1 ? 1 : p;
                    float distX = getOffset(i, move, p);
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

                if (!stayHere(i)) {

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

    private float getOffset(int from, int move, float progress) {

        // 计算目标点
        float dist = startX;
        for (int i = 0; i < move; i++) {
            dist += gaps[i];
        }

        // 计算当前点
        float cur = oldStartX;
        for (int i = 0; i < from; i++) {
            cur += oldGaps[i];
        }

        return cur + (dist - cur) * progress;

    }

    public int needMove(int index) {
        for (Different different : differentList) {
            if (different.fromIndex == index) {
                return different.moveIndex;
            }
        }
        return -1;
    }

    public boolean stayHere(int index) {
        for (Different different : differentList) {
            if (different.moveIndex == index) {
                return true;
            }
        }
        return false;
    }

    public List<Different> diff(CharSequence oldText, CharSequence newText) {

        List<Different> differentList = new ArrayList<>();
        Set<Integer> skip = new HashSet<>();

        for (int i = 0; i < oldText.length(); i++) {
            char c = oldText.charAt(i);
            for (int j = 0; j < newText.length(); j++) {
                if (!skip.contains(j) && c == newText.charAt(j)) {
                    skip.add(j);
                    Different different = new Different();
                    different.c = c;
                    different.fromIndex = i;
                    different.moveIndex = j;
                    differentList.add(different);
                    break;
                }
            }
        }
        return differentList;

    }

    public void reset(CharSequence text) {
        progress = 1;
        calc();
        invalidate();
    }

    class Different {
        char c;
        int  fromIndex;
        int  moveIndex;
    }
}
