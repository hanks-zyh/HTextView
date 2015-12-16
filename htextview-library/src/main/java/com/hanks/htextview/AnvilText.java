package com.hanks.htextview;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;

import com.hanks.htextview.util.CharacterUtils;
import com.hanks.htextview.util.HLog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * 蒸发效果
 * Created by hanks on 15-12-14.
 */
public class AnvilText implements AnimateText {

    Paint paint, oldPaint,bitmapPaint;
    float charTime  = 500; // 每个字符动画时间 500ms
    int   mostCount = 20; // 最多10个字符同时动画
    HTextView mHTextView;
    float upDistance = 0;
    private float[] gaps    = new float[100];
    private float[] oldGaps = new float[100];
    private DisplayMetrics metrics;
    private float          textSize;
    private CharSequence   mText;
    private CharSequence   mOldText;
    private List<CharacterDiffResult> differentList = new ArrayList<>();
    private float                     oldStartX     = 0;
    private float                     startX        = 0;
    private float                     startY        = 0;
    private Bitmap smokeBitmap;


    Bitmap[] smokes = new Bitmap[50];

    public void init(HTextView hTextView) {
        mHTextView = hTextView;

        mText = "";
        mOldText = "";

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        oldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oldPaint.setColor(Color.WHITE);
        oldPaint.setStyle(Paint.Style.FILL);

        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapPaint.setColor(Color.WHITE);
        bitmapPaint.setStyle(Paint.Style.FILL);

        metrics = new DisplayMetrics();
        WindowManager windowManger = (WindowManager) hTextView.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManger.getDefaultDisplay().getMetrics(metrics);

        textSize = hTextView.getTextSize();

        smokeBitmap = BitmapFactory.decodeResource(mHTextView.getResources(), R.drawable.smoke);


        try {
            String pp = "";
            R.drawable d = new R.drawable();
            for (int j = 0; j < 50; j++) {
                if (j < 10) {
                    pp = "0";
                } else {
                    pp = "";
                }
                Field fieldimgId = d.getClass().getDeclaredField("wenzi00"+ pp + j);
                int imgId = (Integer) fieldimgId.get(d);//这个ID就是每个图片资源ID
                smokes[j] = BitmapFactory.decodeResource(hTextView.getResources(), imgId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override public void reset(CharSequence text) {
        progress = 1;
        calc();
        mHTextView.invalidate();
    }

    @Override public void animateText(CharSequence text) {
        mOldText = mText;
        mText = text;

        calc();

        int n = mText.length();
        n = n <= 0 ? 1 : n;

        // 计算动画总时间
//        long duration = (long) (charTime + charTime / mostCount * (n - 1));
//
//        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, duration).setDuration(duration);
//        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override public void onAnimationUpdate(ValueAnimator animation) {
//                progress = (float) animation.getAnimatedValue();
//                mHTextView.invalidate();
//            }
//        });
//        valueAnimator.start();

        progress = 0;
        mHTextView.invalidate();
    }

    @Override public void onDraw(Canvas canvas) {
        float offset = startX;
        float oldOffset = oldStartX;

        int maxLength = Math.max(mText.length(), mOldText.length());

//        float percent = progress / (charTime + charTime / mostCount * (mText.length() - 1)); // 动画进行的百分比 0~1
        float percent = progress*1.0f / 50f; // 动画进行的百分比 0~1

        HLog.i("动画进度:"+percent);
        float firstNew = -1;
        float lastNew = -1;

        for (int i = 0; i < maxLength; i++) {

            // draw old text
            if (i < mOldText.length()) {

                oldPaint.setTextSize(textSize);
                int move = CharacterUtils.needMove(i, differentList);
                if (move != -1) {
                    oldPaint.setAlpha(255);
                    float p = percent * 2f;
                    p = p > 1 ? 1 : p;
                    float distX = CharacterUtils.getOffset(i, move, p, startX, oldStartX, gaps, oldGaps);
                    canvas.drawText(mOldText.charAt(i) + "", 0, 1, distX, startY, oldPaint);
                } else {

                    // 记录第一个新产生的字符和最后一个产生的字符位置,计算出现烟的中心
                    if(firstNew == -1){
                        firstNew = offset;
                    }

                    lastNew = offset;


                    float p = percent * 2f;
                    p = p > 1 ? 1 : p;
                    oldPaint.setAlpha((int) ((1 - p) * 255));
                    canvas.drawText(mOldText.charAt(i) + "", 0, 1, oldOffset, startY, oldPaint);
                }
                oldOffset += oldGaps[i];
            }

            // draw new text
            if (i < mText.length()) {

                if (!CharacterUtils.stayHere(i, differentList)) {

                    float interpolation = new BounceInterpolator().getInterpolation(percent);

                    int alpha = (int) (255 * percent);
                    alpha = alpha > 255 ? 255 : alpha;
                    alpha = alpha < 0 ? 0 : alpha;

                    paint.setAlpha(255);
                    paint.setTextSize(textSize);

                    float y = startY - (1 - interpolation) * upDistance * 2;

                    float width = paint.measureText(mText.charAt(i) + "");
                    canvas.drawText(mText.charAt(i) + "", 0, 1, offset + (gaps[i] - width) / 2, y, paint);
                }

                offset += gaps[i];
            }
        }

        if (percent>0.3 && percent < 1 ) {
//            if(firstNew != -1 ) {
                drawSmokes(canvas,startX + (offset - startX)/2f, startY-50, paint,percent);
//            }
        }

        if(progress<50) {
            mHTextView.postInvalidate();
            progress++;
        }
    }

    int progress = 0;
    /**
     * 烟雾, 从中间向外扩散
     * @param canvas 画布
     * @param x 中心点x坐标
     * @param y 中心点Y坐标
     * @param paint 画笔
     */
    private void drawSmokes(Canvas canvas, float x, float y, Paint paint,float percent) {
//        float[] positions = new float[]{-10,-8,-6,-5,-4,-3,-2.5f,-2,-1.5f,-1,-1,0,0,0,1,1,1.5f,2,2.5f,3,4,5,6,8,10};
//        Random random = new Random();
//        for (int i=0;i<positions.length;i++) {
//            Bitmap singleSmoke = getSingleSmoke(positions[i],random);
//            paint.setAlpha((int) (255*( (Math.abs(positions[i])-10)/10f)));
//            canvas.drawBitmap(singleSmoke, x + positions[i] * 20, y + (50 -singleSmoke.getHeight()), paint);
//        }

        if (progress < 50) {
            Bitmap b = smokes[0];

            try {
                int index = (int) (50 * percent);
                if (index<0) index = 0;
                if(index>=50) index = 49;
                b = smokes[index];
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (b != null) {
                int w = b.getWidth();
                int h = b.getHeight();

                canvas.drawBitmap(b, x- w/2, y-h/2 , bitmapPaint);
            }
        }
    }

    private Bitmap getSingleSmoke(float i, Random random) {

        int size = (int) (Math.abs(i)* random.nextDouble() * 15 +30);

        return Bitmap.createScaledBitmap(smokeBitmap, size,size,false);
    }

    private void calc() {
        textSize = mHTextView.getTextSize();
        paint.setTextSize(textSize);

        for (int i = 0; i < mText.length(); i++) {
            gaps[i] = paint.measureText(mText.charAt(i) + "");
        }

        oldPaint.setTextSize(textSize);

        for (int i = 0; i < mOldText.length(); i++) {
            oldGaps[i] = oldPaint.measureText(mOldText.charAt(i) + "");
        }

        oldStartX = (mHTextView.getWidth() - oldPaint.measureText(mOldText.toString())) / 2f;

        startX = (mHTextView.getWidth() - paint.measureText(mText.toString())) / 2f;
        startY = (int) ((mHTextView.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        differentList.clear();
        differentList.addAll(CharacterUtils.diff(mOldText, mText));

        Rect bounds = new Rect();
        paint.getTextBounds(mText.toString(), 0, mText.length(), bounds);
        upDistance = bounds.height();
    }



}
