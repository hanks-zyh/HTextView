package com.hanks.htextview.scale;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.hanks.htextview.base.AnimationListener;
import com.hanks.htextview.base.HTextView;

/**
 * ScaleTextView
 * Created by hanks on 2017/3/15.
 */

public class ScaleTextView extends HTextView {

    private ScaleText scaleText;

    public ScaleTextView(Context context) {
        this(context, null);
    }

    public ScaleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scaleText = new ScaleText();
        scaleText.init(this, attrs, defStyleAttr);
        setMaxLines(1);
        setEllipsize(TextUtils.TruncateAt.END);
    }

    @Override
    public void setAnimationListener(AnimationListener listener) {
        scaleText.setAnimationListener(listener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        scaleText.onDraw(canvas);
    }

    @Override
    public void setProgress(float progress) {
        scaleText.setProgress(progress);
    }

    @Override
    public void animateText(CharSequence text) {
        scaleText.animateText(text);
    }
}
