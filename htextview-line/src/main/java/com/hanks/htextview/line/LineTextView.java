package com.hanks.htextview.line;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.hanks.htextview.base.HTextView;


/**
 * line effect view
 * Created by hanks on 2017/3/13.
 */

public class LineTextView extends HTextView {

    private LineText lineText;

    public LineTextView(Context context) {
        this(context, null);
    }

    public LineTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public void setLineColor(@ColorInt int color) {
        lineText.setLineColor(color);
    }

    public float getLineWidth() {
        return lineText.getLineWidth();
    }

    public void setLineWidth(float lineWidth) {
        lineText.setLineWidth(lineWidth);
    }

    public float getAnimationDuration() {
        return lineText.getAnimationDuration();
    }

    public void setAnimationDuration(float animationDuration) {
        lineText.setAnimationDuration(animationDuration);
    }

    @Override
    public void setProgress(float progress) {
        lineText.setProgress(progress);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        lineText = new LineText();
        lineText.init(this, attrs, defStyleAttr);
    }

    @Override
    public void animateText(CharSequence text) {
        lineText.animateText(text);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        lineText.onDraw(canvas);
    }

}
