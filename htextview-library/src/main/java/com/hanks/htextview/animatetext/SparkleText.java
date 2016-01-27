package com.hanks.htextview.animatetext;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;

import com.hanks.htextview.R;
import com.hanks.htextview.animatetext.base.IHTextImpl;
import com.hanks.htextview.util.CharacterUtils;

import java.util.Random;

/**
 * 蒸发效果
 * Created by hanks on 15-12-14.
 */
public class SparkleText extends IHTextImpl {

    private static final float CHAR_TIME = 400; // 每个字符动画时间 400ms
    private static final int MOST_COUNT = 20; // 最多20个字符同时动画

    private float upDistance;
    private float progress;

    private Paint backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap sparkBitmap;
    private Rect bounds = new Rect();

    @Override
    protected void initVariables() {
        backPaint.setColor(((ColorDrawable) mHTextView.getBackground()).getColor());
        backPaint.setStyle(Paint.Style.FILL);
        mPaint.setAlpha(255);
        sparkBitmap = BitmapFactory.decodeResource(mHTextView.getResources(), R.drawable.sparkle);
    }

    @Override
    protected void animateStart() {
        int textLength = mText.length();
        textLength = textLength <= 0 ? 1 : textLength;
        // 计算动画总时间
        long duration = (long) (CHAR_TIME + CHAR_TIME / MOST_COUNT * (textLength - 1));
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, duration).setDuration(duration);
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
        mPaint.getTextBounds(mText.toString(), 0, mText.length(), bounds);
        upDistance = bounds.height();
    }

    @Override
    protected void drawFrame(Canvas canvas) {

        float percent = progress / (CHAR_TIME + CHAR_TIME / MOST_COUNT * (mText.length() - 1));
        mPaint.setTextSize(mTextSize);

        // draw new text
        float offset = startX;
        for (int i = 0; i < mText.length(); ++i) {
            if (CharacterUtils.stayHere(i, differentList)) {
                offset += gaps[i];
                continue;
            }
            float width = mPaint.measureText(mText.charAt(i) + "");
            canvas.drawText(mText.charAt(i) + "", 0, 1, offset, startY, mPaint);
            if (percent < 1) {
                drawSparkle(canvas, offset, startY - (1 - percent) * upDistance, width);
            }
            canvas.drawRect(offset, startY * 1.2f - (1 - percent) * (upDistance + startY * 0.2f), offset + gaps[i], startY * 1.2f, backPaint);
            offset += gaps[i];
        }

        // draw old text
        float oldOffset = oldStartX;
        for (int i = 0; i < mOldText.length(); ++i) {
            mOldPaint.setTextSize(mTextSize);
            int move = CharacterUtils.needMove(i, differentList);
            if (move != -1) {
                mOldPaint.setAlpha(255);
                float percent2X = percent > 0.5f ? 1 : percent * 2f;
                float distX = CharacterUtils.getOffset(i, move, percent2X, startX, oldStartX, gaps, oldGaps);
                canvas.drawText(mOldText.charAt(i) + "", 0, 1, distX, startY, mOldPaint);
                oldOffset += oldGaps[i];
                continue;
            }
            float p = percent * 3.5f;
            p = p > 1 ? 1 : p;
            mOldPaint.setAlpha((int) (255 * (1 - p)));
            canvas.drawText(mOldText.charAt(i) + "", 0, 1, oldOffset, startY, mOldPaint);
            oldOffset += oldGaps[i];
        }
    }

    private void drawSparkle(Canvas canvas, float offset, float startY, float width) {
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            //这里加一个Temp避免OOM
            Bitmap temp = getRandomSpark(random);
            canvas.drawBitmap(temp, (float) (offset + random.nextDouble() * width), (float) (startY - random
                    .nextGaussian() * Math.sqrt(upDistance)), mPaint);
            temp.recycle();
        }
    }

    private Bitmap getRandomSpark(Random random) {
        int dstWidthAndHeight = random.nextInt(12) + 1;
        return Bitmap.createScaledBitmap(sparkBitmap, dstWidthAndHeight, dstWidthAndHeight, false);
    }
}
