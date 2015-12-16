package xyz.hanks.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Field;

/**
 * TODO: document your custom view class.
 */
public class SmokeUp extends View {

    Bitmap[] smokes = new Bitmap[50];
    int progress = 0;
    private TextPaint mTextPaint;

    public SmokeUp(Context context) {
        super(context);
        init(null, 0);
    }

    public SmokeUp(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SmokeUp(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {

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
                smokes[j] = BitmapFactory.decodeResource(getResources(), imgId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void anim() {
        progress = 0;
        invalidate();
    }

    @Override protected void onDraw(Canvas canvas) {

        if (progress < 50) {
            Bitmap b = smokes[0];

            try {
                b = smokes[progress];
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (b != null) {
                canvas.drawBitmap(smokes[progress], 10, 10, mTextPaint);
            }
            postInvalidateDelayed(40);
            progress++;
        }

    }

}
