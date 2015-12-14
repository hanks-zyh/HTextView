package com.hanks.htextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Animate TextView
 */
public class HTextView extends TextView {

    //private AnimateText mAnimateText = new ScaleText();
    private AnimateText mAnimateText = new EvaporateText();
    //private AnimateText mAnimateText = new FallText();
    //private AnimateText mAnimateText = new PixelateText();

    public HTextView(Context context) {
        super(context);
        init(null, 0);
    }

    public HTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public HTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.HTextView, defStyle, 0);

        a.recycle();

        mAnimateText.init(this);

    }

    public void animateText(CharSequence text) {
        mAnimateText.animateText(text);
    }

    @Override protected void onDraw(Canvas canvas) {
        mAnimateText.onDraw(canvas);
    }

    public void reset(CharSequence text) {
        mAnimateText.reset(text);
    }

}
