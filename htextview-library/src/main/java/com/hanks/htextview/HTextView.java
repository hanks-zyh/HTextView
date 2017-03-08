package com.hanks.htextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hanks.htextview.animatetext.AnvilText;
import com.hanks.htextview.animatetext.EvaporateText;
import com.hanks.htextview.animatetext.FallText;
import com.hanks.htextview.animatetext.IHText;
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

    private IHText mIHText = new ScaleText();
    private AttributeSet attrs;
    private int defStyle;
    private int animateType;

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

        // Get the attributes array
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HTextView);
        animateType = typedArray.getInt(R.styleable.HTextView_animateType, 0);
        final String fontAsset = typedArray.getString(R.styleable.HTextView_fontAsset);

        if (!this.isInEditMode()) {
            // Set custom typeface
            if (fontAsset != null && !fontAsset.trim().isEmpty()) {
                setTypeface(Typeface.createFromAsset(getContext().getAssets(), fontAsset));
            }
        }


        switch (animateType) {
            case 0:
                mIHText = new ScaleText();
                break;
            case 1:
                mIHText = new EvaporateText();
                break;
            case 2:
                mIHText = new FallText();
                break;
            case 3:
                mIHText = new SparkleText();
                break;
            case 4:
                mIHText = new AnvilText();
                break;
            case 5:
                mIHText = new LineText();
                break;
            case 6:
                mIHText = new PixelateText();
                break;
            case 7:
                mIHText = new TyperText();
                break;
            case 8:
                mIHText = new RainBowText();
                break;
            //            <enum name="scale" value="0"/>
            //            <enum name="evaporate" value="1"/>
            //            <enum name="fall" value="2"/>
            //            <enum name="sparkle" value="3"/>
            //            <enum name="anvil" value="4"/>
            //            <enum name="line" value="5"/>
            //            <enum name="pixelate" value="6"/
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

    
    @Override
    public void setTextColor(@ColorInt int color) {
        //Check for SparkleText. Do not alter color if on that type due to paint conflicts
        if(animateType != 3){
            super.setTextColor(color);
        }
    }
    
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState state = new SavedState(superState);
        state.animateType = animateType;
        return state;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(state);
        animateType = ss.animateType;
    }

    public static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int animateType;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            animateType = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(animateType);
        }

        @Override
        public int describeContents() {
            return 0;
        }
    }

}
