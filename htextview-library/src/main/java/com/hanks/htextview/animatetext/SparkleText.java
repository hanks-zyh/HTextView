package com.hanks.htextview.animatetext;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.R;
import com.hanks.htextview.util.CharacterUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * 闪光效果
 * Created by hanks on 15-12-14.
 */
public class SparkleText implements AnimateText {

    float progress = 0;
    Paint paint, oldPaint, sparkPaint;
    float charTime = 1000;
    HTextView mHTextView;
    float upDistance = 0;

    int textColor = Color.WHITE;
    Paint backPaint;

    private DisplayMetrics metrics;
    private CharSequence   mText;
    private CharSequence   mOldText;

    private List<CharacterDiffResult> differentList = new ArrayList<>();

    private float textSize;

    private float startX    = 0;
    private float startY    = 0;
    private float oldStartX = 0;

    private float[] gaps    = new float[100];
    private float[] oldGaps = new float[100];

    private Bitmap sparkBitmap;

    public void init(HTextView hTextView) {
        mHTextView = hTextView;

        mText = "";
        mOldText = "";

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(textColor);
        paint.setStyle(Paint.Style.FILL);

        oldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oldPaint.setColor(textColor);
        oldPaint.setStyle(Paint.Style.FILL);

        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(((ColorDrawable) mHTextView.getBackground()).getColor());
        backPaint.setStyle(Paint.Style.FILL);

        sparkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sparkPaint.setColor(Color.WHITE);
        sparkPaint.setStyle(Paint.Style.FILL);

        metrics = new DisplayMetrics();
        WindowManager windowManger = (WindowManager) hTextView.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManger.getDefaultDisplay().getMetrics(metrics);

        textSize = hTextView.getTextSize();

        sparkBitmap = BitmapFactory.decodeResource(hTextView.getResources(), R.drawable.sparkle);
    }

    @Override public void reset(CharSequence text) {
        progress = 0;
        calc();
        mHTextView.invalidate();
    }

    @Override public void animateText(CharSequence text) {
        mOldText = mText;
        mText = text;

        calc();

        //计算动画总时间
        long duration = (long) charTime;

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, duration).setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                mHTextView.invalidate();
            }
        });
        valueAnimator.start();
        progress = 0;
        mHTextView.invalidate();
    }

    @Override public void onDraw(Canvas canvas) {
        float offset = startX;
        float percent = progress / (charTime);

        paint.setAlpha(255);
        paint.setTextSize(textSize);

        canvas.drawText(mText, 0, mText.length(), offset, startY, paint);

        for (int i = 0; i < mText.length(); i++) {
            offset += gaps[i];
        }

        canvas.drawRect(startX, startY * 1.2f - (1 - percent) * (upDistance + startY * 0.2f), offset, startY * 1.2f, backPaint);
        if (startY * 1.2f - (1 - percent) * (upDistance + startY * 0.2f) < startY) {
            drawSparkle(canvas, startX, startY * 1.2f - (1 - percent) * (upDistance + startY * 0.2f), (offset - startX));
        }

    }

    private void drawSparkle(Canvas canvas, float offset, float y, float width) {
        Random random = new Random();
        for (int i = 0; i < random.nextInt(10); i++) {
            canvas.drawBitmap(getRandomSpark(random), (float) (offset + random.nextDouble() * width), (float) (y - 10 - random
                    .nextDouble() * 6), sparkPaint);
        }
        for (int i = 0; i < width / 3; i++) {
            canvas.drawBitmap(getRandomSpark(random), (float) (offset + random.nextDouble() * width), (float) (y - random
                    .nextDouble() * 10), sparkPaint);
        }
    }

    private Bitmap getRandomSpark(Random random) {
        int dstWidth = random.nextInt(8) + 1;
        return Bitmap.createScaledBitmap(sparkBitmap, dstWidth + random.nextInt(3), dstWidth + random
                .nextInt(5), false);
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
