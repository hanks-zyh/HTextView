package com.hanks.htextview;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
/**
 *
 * Created by hanks on 15-12-14.
 */
public interface AnimateText {
    void init(HTextView hTextView);
    void animateText(CharSequence text);
    void onDraw(Canvas canvas);
    void reset(CharSequence text);
}
