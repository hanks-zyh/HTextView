package com.hanks.htextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hanks.htextview.animatetext.AnvilText;
import com.hanks.htextview.animatetext.EvaporateText;
import com.hanks.htextview.animatetext.FallText;
import com.hanks.htextview.animatetext.base.IHText;
import com.hanks.htextview.animatetext.LineText;
import com.hanks.htextview.animatetext.PixelateText;
import com.hanks.htextview.animatetext.RainBowText;
import com.hanks.htextview.animatetext.ScaleText;
import com.hanks.htextview.animatetext.SparkleText;
import com.hanks.htextview.animatetext.TyperText;

/**
 * Animate TextView
 */
public class HTextView extends TextView {

    private static final int SCALE = 0;
    private static final int EVAPORATE = 1;
    private static final int FALL = 2;
    private static final int SPARKLE = 3;
    private static final int ANVIL = 4;
    private static final int LINE = 5;
    private static final int PIXELATE = 6;
    private static final int TYPER = 7;
    private static final int RAINBOW = 8;

    private IHText mIHText = new ScaleText();
    private AttributeSet attrs;
    private int defStyle;

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
        this.attrs = attrs;
        this.defStyle = defStyle;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HTextView);
        int animateType = typedArray.getInt(R.styleable.HTextView_animateType, SCALE);
        switch (animateType) {
            case SCALE:
                mIHText = new ScaleText();
                break;
            case EVAPORATE:
                mIHText = new EvaporateText();
                break;
            case FALL:
                mIHText = new FallText();
                break;
            case SPARKLE:
                mIHText = new SparkleText();
                break;
            case ANVIL:
                mIHText = new AnvilText();
                break;
            case LINE:
                mIHText = new LineText();
                break;
            case PIXELATE:
                mIHText = new PixelateText();
                break;
            case TYPER:
                mIHText = new TyperText();
                break;
            case RAINBOW:
                mIHText = new RainBowText();
                break;
        }
        typedArray.recycle();
        initHText(attrs, defStyle);
    }

    private void initHText(AttributeSet attrs, int defStyle) {
        mIHText.init(this, attrs, defStyle);
    }


    public void animateText(CharSequence text) {
        mIHText.animateText(text);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mIHText.onDraw(canvas);
    }

    public void reset(CharSequence text) {
        mIHText.reset(text);
    }

    public void setAnimateType(HTextViewType type) {
        switch (type) {
            case SCALE:
                mIHText = new ScaleText();
                break;
            case EVAPORATE:
                mIHText = new EvaporateText();
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
            case TYPER:
                mIHText = new TyperText();
                break;
            case RAINBOW:
                mIHText = new RainBowText();
                break;
        }
        initHText(attrs, defStyle);
    }
}
