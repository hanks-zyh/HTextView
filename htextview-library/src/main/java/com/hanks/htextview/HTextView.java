package com.hanks.htextview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hanks.htextview.animatetext.IHText;
import com.hanks.htextview.animatetext.FallText;

/**
 * Animate TextView
 */
public class HTextView extends TextView {

    private IHText mIHText = new FallText();

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
        mIHText.init(this, attrs, defStyle);
    }

    public void animateText(CharSequence text) {
        mIHText.animateText(text);
    }

    @Override protected void onDraw(Canvas canvas) {
        mIHText.onDraw(canvas);
    }

    public void reset(CharSequence text){
        mIHText.reset(text);
    }

    public void setAnimateType(IHText IHText) {
        this.mIHText = IHText;
        init(null,0);
    }
}
