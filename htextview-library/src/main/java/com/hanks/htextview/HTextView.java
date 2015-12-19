package com.hanks.htextview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hanks.htextview.animatetext.AnvilText;
import com.hanks.htextview.animatetext.EvaporateText;
import com.hanks.htextview.animatetext.IHText;
import com.hanks.htextview.animatetext.FallText;
import com.hanks.htextview.animatetext.LineText;
import com.hanks.htextview.animatetext.PixelateText;
import com.hanks.htextview.animatetext.ScaleText;
import com.hanks.htextview.animatetext.SparkleText;

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

    public void setAnimateType(HTextViewType type) {
        switch (type){
            case SCALE:
                mIHText = new ScaleText();
                break;
            case EVAPORATE:
                mIHText = new EvaporateTlext();
                break;
            case FALL:
                mIHText = new FallText();
                break;
            case PIXELATE:
                mIHText = new PixelateText();
                break;
            case ANVIL:
                mIHText = new AnvilText();
                break;
            case SPARKLE:
                mIHText = new SparkleText();
                break;
            case LINE:
                mIHText = new LineText();
                break;
        }


        init(null,0);
    }
}
