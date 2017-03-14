package com.hanks.htextview.line;

import android.animation.ValueAnimator;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.hanks.htextview.base.DisplayUtils;
import com.hanks.htextview.base.HText;
import com.hanks.htextview.base.HTextView;


/**
 * line effect
 * Created by hanks on 2017/3/13.
 */

public class LineText extends HText {

    public static final int DEFAULT_DURATION = 800;
    public static final int DEFAULT_LINE_WIDTH = DisplayUtils.dp2px(3f);
    PointF p1 = new PointF();
    PointF p2 = new PointF();
    PointF p3 = new PointF();
    PointF p4 = new PointF();
    PointF p5 = new PointF();
    PointF p6 = new PointF();
    PointF p7 = new PointF();
    PointF p8 = new PointF();
    PointF pA = new PointF();
    PointF pB = new PointF();
    PointF pC = new PointF();
    PointF pD = new PointF();
    private float animationDuration;
    private float lineWidth;
    private int lineColor;
    private Paint mLinePaint;

    public void setLineColor(int color) {
        this.lineColor = color;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public float getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(float animationDuration) {
        this.animationDuration = animationDuration;
    }

    @Override
    public void init(HTextView hTextView, AttributeSet attrs, int defStyle) {
        super.init(hTextView, attrs, defStyle);
        TypedArray typedArray = hTextView.getContext().obtainStyledAttributes(attrs, R.styleable.LineTextView);
        animationDuration = typedArray.getInt(R.styleable.LineTextView_animationDuration, DEFAULT_DURATION);
        lineColor = typedArray.getColor(R.styleable.LineTextView_lineColor, hTextView.getCurrentTextColor());
        lineWidth = typedArray.getDimension(R.styleable.LineTextView_lineWidth, DEFAULT_LINE_WIDTH);
        typedArray.recycle();
    }

    @Override
    protected void initVariables() {
        lineWidth = DEFAULT_LINE_WIDTH;
        animationDuration = DEFAULT_DURATION;

        mLinePaint = new Paint(lineColor);
        mLinePaint.setStrokeWidth(lineWidth);
    }

    @Override
    protected void animateStart(CharSequence text) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1)
                .setDuration((long) animationDuration);
        valueAnimator.setInterpolator(new LinearInterpolator());
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
    protected void animatePrepare(CharSequence text) {

    }

    @Override
    protected void drawFrame(Canvas canvas) {
        float percent = progress;
        float percent2 = (float) (Math.sqrt(3.38f - (percent - 1.7f) * (percent - 1.7f)) - 0.7);

        int width = mHTextView.getWidth();
        int height = mHTextView.getHeight();

        pA.x = 0;
        pA.y = 0;

        pB.x = width;
        pB.y = 0;

        pC.x = width;
        pC.y = height;

        pD.x = 0;
        pD.y = height;

        p1.x = width * percent2;
        p1.y = pB.y;
        drawLine(canvas, p1, pB);

        p2.x = pB.x;
        p2.y = height * percent;
        drawLine(canvas, pB, p2);

        p3.x = width;
        p3.y = height * percent2;
        drawLine(canvas, p3, pC);

        p4.x = width * (1 - percent);
        p4.y = height;
        drawLine(canvas, pC, p4);

        p5.x = width * (1 - percent2);
        p5.y = height;
        drawLine(canvas, p5, pD);

        p6.x = 0;
        p6.y = height * (1 - percent);
        drawLine(canvas, pD, p6);

        p7.x = 0;
        p7.y = height * (1 - percent2);
        drawLine(canvas, p7, pA);

        p8.x = width * percent;
        p8.y = 0;
        drawLine(canvas, pA, p8);
    }

    private void drawLine(Canvas canvas, PointF a, PointF b) {
        canvas.drawLine(a.x, a.y, b.x, b.y, mLinePaint);
    }
}
