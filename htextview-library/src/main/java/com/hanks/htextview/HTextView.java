package com.hanks.htextview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hanks.htextview.animatetext.AnimateText;
import com.hanks.htextview.animatetext.FallText;

/**
 * Animate TextView
 */
public class HTextView extends TextView {

    private AnimateText mAnimateText = new FallText();

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
        mAnimateText.init(this, attrs, defStyle);
    }

    public void animateText(CharSequence text) {
        mAnimateText.animateText(text);
    }

    @Override protected void onDraw(Canvas canvas) {
        mAnimateText.onDraw(canvas);
    }

    public void reset(CharSequence text){
        mAnimateText.reset(text);
    }

    public void setAnimateType(AnimateText animateText) {
        this.mAnimateText = animateText;
        init(null,0);
    }
}
