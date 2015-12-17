package com.hanks.htextview.animatetext;
import android.graphics.Canvas;

import com.hanks.htextview.HTextView;
/**
 * interface used in HTextView
 * Created by hanks on 15-12-14.
 */
public interface AnimateText {
    void init(HTextView hTextView);
    void animateText(CharSequence text);
    void onDraw(Canvas canvas);
    void reset(CharSequence text);
}
