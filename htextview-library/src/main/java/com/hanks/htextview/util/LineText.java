package com.hanks.htextview.util;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.hanks.htextview.AnimateText;
import com.hanks.htextview.CharacterDiffResult;
import com.hanks.htextview.HTextView;

import java.util.ArrayList;
import java.util.List;
/**
 * 线条边框流动
 * Created by hanks on 15-12-17.
 */
public class LineText implements AnimateText {

    float progress       = 0;
    float ANIMA_DURATION = 1000;

    Paint paint, oldPaint, sparkPaint;

    HTextView mHTextView;

    float upDistance = 0;

    int textColor = Color.WHITE;
    Paint linePaint;
    float padding = 20;
    float gap     = 0;
    PointF p1 = new PointF();
    PointF p2 = new PointF();
    PointF p3 = new PointF();
    PointF p4 = new PointF();
    PointF p5 = new PointF();
    PointF p6 = new PointF();
    PointF p7 = new PointF();
    PointF p8 = new PointF();
    int xLineLength = 0;
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
    private float distWidth;
    private float distHeight;

    private float yLineLength;
    private float lineWidth = 2;

    private int yLineShort;
    private int xLineShort;

    public void init(HTextView hTextView) {
        mHTextView = hTextView;

        mText = "";
        mOldText = "";

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(textColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(lineWidth);

        oldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oldPaint.setColor(textColor);
        oldPaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(((ColorDrawable) mHTextView.getBackground()).getColor());
        linePaint.setStyle(Paint.Style.FILL);

        metrics = new DisplayMetrics();
        WindowManager windowManger = (WindowManager) hTextView.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManger.getDefaultDisplay().getMetrics(metrics);

        textSize = hTextView.getTextSize();
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

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1)
                .setDuration((long) ANIMA_DURATION);
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
        float percent = progress;
        paint.setAlpha(255);
        paint.setTextSize(textSize);

        xLineLength = (int) (mHTextView.getWidth() - (mHTextView.getWidth() - distWidth + gap) * percent);
        yLineLength = (int) (mHTextView.getHeight() - (mHTextView.getHeight() - distHeight + gap) * percent);

        p1.x = (mHTextView.getWidth() / 2 + distWidth / 2 - gap) * percent;
        p1.y = (mHTextView.getHeight() - distHeight) / 2;
        canvas.drawLine(p1.x - xLineLength, p1.y, p1.x, p1.y, paint);

        p2.x = (mHTextView.getWidth() / 2 + distWidth / 2);
        p2.y = (mHTextView.getHeight() / 2 + distHeight / 2 - gap) * percent;
        canvas.drawLine(p2.x, p2.y - yLineLength, p2.x, p2.y, paint);

        p3.x = mHTextView.getWidth() - (mHTextView.getWidth() / 2 + distWidth / 2 - gap) * percent;
        p3.y = (mHTextView.getHeight() + distHeight) / 2;
        canvas.drawLine(p3.x + xLineLength, p3.y, p3.x, p3.y, paint);

        p4.x = (mHTextView.getWidth() / 2 - distWidth / 2);
        p4.y = mHTextView.getHeight() - (mHTextView.getHeight() / 2 + distHeight / 2 - gap) * percent;
        canvas.drawLine(p4.x, p4.y + yLineLength, p4.x, p4.y, paint);



        xLineShort = (int) ( (distWidth + gap) * (1-percent));
        yLineShort = (int) ( (distHeight + gap) * (1-percent));


        p5.x = (mHTextView.getWidth() / 2 + distWidth / 2 ) ;
        p5.y = (mHTextView.getHeight() - distHeight) / 2;
        canvas.drawLine(p5.x - xLineShort, p5.y, p5.x, p5.y, paint);

        p6.x = (mHTextView.getWidth() / 2 + distWidth / 2);
        p6.y = (mHTextView.getHeight() / 2 + distHeight / 2) ;
        canvas.drawLine(p6.x, p6.y - yLineShort, p6.x, p6.y, paint);

        p7.x = mHTextView.getWidth() - (mHTextView.getWidth() / 2 + distWidth / 2 - gap);
        p7.y = (mHTextView.getHeight() + distHeight) / 2;
        canvas.drawLine(p7.x + xLineShort, p7.y, p7.x, p7.y, paint);

        p8.x = (mHTextView.getWidth() / 2 - distWidth / 2);
        p8.y = mHTextView.getHeight() - (mHTextView.getHeight() / 2 + distHeight / 2 - gap) ;
        canvas.drawLine(p8.x, p8.y + yLineShort, p8.x, p8.y, paint);

        canvas.drawText(mText, 0, mText.length(), offset, startY, paint);

        for (int i = 0; i < mText.length(); i++) {
            offset += gaps[i];
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

        distWidth = bounds.width() + padding * 2;
        distHeight = bounds.height() + padding * 2;

        xLineLength = mHTextView.getWidth();
        yLineLength = mHTextView.getHeight();

    }

}
