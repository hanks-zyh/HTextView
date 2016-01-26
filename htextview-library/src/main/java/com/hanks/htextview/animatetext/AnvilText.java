package com.hanks.htextview.animatetext;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.animation.BounceInterpolator;

import com.hanks.htextview.R;
import com.hanks.htextview.animatetext.base.IHTextImpl;
import com.hanks.htextview.util.CharacterUtils;
import com.hanks.htextview.util.MathUtils;

import java.lang.reflect.Field;

/**
 * keynote 轰然坠落效果
 * Created by hanks on 15-12-14.
 */
public class AnvilText extends IHTextImpl {

    private Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap[] smokes = new Bitmap[50];
    // 每个字符动画时间 800ms
    public static final long ANIMATE_DURATION = 800;
    private int mTextHeight;
    private int mTextWidth;
    private float progress;
    private Rect bounds = new Rect();

    {
        bitmapPaint.setColor(Color.WHITE);
        bitmapPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void initVariables() {
        //通过反射获取到图片资源
        try {
            R.drawable d = new R.drawable();
            for (int j = 0; j < 50; j++) {
                String drawableName = j < 10 ? "wenzi000" + j : "wenzi00" + j;
                Field fieldImgId = d.getClass().getDeclaredField(drawableName);
                //这个ID就是每个图片资源ID
                int imgId = (Integer) fieldImgId.get(d);
                smokes[j] = BitmapFactory.decodeResource(mHTextView.getResources(), imgId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void animateStart() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(ANIMATE_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (float) animation.getAnimatedValue();
                mHTextView.invalidate();
            }
        });
        valueAnimator.start();
        for (int i = 0; i < smokes.length; i++) {
            //这里必须有一个Temp，要不然会有Bitmap内存泄漏
            Bitmap temp = smokes[i];
            //把原来的图片按原长宽比例扩大1.5倍，限制扩大后最大宽度为400px
            int dstWidth = (int) (mTextWidth * 1.5f);
            if (dstWidth < 400) dstWidth = 400;
            int dstHeight = (int) ((float) temp.getHeight() / temp.getWidth() * dstWidth);
            smokes[i] = Bitmap.createScaledBitmap(temp, dstWidth, dstHeight, false);
            temp.recycle();
        }
        System.gc();
    }

    @Override
    protected void animatePrepare() {
        //获取画出新的Text需要的最小矩形，保存在bounds中
        mPaint.getTextBounds(mText.toString(), 0, mText.length(), bounds);
        mTextHeight = bounds.height();
        mTextWidth = bounds.width();
    }

    @Override
    protected void drawFrame(Canvas canvas) {
        // draw old text
        //Text的偏移，初始化为原来的Text的X
        float oldOffset = oldStartX;
        mOldPaint.setTextSize(mTextSize);
        for (int i = 0; i < mOldText.length(); ++i) {
            int move = CharacterUtils.needMove(i, differentList);
            float progress2X = progress > 0.5f ? 1 : progress * 2f;
            // 新的Text里有这个字符，将这个字符移动到新的位置
            if (move != CharacterUtils.NEED_TO_DISCUSS) {
                //旧的Text需要在一半的时间内走完动画
                float distX = CharacterUtils.getOffset(i, move, progress2X, startX, oldStartX, gaps, oldGaps);
                mOldPaint.setAlpha(255);
                canvas.drawText(mOldText.charAt(i) + "", 0, 1, distX, startY, mOldPaint);
                oldOffset += oldGaps[i];
                continue;
            }
            //新的Text里没有这个字符，需要淡出，在一半的时间里淡出完毕
            mOldPaint.setAlpha((int) ((1 - progress2X) * 255));
            canvas.drawText(mOldText.charAt(i) + "", 0, 1, oldOffset, startY, mOldPaint);
            oldOffset += oldGaps[i];
        }
        //draw new text
        //新的Text的偏移，初始化为原来的Text的X
        float offset = startX;
        boolean showSmoke = false;
        mPaint.setAlpha(255);
        mPaint.setTextSize(mTextSize);
        for (int i = 0; i < mText.length(); ++i) {
            //这个字符不需要从上面轰下来
            if (CharacterUtils.stayHere(i, differentList)) {
                offset += gaps[i];
                continue;
            }
            //需要轰下来，同时也有烟雾效果
            showSmoke = true;
            //根据现在的进度计算插值
            float interpolation = new BounceInterpolator().getInterpolation(progress);
            //计算在y轴的位置
            float y = startY - (1 - interpolation) * mTextHeight * 2;
            float width = mPaint.measureText(mText.charAt(i) + "");
            canvas.drawText(mText.charAt(i) + "", 0, 1, offset + (gaps[i] - width) / 2, y, mPaint);
            offset += gaps[i];
        }
        if (progress > 0.3 && progress < 1 && showSmoke) {
            drawSmokes(canvas, startX + (offset - startX) / 2f, startY - 50, progress);
        }
    }

    /**
     * @param canvas 画布
     * @param x      中心点x坐标
     * @param y      中心点Y坐标
     */
    private void drawSmokes(Canvas canvas, float x, float y, float percent) {
        int index = MathUtils.constrain(0, 49, (int) (50 * percent));
        if (smokes[index] != null) {
            float left = x - smokes[index].getWidth() / 2;
            float top = y - smokes[index].getHeight() / 2;
            canvas.drawBitmap(smokes[index], left, top, bitmapPaint);
        }
    }
}
