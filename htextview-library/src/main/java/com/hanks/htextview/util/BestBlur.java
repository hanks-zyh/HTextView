package com.hanks.htextview.util;
/**
 * Created by hanks on 15-12-14.
 */
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

public class BestBlur {

    public static final int MAX_SUPPORTED_BLUR_PIXELS = 25;

    private RenderScript               mRS;
    private ScriptIntrinsicBlur        mSIBlur;
    private ScriptIntrinsicColorMatrix mSIGrey;
    private Allocation                 mTmp1;
    private Allocation                 mTmp2;

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
            doDesaturate(MathUtil.constrain(0, 1, desaturateAmount), mTmp2, mTmp1);
            mTmp1.copyTo(dest);
        } else if (radius > 0f) {
            doBlur(radius, mTmp1, mTmp2);
            mTmp2.copyTo(dest);
        } else {
            doDesaturate(MathUtil.constrain(0, 1, desaturateAmount), mTmp1, mTmp2);
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
        Matrix3f m = new Matrix3f(new float[]{MathUtil.interpolate(1, 0.299f, normalizedAmount), MathUtil.interpolate(0, 0.299f, normalizedAmount), MathUtil.interpolate(0, 0.299f, normalizedAmount),

                MathUtil.interpolate(0, 0.587f, normalizedAmount), MathUtil.interpolate(1, 0.587f, normalizedAmount), MathUtil.interpolate(0, 0.587f, normalizedAmount),

                MathUtil.interpolate(0, 0.114f, normalizedAmount), MathUtil.interpolate(0, 0.114f, normalizedAmount), MathUtil.interpolate(1, 0.114f, normalizedAmount),});
        mSIGrey.setColorMatrix(m);
        mSIGrey.forEach(input, output);
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

