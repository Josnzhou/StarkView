package demo.vew.stack.com.stackview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 折现图
 * Created by zhouwan on 2017/4/20.
 */

public class LineChartView extends View {
    private int xAxisLineColor = Color.BLACK;
    private int xAxisLineValueColor = Color.BLACK;
    private int xAxisLineValueTextSize = 20;
    private int lineColor = Color.BLACK;
    private int pointColor = Color.BLACK;
    private int valueColor = Color.BLACK;
    private int valueTextSize = 20;
    private int selectPointColor = Color.RED;
    private int unSelectPointColor = Color.BLACK;
    private int xAxisGridColor = Color.BLACK;


    private Paint linePaint;
    private Paint circlePaint;
    private Paint textValuePaint;
    private List<LineData> lineDataList;
    private int lineChartMaxValue = 100;
    private int radius = 10;
    private int textMargin = 5;
    private List<RectF> listRectF;
    private LineChartValueClickLister lineChartValueClickLister;
    public int checkItemIndex = -1;
    public boolean isHasCancelClick = true;
    private Context context;


    public LineChartView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttributeSet(context, attrs);
        init();
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttributeSet(context, attrs);
        init();
    }

    public void updateLineChart(@Nullable List<LineData> lineDataList) {
        if (lineDataList == null && lineDataList.size() == 0) {
            return;
        }
        this.lineDataList = lineDataList;
        if (lineChartMaxValue <= 0) {
            lineChartMaxValue = 100;
        }
        this.postInvalidate();
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lineDataList == null && lineDataList.size() == 0) {
            return;
        }
        drawChart(canvas);
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setStrokeWidth(dp2px(1.5f, context));
        linePaint.setColor(xAxisLineColor);
        linePaint.setAntiAlias(true);

        circlePaint = new Paint();
        circlePaint.setColor(pointColor);
        circlePaint.setStrokeWidth(dp2px(1f, context));
        circlePaint.setAntiAlias(true);


        textValuePaint = new Paint();
        textValuePaint.setColor(valueColor);
        textValuePaint.setStyle(Paint.Style.STROKE);
        textValuePaint.setTextAlign(Paint.Align.CENTER);
        textValuePaint.setAntiAlias(true);
        listRectF = new ArrayList<>();


    }

    private void initAttributeSet(Context context, @Nullable AttributeSet attrs) {
        radius = dp2px(7f, context);
        textMargin = dp2px(5f, context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineChartView);
        if (typedArray != null) {
            radius = typedArray.getDimensionPixelSize(R.styleable.LineChartView_pointRadius, radius);
            xAxisLineColor = typedArray.getColor(R.styleable.LineChartView_xAxisLineColor, xAxisLineColor);
            xAxisGridColor = typedArray.getColor(R.styleable.LineChartView_xAxisGridColor, xAxisGridColor);
            xAxisLineValueColor = typedArray.getColor(R.styleable.LineChartView_xAxisLineValueColor, xAxisLineValueColor);
            lineColor = typedArray.getColor(R.styleable.LineChartView_lineChartColor, lineColor);
            selectPointColor = typedArray.getColor(R.styleable.LineChartView_selectPointColor, selectPointColor);
            unSelectPointColor = typedArray.getColor(R.styleable.LineChartView_unSelectPointColor, unSelectPointColor);
            xAxisGridColor = typedArray.getColor(R.styleable.LineChartView_xAxisGridColor, xAxisGridColor);
            valueTextSize = typedArray.getDimensionPixelSize(R.styleable.LineChartView_valueLineTextSize, valueTextSize);
            xAxisLineValueTextSize = typedArray.getDimensionPixelSize(R.styleable.LineChartView_xAxisLineValueTextSize, xAxisLineValueTextSize);
        }

    }

    private void drawChart(Canvas canvas) {
        int height = getHeight();
        textValuePaint.setTextSize(valueTextSize);
        textValuePaint.setColor(valueColor);
        int valueTextHeight = getTextHeight(textValuePaint);

        textValuePaint.setTextSize(xAxisLineValueTextSize);
        textValuePaint.setColor(xAxisLineValueColor);
        int xAxisTextHeight = getTextHeight(textValuePaint);
        int chartHeight = height - xAxisTextHeight - textMargin * 2;
        int pointSize = lineDataList.size();


        listRectF.clear();

        float itemWidth = getWidth() / pointSize;
        float xAxisTextWidth = itemWidth / 2;
        linePaint.setColor(xAxisGridColor);
        linePaint.setStrokeWidth(dp2px(0.5f, context));
        for (int i = 0; i < pointSize; i++) {
            LineData lineData = lineDataList.get(i);
            canvas.drawText(lineData.getxAxisValue(), xAxisTextWidth, chartHeight + dp2px(3, context) + xAxisTextHeight, textValuePaint);//绘制底部字体
            canvas.drawLine(xAxisTextWidth, 0, xAxisTextWidth, chartHeight, linePaint);//绘制栅格线

            float pointValue = (float) (lineData.getValue() / lineChartMaxValue) * (chartHeight - valueTextHeight - textMargin * 3);
            float pointY = chartHeight - pointValue - radius;
            RectF rectF = new RectF();
            rectF.top = pointY - radius;
            rectF.bottom = pointY + radius;
            rectF.left = xAxisTextWidth - radius;
            rectF.right = xAxisTextWidth + radius;

            xAxisTextWidth = xAxisTextWidth + itemWidth;
            listRectF.add(rectF);
        }
        linePaint.setColor(xAxisLineColor);
        linePaint.setStrokeWidth(dp2px(1f, context));
        canvas.drawLine(0, chartHeight, getWidth(), chartHeight, linePaint);

        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(dp2px(1f, context));
        for (int i = 0; i < listRectF.size(); i++) {
            LineData lineData = lineDataList.get(i);
            RectF rectF = listRectF.get(i);
            if ((i + 1) <= listRectF.size() - 1) {
                RectF rectFNext = listRectF.get(i + 1);
                canvas.drawLine(rectF.centerX(), rectF.centerY(), rectFNext.centerX(), rectFNext.centerY(), linePaint);
            }
            circlePaint.setStyle(Paint.Style.FILL);
            circlePaint.setColor(Color.WHITE);
            if (checkItemIndex == i) {
                pointColor = selectPointColor;
            } else {
                pointColor = unSelectPointColor;
            }
            canvas.drawCircle(rectF.centerX(), rectF.centerY(), radius, circlePaint);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setColor(pointColor);
            canvas.drawCircle(rectF.centerX(), rectF.centerY(), radius, circlePaint);
            circlePaint.setStyle(Paint.Style.FILL);
            circlePaint.setColor(pointColor);
            canvas.drawCircle(rectF.centerX(), rectF.centerY(), radius - dp2px(2f, context), circlePaint);
            textValuePaint.setTextSize(valueTextSize);
            textValuePaint.setColor(pointColor);
            canvas.drawText(lineData.getValueText(), rectF.centerX(), rectF.centerY() - radius - textMargin, textValuePaint);
            rectF.top = 0;
            rectF.bottom = getHeight();

        }


    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int indexSelect = getCheckItemIndex(x, y);
                if (indexSelect >= 0) {
                    if (indexSelect == checkItemIndex) {
                        if (lineChartValueClickLister != null) {
                            if (isHasCancelClick) {
                                checkItemIndex = -1;
                                this.postInvalidate();
                                lineChartValueClickLister.onValueUnClick();
                            }
                        }
                    } else {
                        checkItemIndex = indexSelect;
                        if (lineDataList.size() - 1 >= checkItemIndex) {
                            if (lineChartValueClickLister != null) {
                                this.postInvalidate();
                                lineChartValueClickLister.onValueClick(lineDataList.get(checkItemIndex));

                            }

                        }

                    }

                }

                break;
        }
        if (lineChartValueClickLister != null) {
            return true;
        } else {
            return super.onTouchEvent(event);
        }

    }

    private int getCheckItemIndex(int x, int y) {
        for (int i = 0; i < listRectF.size(); i++) {
            RectF rectF = listRectF.get(i);
            if (rectF.contains(x, y)) {
                return i;
            }

        }
        return -1;
    }


    public int getTextHeight(Paint paint) {
        Paint.FontMetricsInt metrics = paint.getFontMetricsInt();
        return metrics.descent - metrics.ascent;
    }

    public interface LineChartValueClickLister {
        void onValueClick(LineData lineData);

        void onValueUnClick();
    }


    public void setLineChartValueClickLister(LineChartValueClickLister lineChartValueClickLister) {
        this.lineChartValueClickLister = lineChartValueClickLister;
    }

    public void setxAxisLineColor(int xAxisLineColor) {
        this.xAxisLineColor = xAxisLineColor;
    }

    public void setxAxisLineValueColor(int xAxisLineValueColor) {
        this.xAxisLineValueColor = xAxisLineValueColor;
    }

    public void setxAxisLineValueTextSize(int xAxisLineValueTextSize) {
        this.xAxisLineValueTextSize = xAxisLineValueTextSize;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public void setPointColor(int pointColor) {
        this.pointColor = pointColor;
    }

    public void setValueColor(int valueColor) {
        this.valueColor = valueColor;
    }

    public void setValueTextSize(int valueTextSize) {
        this.valueTextSize = valueTextSize;
    }

    public void setSelectPointColor(int selectPointColor) {
        this.selectPointColor = selectPointColor;
    }

    public void setLineChartMaxValue(int lineChartMaxValue) {
        this.lineChartMaxValue = lineChartMaxValue;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setCheckItemIndex(int checkItemIndex) {
        this.checkItemIndex = checkItemIndex;
    }

    public void setUnSelectPointColor(int unSelectPointColor) {
        this.unSelectPointColor = unSelectPointColor;

    }

    public void setHasCancelClick(boolean hasCancelClick) {
        isHasCancelClick = hasCancelClick;
    }

    public void setxAxisGridColor(int xAxisGridColor) {
        this.xAxisGridColor = xAxisGridColor;
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(float dpValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}