package com.hanks.htextview.animatetext;

import android.graphics.Canvas;

/**
 * 打字机效果
 * Created by hanks on 15/12/26.
 */
public class TyperText extends IHTextImpl {
    private int currentLength;

    @Override
    protected void initVariables() {

    }

    @Override
    protected void animateStart( ) {
        currentLength = 0;
        mHTextView.invalidate();
    }

    @Override
    protected void animatePrepare( ) {

    }

    @Override
    protected void drawFrame(Canvas canvas) {

        canvas.drawText(mText, 0, currentLength, startX, startY, mPaint);
        if (currentLength < mText.length()) {
            currentLength++;
            mHTextView.postInvalidateDelayed(100);
        }
    }
}
