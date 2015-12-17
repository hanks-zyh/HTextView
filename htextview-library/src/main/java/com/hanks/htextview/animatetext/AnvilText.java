package com.hanks.htextview.animatetext;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.animation.BounceInterpolator;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.R;
import com.hanks.htextview.util.CharacterUtils;
import com.hanks.htextview.util.HLog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
/**
 * keynote 轰然坠落效果
 * Created by hanks on 15-12-14.
 */
public class AnvilText implements AnimateText {

    Paint paint, oldPaint, bitmapPaint;

    float ANIMA_DURATION = 800; // 每个字符动画时间 500ms

    HTextView mHTextView;
    float    upDistance = 0;
    Bitmap[] smokes     = new Bitmap[50];

    float progress = 0;

    private float[] gaps    = new float[100];
    private float[] oldGaps = new float[100];

    private float textSize;

    private CharSequence mText;
    private CharSequence mOldText;

    private List<CharacterDiffResult> differentList = new ArrayList<>();

    private float oldStartX = 0;
    private float startX    = 0;
    private float startY    = 0;
    private int textWidth;

    public void init(HTextView hTextView) {
        mHTextView = hTextView;

        mText = mHTextView.getText();
        mOldText = mHTextView.getText();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        oldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oldPaint.setColor(Color.WHITE);
        oldPaint.setStyle(Paint.Style.FILL);

        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapPaint.setColor(Color.WHITE);
        bitmapPaint.setStyle(Paint.Style.FILL);

        textSize = hTextView.getTextSize();

        try {
            R.drawable d = new R.drawable();
            for (int j = 0; j < 50; j++) {
                String drawable = "";
                if (j < 10) {
                    drawable = "wenzi000" + j;
                } else {
                    drawable = "wenzi00" + j;
                }
                Field fieldImgId = d.getClass().getDeclaredField(drawable);
                int imgId = (Integer) fieldImgId.get(d);//这个ID就是每个图片资源ID
                smokes[j] = BitmapFactory.decodeResource(hTextView.getResources(), imgId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1)
                .setDuration((long) ANIMA_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                mHTextView.invalidate();
            }
        });
        valueAnimator.start();

        for (int i = 0; i < smokes.length; i++) {
            Bitmap smoke = smokes[i];

            int dstWidth = (int) (textWidth * 1.5f);
            if(dstWidth<400) dstWidth = 400;
            int dstHeight = (int) (smoke.getHeight() * 1f / smoke.getWidth() * dstWidth);
            smokes[i] = Bitmap.createScaledBitmap(smoke, dstWidth, dstHeight, false);
            smoke.recycle();
        }
    }

    @Override public void onDraw(Canvas canvas) {
        float offset = startX;
        float oldOffset = oldStartX;

        int maxLength = Math.max(mText.length(), mOldText.length());

        float percent = progress; // 动画进行的百分比 0~1

        HLog.i("动画进度:" + percent);

        boolean showSmoke = false;
        for (int i = 0; i < maxLength; i++) {

            // draw old text
            if (i < mOldText.length()) {

                oldPaint.setTextSize(textSize);
                int move = CharacterUtils.needMove(i, differentList);
                if (move != -1) {
                    oldPaint.setAlpha(255);
                    float p = percent * 2f;
                    p = p > 1 ? 1 : p;
                    float distX = CharacterUtils.getOffset(i, move, p, startX, oldStartX, gaps, oldGaps);
                    canvas.drawText(mOldText.charAt(i) + "", 0, 1, distX, startY, oldPaint);
                } else {

                    float p = percent * 2f;
                    p = p > 1 ? 1 : p;
                    oldPaint.setAlpha((int) ((1 - p) * 255));
                    canvas.drawText(mOldText.charAt(i) + "", 0, 1, oldOffset, startY, oldPaint);
                }
                oldOffset += oldGaps[i];
            }

            // draw new text
            if (i < mText.length()) {

                if (!CharacterUtils.stayHere(i, differentList)) {

                    showSmoke = true;
                    float interpolation = new BounceInterpolator().getInterpolation(percent);

                    paint.setAlpha(255);
                    paint.setTextSize(textSize);

                    float y = startY - (1 - interpolation) * upDistance * 2;

                    float width = paint.measureText(mText.charAt(i) + "");
                    canvas.drawText(mText.charAt(i) + "", 0, 1, offset + (gaps[i] - width) / 2, y, paint);
                }

                offset += gaps[i];
            }
        }

        if (percent > 0.3 && percent < 1) {
            if (showSmoke) {
                drawSmokes(canvas, startX + (offset - startX) / 2f, startY - 50, paint, percent);
            }
        }
    }

    /**
     * 烟雾, 从中间向外扩散
     *
     * @param canvas 画布
     * @param x      中心点x坐标
     * @param y      中心点Y坐标
     * @param paint  画笔
     */
    private void drawSmokes(Canvas canvas, float x, float y, Paint paint, float percent) {
        Bitmap b = smokes[0];
        try {
            int index = (int) (50 * percent);
            if (index < 0) index = 0;
            if (index >= 50) index = 49;
            b = smokes[index];
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (b != null) {
            int w = b.getWidth();
            int h = b.getHeight();
            canvas.drawBitmap(b, x - w / 2, y - h / 2, bitmapPaint);
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
        textWidth = bounds.width();
    }

}
