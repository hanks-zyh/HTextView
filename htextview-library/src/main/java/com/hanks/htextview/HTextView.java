package com.hanks.htextview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hanks.htextview.animatetext.AnimateText;
import com.hanks.htextview.animatetext.ScaleText;
import com.hanks.htextview.animatetext.ShimmerViewBase;
import com.hanks.htextview.util.HTextViewHelper;

/**
 * Animate TextView
 */
public class HTextView extends TextView implements ShimmerViewBase {

    private AnimateText mAnimateText = new ScaleText();
    private HTextViewHelper shimmerViewHelper;
    //private AnimateText mAnimateText = new EvaporateText();
    //private AnimateText mAnimateText = new FallText();
    //private AnimateText mAnimateText = new PixelateText();
    //private AnimateText mAnimateText = new SparkleText();

    public void setAnimateType(AnimateText mAnimateText) {
        this.mAnimateText = mAnimateText;
        init(null,0);
    }

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
        shimmerViewHelper = new HTextViewHelper(this, getPaint(), attrs);
        shimmerViewHelper.setPrimaryColor(getCurrentTextColor());

    }

    public void animateText(CharSequence text) {
        mAnimateText.animateText(text);
    }

    @Override protected void onDraw(Canvas canvas) {
        if (shimmerViewHelper != null) {
            shimmerViewHelper.onDraw();
//            mAnimateText.onDraw(canvas);
        }
        super.onDraw(canvas);
    }

    public void reset(CharSequence text) {
        mAnimateText.reset(text);
    }



    @Override
    public float getGradientX() {
        return shimmerViewHelper.getGradientX();
    }

    @Override
    public void setGradientX(float gradientX) {   shimmerViewHelper.setGradientX(gradientX);
    }

    @Override
    public boolean isShimmering() {
        return shimmerViewHelper.isShimmering();
    }

    @Override
    public void setShimmering(boolean isShimmering) {
        shimmerViewHelper.setShimmering(isShimmering);
    }

    @Override
    public boolean isSetUp() {
        return shimmerViewHelper.isSetUp();
    }

    @Override
    public void setAnimationSetupCallback(HTextViewHelper.AnimationSetupCallback callback) {
        shimmerViewHelper.setAnimationSetupCallback(callback);
    }

    @Override
    public int getPrimaryColor() {
        return shimmerViewHelper.getPrimaryColor();
    }

    @Override
    public void setPrimaryColor(int primaryColor) {
        shimmerViewHelper.setPrimaryColor(primaryColor);
    }

    @Override
    public int getReflectionColor() {
        return shimmerViewHelper.getReflectionColor();
    }

    @Override
    public void setReflectionColor(int reflectionColor) {
        shimmerViewHelper.setReflectionColor(reflectionColor);
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        if (shimmerViewHelper != null) {
            shimmerViewHelper.setPrimaryColor(getCurrentTextColor());
        }
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(colors);
        if (shimmerViewHelper != null) {
            shimmerViewHelper.setPrimaryColor(getCurrentTextColor());
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (shimmerViewHelper != null) {
            shimmerViewHelper.onSizeChanged();
        }
    }

}
