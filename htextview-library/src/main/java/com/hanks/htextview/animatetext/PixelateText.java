package com.hanks.htextview.animatetext;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.animatetext.base.IHText;
import com.hanks.htextview.util.MathUtils;

/**
 * 缩放动画
 * Created by hanks on 15-12-14.
 */
public class PixelateText implements IHText {

    private static final int MOST_COUNT = 20; // 最多20个字符同时动画
    private static final float CHAR_TIME = 1000; // 每个字符动画时间 1000ms
    private float progress;
    private float textSize;
    private Paint paint, oldPaint, pixPaint;
    private HTextView mHTextView;

    private CharSequence mText;
    private CharSequence mOldText;

    private float oldStartX = 0;
    private float startX = 0;
    private float startY = 0;

    private Bitmap bitmap = Bitmap.createBitmap(700, 200, Bitmap.Config.ARGB_4444);
    private Canvas pixCanvas = new Canvas(bitmap);
    private Matrix matrix = new Matrix();


    @Override
    public void init(HTextView hTextView, AttributeSet attrs, int defStyle) {
        mHTextView = hTextView;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        oldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oldPaint.setColor(Color.BLACK);
        oldPaint.setStyle(Paint.Style.FILL);

        pixPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pixPaint.setColor(Color.BLACK);
        pixPaint.setStyle(Paint.Style.FILL);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManger = (WindowManager) hTextView.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManger.getDefaultDisplay().getMetrics(metrics);

        textSize = hTextView.getTextSize();
    }

    @Override
    public void reset(CharSequence text) {
        progress = 1;
        calculate();
        mHTextView.invalidate();
    }

    @Override
    public void animateText(CharSequence text) {
        mOldText = mText == null ? "" : mText;
        mText = text;
        calculate();
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
    public void onDraw(Canvas canvas) {
        float offset = startX;
        float oldOffset = oldStartX;
        float percent = progress / (CHAR_TIME + CHAR_TIME / MOST_COUNT * mText.length());
        int alpha = (int) (255 * percent);
        int oldAlpha = (int) (255 * (1 - percent));
        alpha = MathUtils.constrain(0, 255, alpha);
        oldAlpha = MathUtils.constrain(0, 255, oldAlpha);

        oldPaint.setAlpha(oldAlpha);
        paint.setAlpha(alpha);
        pixCanvas.drawColor(Color.WHITE);
        pixCanvas.drawText(mOldText, 0, mOldText.length(), oldOffset, startY, oldPaint);
        pixCanvas.drawText(mText, 0, mText.length(), offset, startY, paint);
        canvas.drawBitmap(bitmap, matrix, pixPaint);
    }

    private void calculate() {
        textSize = mHTextView.getTextSize();
        paint.setTextSize(textSize);
        oldPaint.setTextSize(textSize);
        oldStartX = (mHTextView.getWidth() - oldPaint.measureText(mOldText.toString())) / 2f;
        startX = (mHTextView.getWidth() - paint.measureText(mText.toString())) / 2f;
        startY = (int) ((mHTextView.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
        oldPaint.setTextSize(textSize);
    }

}
