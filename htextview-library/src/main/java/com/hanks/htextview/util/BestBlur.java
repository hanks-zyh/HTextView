package com.hanks.htextview.util;

/*
 * Copyright 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.Matrix3f;
import android.renderscript.RSInvalidStateException;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.ScriptIntrinsicColorMatrix;

/**
 * Blur的工具类
 * Created by hanks on 15-12-14.
 */

public class BestBlur {

    public static final int MAX_SUPPORTED_BLUR_PIXELS = 25;
    //缩放系数
    public final static int SCALE = 8;

    private RenderScript mRS;
    private ScriptIntrinsicBlur mSIBlur;
    private ScriptIntrinsicColorMatrix mSIGrey;
    private Allocation mTmp1;
    private Allocation mTmp2;

    public BestBlur(Context context) {
        mRS = RenderScript.create(context);
        mSIBlur = ScriptIntrinsicBlur.create(mRS, Element.U8_4(mRS));
        mSIGrey = ScriptIntrinsicColorMatrix.create(mRS, Element.U8_4(mRS));
    }

    public Bitmap blurBitmap(Bitmap src, float radius, float desaturateAmount) {
        if (src == null) {
            return null;
        }

        Bitmap dest = src.copy(src.getConfig(), false);
        if (radius == 0f && desaturateAmount == 0f) {
            return dest;
        }

        if (mTmp1 != null) {
            mTmp1.destroy();
        }
        if (mTmp2 != null) {
            try {
                mTmp2.destroy();
            } catch (RSInvalidStateException e) {
                // Ignore 'Object already destroyed' exceptions
            }
        }

        mTmp1 = Allocation.createFromBitmap(mRS, src);
        mTmp2 = Allocation.createFromBitmap(mRS, dest);

        if (radius > 0f && desaturateAmount > 0f) {
            doBlur(radius, mTmp1, mTmp2);
            doDesaturate(MathUtils.constrain(0, 1, desaturateAmount), mTmp2, mTmp1);
            mTmp1.copyTo(dest);
        } else if (radius > 0f) {
            doBlur(radius, mTmp1, mTmp2);
            mTmp2.copyTo(dest);
        } else {
            doDesaturate(MathUtils.constrain(0, 1, desaturateAmount), mTmp1, mTmp2);
            mTmp2.copyTo(dest);
        }
        return dest;
    }

    private void doBlur(float amount, Allocation input, Allocation output) {
        mSIBlur.setRadius(amount);
        mSIBlur.setInput(input);
        mSIBlur.forEach(output);
    }

    private void doDesaturate(float normalizedAmount, Allocation input, Allocation output) {
        Matrix3f m = new Matrix3f(new float[]{MathUtils.interpolate(1, 0.299f, normalizedAmount), MathUtils.interpolate(0, 0.299f, normalizedAmount), MathUtils.interpolate(0, 0.299f, normalizedAmount),

                MathUtils.interpolate(0, 0.587f, normalizedAmount), MathUtils.interpolate(1, 0.587f, normalizedAmount), MathUtils.interpolate(0, 0.587f, normalizedAmount),

                MathUtils.interpolate(0, 0.114f, normalizedAmount), MathUtils.interpolate(0, 0.114f, normalizedAmount), MathUtils.interpolate(1, 0.114f, normalizedAmount),});
        mSIGrey.setColorMatrix(m);
        mSIGrey.forEach(input, output);
    }

    public static Bitmap fastBlur(Bitmap sbitmap, float radiusf) {
        Bitmap bitmap = Bitmap.createScaledBitmap(sbitmap, sbitmap.getWidth() / SCALE, sbitmap.getHeight() / SCALE, false);//先缩放图片，增加模糊速度
        int radius = (int) radiusf;
        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);

    }

    public void destroy() {
        mSIBlur.destroy();
        if (mTmp1 != null) {
            mTmp1.destroy();
        }
        if (mTmp2 != null) {
            mTmp2.destroy();
        }
        mRS.destroy();
    }
}

