package com.hanks.htextview.animatetext;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;

import com.hanks.htextview.util.DisplayUtils;

/**
 * from http://wuxiaolong.me/2015/11/16/LinearGradientTextView/
 * Created by hanks on 15/12/26.
 */
public class RainBowText extends IHTextImpl {
    private int mTextWidth;
    private LinearGradient mLinearGradient;
    private Matrix mMatrix;
    private float mTranslate;
    private int dx;

    @Override
    protected void initVariables() {
        mMatrix = new Matrix();
        dx = DisplayUtils.dp2Px(7);
    }

    @Override
    protected void animateStart( ) {
        mHTextView.invalidate();
    }

    @Override
    protected void animatePrepare( ) {
        mTextWidth = (int) mPaint.measureText(mText, 0, mText.length());
        mTextWidth = Math.max(DisplayUtils.dp2Px(100), mTextWidth);
        if (mTextWidth > 0) {
            mLinearGradient = new LinearGradient(0, 0, mTextWidth, 0,
                    new int[]{0xFFFF2B22, 0xFFFF7F22, 0xFFEDFF22, 0xFF22FF22, 0xFF22F4FF, 0xFF2239FF, 0xFF5400F7}, null, Shader.TileMode.MIRROR);
            mPaint.setShader(mLinearGradient);
        }
    }

    @Override
    protected void drawFrame(Canvas canvas) {
        if (mMatrix != null) {
            mTranslate += dx;
            if (mTranslate > 2 * mTextWidth) {
                mTranslate = -mTextWidth;
            }
            mMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mMatrix);
            canvas.drawText(mText, 0, mText.length(), startX, startY, mPaint);
            mHTextView.postInvalidateDelayed(100);
        }
    }
}
