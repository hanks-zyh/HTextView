package xyz.hanks.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Base TextView
 * Created by hanks on 2017/3/13.
 */

public abstract class HTextView extends android.support.v7.widget.AppCompatTextView {
    public HTextView(Context context) {
        this(context,null);
    }

    public HTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected abstract void setProgress(float progress);
}
