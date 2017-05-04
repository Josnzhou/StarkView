package demo.vew.stack.com.stackview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhouwan on 2017/4/8.
 */

public class CircleStroke extends View {
    private Paint paint;
    private int circleColor = Color.BLACK;

    public CircleStroke(Context context) {
        super(context);
    }

    public CircleStroke(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleStroke(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleStroke);
        if (typedArray != null) {
            circleColor = typedArray.getColor(R.styleable.CircleStroke_circleStrokeColor, circleColor);
        }
        paint.setStrokeWidth(2f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(circleColor);
        paint.setAntiAlias(true);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = Math.min(getWidth(), getHeight());
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius / 2, paint);

    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
