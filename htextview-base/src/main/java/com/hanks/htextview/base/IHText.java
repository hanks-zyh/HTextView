package com.hanks.htextview.base;

import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * interface used in HTextView
 * Created by hanks on 15-12-14.
 */
public interface IHText {
    void init(HTextView hTextView, AttributeSet attrs, int defStyle);

    void animateText(CharSequence text);

    void onDraw(Canvas canvas);

    void setAnimationListener(AnimationListener listener);
}
