package xyz.hanks.draw;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * Created by hanks on 15-12-19.
 */
public class NText extends TextView {
    public NText(Context context) {
        super(context);
        init(null,0);
    }

    public NText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public NText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) public NText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs,defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {

    }



}
