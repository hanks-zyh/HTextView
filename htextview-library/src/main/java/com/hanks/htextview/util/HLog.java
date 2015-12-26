package com.hanks.htextview.util;
import android.util.Log;

import com.hanks.htextview.BuildConfig;

/**
 * Created by hanks on 15-12-14.
 */
public class HLog {
    public static void i(Object s){
        if(BuildConfig.DEBUG) {
            Log.i("HLog", s.toString());
        }
    }
}
