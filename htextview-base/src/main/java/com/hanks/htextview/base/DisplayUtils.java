package com.hanks.htextview.base;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * util to get:
 * dp convert px
 * screen's width/height
 * Created by hanks on 15-12-19.
 */
public final class DisplayUtils {

    public static DisplayMetrics getDisplayMetrics() {
        return Resources.getSystem().getDisplayMetrics();
    }

    public static int dp2px(float dp) {
        return Math.round(dp * getDisplayMetrics().density);
    }

    public static int getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }

}
