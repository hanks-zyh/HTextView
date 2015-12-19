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
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.R;
import com.hanks.htextview.util.CharacterUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * 蒸发效果
 * Created by hanks on 15-12-14.
 */
public class BurnText implements IHText {

    float progress = 0;
    Paint paint, oldPaint;
    float charTime  = 5000; // 每个字符动画时间 500ms
    int   mostCount = 20; // 最多10个字符同时动画
    HTextView mHTextView;
    float upDistance = 0;

    int textColor = Color.WHITE;
    Paint backPaint;
    private float[] gaps    = new float[100];
    private float[] oldGaps = new float[100];
    private DisplayMetrics metrics;
    private float          textSize;
    private CharSequence mText;
    private CharSequence mOldText;
    private List<CharacterDiffResult> differentList = new ArrayList<>();
    private float oldStartX = 0;
    private float startX    = 0;
    private float startY    = 0;
    private Bitmap sparkBitmap;

    public void init(HTextView hTextView, AttributeSet attrs, int defStyle) {
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
        backPaint.setColor(((ColorDrawable)mHTextView.getBackground()).getColor());
        backPaint.setStyle(Paint.Style.FILL);

        metrics = new DisplayMetrics();
        WindowManager windowManger = (WindowManager) hTextView.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManger.getDefaultDisplay().getMetrics(metrics);

        textSize = hTextView.getTextSize();

        sparkBitmap = BitmapFactory.decodeResource(hTextView.getResources(), R.drawable.fire);
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
        float percent = progress / (charTime + charTime / mostCount * (mText.length() - 1));

        for (int i = 0; i < maxLength; i++) {

            // draw new text
            if (i < mText.length()) {

                if (!CharacterUtils.stayHere(i, differentList)) {

                    paint.setAlpha(255);
                    paint.setTextSize(textSize);
                    float width = paint.measureText(mText.charAt(i) + "");
                    canvas.drawText(mText.charAt(i) + "", 0, 1, offset, startY, paint);
                    canvas.drawRect(offset, startY*1.2f - (1 - percent) * (upDistance+startY*0.2f), offset + gaps[i], startY *1.2f, backPaint);
                    if (percent < 1) {
                        drawSparkle(canvas, offset, startY - (1 - percent) * upDistance, width);
                    }

                }
                offset += gaps[i];
            }
            // draw old text
            if (i < mOldText.length()) {
                canvas.save();
                //
                float pp = progress / (charTime + charTime / mostCount * (mText.length() - 1));

                oldPaint.setTextSize(textSize);
                int move = CharacterUtils.needMove(i, differentList);
                if (move != -1) {
                    oldPaint.setAlpha(255);
                    float p = pp * 7f;
                    p = p > 1 ? 1 : p;
                    float distX = CharacterUtils.getOffset(i, move, p, startX, oldStartX, gaps, oldGaps);
                    canvas.drawText(mOldText.charAt(i) + "", 0, 1, distX, startY, oldPaint);
                } else {

                    float p = pp * 3.5f;
                    p = p > 1 ? 1 : p;
                    oldPaint.setAlpha((int) (255 * (1 - p)));
                    canvas.drawText(mOldText.charAt(i) + "", 0, 1, oldOffset, startY, oldPaint);
                }
                oldOffset += oldGaps[i];
            }

        }
    }

    private void drawSparkle(Canvas canvas, float offset, float startY, float width) {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            canvas.drawBitmap(getRandomSpark(random), (float) (offset + random.nextDouble() * width), (float) (startY -  random.nextGaussian() * Math.sqrt(upDistance)), paint);
        }
    }

    private Bitmap getRandomSpark(Random random) {
        int dstWidth = random.nextInt(22) + 20;
        return Bitmap.createScaledBitmap(sparkBitmap, dstWidth, dstWidth, false);
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
