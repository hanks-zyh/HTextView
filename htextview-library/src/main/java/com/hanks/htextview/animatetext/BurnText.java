package com.hanks.htextview.animatetext;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.R;
import com.hanks.htextview.animatetext.base.IHText;
import com.hanks.htextview.util.CharacterDiffResult;
import com.hanks.htextview.util.CharacterUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 蒸发效果
 * Created by hanks on 15-12-14.
 */
public class BurnText implements IHText {

    private static final float CHAR_TIME = 500; // 每个字符动画时间 500ms
    private static final int MOST_COUNT = 20; // 最多20个字符同时动画
    private float progress;
    private float upDistance;
    private float oldStartX, startX, startY;
    private float[] gaps = new float[100];
    private float[] oldGaps = new float[100];
    private float textSize;
    private Paint paint, oldPaint;
    //我觉得没有黑块更好看
    //private Paint backPaint;
    private HTextView mHTextView;
    private CharSequence mText, mOldText;
    private List<CharacterDiffResult> differentList = new ArrayList<>();
    private Bitmap sparkBitmap;
    private Rect bounds = new Rect();

    public void init(HTextView hTextView, AttributeSet attrs, int defStyle) {
        mHTextView = hTextView;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        oldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oldPaint.setColor(Color.WHITE);
        oldPaint.setStyle(Paint.Style.FILL);

        //backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //backPaint.setColor(((ColorDrawable) mHTextView.getBackground()).getColor());
        //backPaint.setStyle(Paint.Style.FILL);

        textSize = hTextView.getTextSize();
        sparkBitmap = BitmapFactory.decodeResource(hTextView.getResources(), R.drawable.fire);
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
    public void onDraw(Canvas canvas) {
        float percent = progress / (CHAR_TIME + CHAR_TIME / MOST_COUNT * (mText.length() - 1));

        // draw new text
        float offset = startX;
        paint.setAlpha(255);
        paint.setTextSize(textSize);
        for (int i = 0; i < mText.length(); i++) {
            if (CharacterUtils.stayHere(i, differentList)) {
                offset += gaps[i];
                continue;
            }
            float width = paint.measureText(mText.charAt(i) + "");
            canvas.drawText(mText.charAt(i) + "", 0, 1, offset, startY, paint);
            //canvas.drawRect(offset, startY * 1.2f - (1 - percent) * (upDistance + startY * 0.2f), offset + gaps[i], startY * 1.2f, backPaint);
            //在动画完成之后不需要画火花
            if (percent < 1) {
                drawSparkle(canvas, offset, startY - (1 - percent) * upDistance, width);
            }
            offset += gaps[i];
        }
        // draw old text
        float oldOffset = oldStartX;
        oldPaint.setTextSize(textSize);
        for (int i = 0; i < mOldText.length(); ++i) {
            oldPaint.setAlpha(255);
            int move = CharacterUtils.needMove(i, differentList);
            if (move != -1) {
                float p = percent * 7f;
                p = p > 1 ? 1 : p;
                float distX = CharacterUtils.getOffset(i, move, p, startX, oldStartX, gaps, oldGaps);
                canvas.drawText(mOldText.charAt(i) + "", 0, 1, distX, startY, oldPaint);
                oldOffset += oldGaps[i];
                continue;
            }
            float p = percent * 3.5f;
            p = p > 1 ? 1 : p;
            oldPaint.setAlpha((int) (255 * (1 - p)));
            canvas.drawText(mOldText.charAt(i) + "", 0, 1, oldOffset, startY, oldPaint);
            oldOffset += oldGaps[i];
        }
    }

    private void drawSparkle(Canvas canvas, float offset, float startY, float width) {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            Bitmap temp = getRandomSpark(random);
            canvas.drawBitmap(temp, (float) (offset + random.nextDouble() * width), (float) (startY - random.nextGaussian() * Math.sqrt(upDistance)), paint);
            temp.recycle();
        }
    }

    private Bitmap getRandomSpark(Random random) {
        int dstWidthAndHeight = random.nextInt(22) + 20;
        return Bitmap.createScaledBitmap(sparkBitmap, dstWidthAndHeight, dstWidthAndHeight, false);
    }

    private void calculate() {
        textSize = mHTextView.getTextSize();
        paint.setTextSize(textSize);
        oldPaint.setTextSize(textSize);

        for (int i = 0; i < mText.length(); i++) {
            gaps[i] = paint.measureText(mText.charAt(i) + "");
        }
        for (int i = 0; i < mOldText.length(); i++) {
            oldGaps[i] = oldPaint.measureText(mOldText.charAt(i) + "");
        }

        oldStartX = (mHTextView.getWidth() - oldPaint.measureText(mOldText.toString())) / 2f;

        startX = (mHTextView.getWidth() - paint.measureText(mText.toString())) / 2f;
        startY = (int) ((mHTextView.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        differentList.clear();
        differentList.addAll(CharacterUtils.diff(mOldText, mText));

        paint.getTextBounds(mText.toString(), 0, mText.length(), bounds);
        upDistance = bounds.height();
    }
}
