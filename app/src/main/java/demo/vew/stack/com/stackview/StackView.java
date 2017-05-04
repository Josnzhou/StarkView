package demo.vew.stack.com.stackview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zhouwan on 2017/3/31.
 */
public class StackView extends View {
    private Paint gridLinePaint;
    //    /  绘制文本的画笔
    private Paint drawTextPaint;
    /**
     * 绘制矩形
     */
    private Paint drawRectPaint;
    /**
     * 绘制底部tag提示
     */
    private Paint drawTagPaint;
    private Paint paintRoundRect;
    /**
     * 是否显示分层数字
     */
    private boolean isDisplayValue = false;
    /**
     * 当前点击柱形图下标
     */
    private int rectClickIndex = -1;
    private ArrayList<Rect> listTopRect;
    private ArrayList<Rect> listBottomRect;
    private int stackViewHeight = 0;
    @NonNull private List<StackViewData> stackViewDataList = new ArrayList<>();
    /**
     * 统计表的最大值
     */
    private int MAX = 0;

    private OnRectClickLister onRectClickLister;
    /**
     * 文字距离
     */
    private int TEXT_MARGIN = 10;
    private int MARGIN_TOP = 5;
    /**
     * 是否有点击效果
     */
    private boolean isHasSelectStyle;

    private int countTextColor = Color.BLACK;

    private int valueTextColor = Color.BLACK;

    private int lineColor = Color.GRAY;

    private int topRectColor = Color.RED;

    private int bottomRectColor = Color.BLUE;

    private int xAxisTextColor = Color.BLUE;

    private int topSelectColor = Color.GRAY;

    private int bottomSelectColor = Color.YELLOW;
    private int tagColor = Color.BLACK;
    private int valueTextSize = 20;
    private int xAxisTextSize = 16;
    private int tagTextSize = 16;
    private int roundSize = 15;
    private int bubbleSpace = 20;


    /**
     * 统计图tag高度
     */
    private final int CHART_TAG_SIZE = 60;
    private String leftTagText = "";
    private String rightTagText = "";
    private String sumTotalDescribe = "描述";
    private int rectWidth = 50;
    private Context context;

    public StackView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public StackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initParams(context, attrs);
    }

    public StackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initParams(context, attrs);
    }

    public void updateStackView(@NonNull List<StackViewData> stackViewDataList) {
        this.stackViewDataList = stackViewDataList;
        Comparator comp = new SortComparator();
        if (stackViewDataList.size() == 0) {
            return;
        }
        ArrayList<StackViewData> list = new ArrayList<>();
        list.addAll(stackViewDataList);
        Collections.sort(list, comp);
        MAX = list.get(0).getSumTotal();
        this.postInvalidate();
    }


    private void init() {


        listTopRect = new ArrayList<>();
        listBottomRect = new ArrayList<>();

        gridLinePaint = new Paint();
        gridLinePaint.setStrokeWidth(dp2px(0.5f, context));
        gridLinePaint.setStyle(Paint.Style.FILL);
        gridLinePaint.setAntiAlias(true);
        gridLinePaint.setColor(Color.BLACK);

        drawRectPaint = new Paint();

        drawTextPaint = new Paint();
        drawTextPaint.setColor(Color.BLACK);
        drawTextPaint.setTextSize(16f);
        drawTextPaint.setStyle(Paint.Style.FILL);
        drawTextPaint.setTextAlign(Paint.Align.CENTER);
        drawTextPaint.setStrokeWidth(2f);
        drawTextPaint.setAntiAlias(true);

        drawTagPaint = new Paint();
        drawTagPaint.setColor(Color.BLACK);
        drawTagPaint.setTextSize(16f);
        drawTagPaint.setStyle(Paint.Style.FILL);
        drawTagPaint.setTextAlign(Paint.Align.CENTER);
        drawTagPaint.setStrokeWidth(2f);
        drawTagPaint.setAntiAlias(true);
        drawTagPaint.setTextSize(tagTextSize);

        paintRoundRect = new Paint();


    }

    private void initParams(Context context, AttributeSet attrs) {
        valueTextSize = dp2px(13, context);
        xAxisTextSize = dp2px(13, context);
        tagTextSize = dp2px(13, context);
        TEXT_MARGIN = dp2px(5, context);
        MARGIN_TOP = dp2px(10, context);
        bubbleSpace = dp2px(7, context);
        roundSize = dp2px(5, context);
        rectWidth = dp2px(10, context);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StackView);
        if (typedArray != null) {
            countTextColor = typedArray.getColor(R.styleable.StackView_countTextColor, Color.BLACK);
            valueTextColor = typedArray.getColor(R.styleable.StackView_valueTextColor, Color.BLACK);
            lineColor = typedArray.getColor(R.styleable.StackView_lineColor, Color.GRAY);
            topRectColor = typedArray.getColor(R.styleable.StackView_topRectColor, Color.RED);
            bottomRectColor = typedArray.getColor(R.styleable.StackView_bottomRectColor, Color.BLUE);
            xAxisTextColor = typedArray.getColor(R.styleable.StackView_xAxisTextColor, Color.BLUE);
            topSelectColor = typedArray.getColor(R.styleable.StackView_topSelectColor, Color.GRAY);
            bottomSelectColor = typedArray.getColor(R.styleable.StackView_bottomSelectColor, Color.YELLOW);
            leftTagText = typedArray.getString(R.styleable.StackView_leftTagText);
            rightTagText = typedArray.getString(R.styleable.StackView_rightTagText);
            tagColor = typedArray.getColor(R.styleable.StackView_tagColor, Color.BLACK);
            valueTextSize = typedArray.getDimensionPixelSize(R.styleable.StackView_valueTextSize, valueTextSize);
            xAxisTextSize = typedArray.getDimensionPixelSize(R.styleable.StackView_xAxisTextSize, xAxisTextSize);
            tagTextSize = typedArray.getDimensionPixelSize(R.styleable.StackView_tagTextSize, tagTextSize);
            rectWidth = typedArray.getDimensionPixelSize(R.styleable.StackView_rectWidth, rectWidth);
            sumTotalDescribe = typedArray.getString(R.styleable.StackView_describe);
            typedArray.recycle();
        }
        init();

    }

    @Override protected void onDraw(Canvas canvas) {

        if (stackViewDataList.size() == 0) {
            return;
        }
        if (MAX <= 0) {
            MAX = 1;
        }
        drawChart(canvas);
        super.onDraw(canvas);

    }


    /**
     * 绘制背景线
     *
     * @param canvas
     */
    private void drawChart(Canvas canvas) {
        gridLinePaint.setColor(lineColor);
        stackViewHeight = MARGIN_TOP;
        drawTextPaint.setTextSize(xAxisTextSize);
        int xDrawTextHeight = getTextHeight(drawTextPaint);
        int gridHeight = (getHeight() - xDrawTextHeight - TEXT_MARGIN - MARGIN_TOP - getTextHeight(drawTagPaint) * 2) / 5;
        for (int i = 0; i < 5; i++) {
            canvas.drawLine(0, stackViewHeight, getWidth(), stackViewHeight, gridLinePaint);
            stackViewHeight = stackViewHeight + gridHeight;
        }
        canvas.drawLine(0, stackViewHeight, getWidth(), stackViewHeight, gridLinePaint);

        int dateSize = stackViewDataList.size();

        int xLength = getWidth() / dateSize / 2;
        int textY = stackViewHeight + TEXT_MARGIN + xDrawTextHeight;
        int textX = xLength;
        drawTextPaint.setColor(xAxisTextColor);
        drawTextPaint.setTextSize(xAxisTextSize);
        for (int i = 0; i < dateSize; i++) {//绘制底部字体
            canvas.drawText(stackViewDataList.get(i).getBottomText(), textX, textY, drawTextPaint);
            textX = textX + (xLength * 2);
        }
        //绘制底部字体
        drawTagText(canvas, textY);

        int paddingRect = rectWidth;
        int rectRight = xLength;

        drawTextPaint.setColor(valueTextColor);
        //统计图的高度
        int rectHeight = stackViewHeight - MARGIN_TOP - getTextHeight(drawTextPaint) - dp2px(5, context);
        drawTextPaint.setTextSize(valueTextSize);
        for (int i = 0; i < dateSize; i++) {
            StackViewData stackViewData = stackViewDataList.get(i);
            int top = (int) (getDoubleValue(stackViewData.getSumTotal(), MAX) * rectHeight);
            top = getMaxHeight(top);
            Rect rectBottom = new Rect();
            rectBottom.left = rectRight - paddingRect;
            rectBottom.top = stackViewHeight - (int) (top * getDoubleValue(stackViewData.getTwoLevelTotal(), stackViewData.getSumTotal()));

            rectBottom.right = rectRight + paddingRect;
            rectBottom.bottom = stackViewHeight;


            if (i == rectClickIndex && isHasSelectStyle) {
                drawRectPaint.setColor(bottomSelectColor);
            } else {
                drawRectPaint.setColor(bottomRectColor);
            }
            int valueHeight;
            canvas.drawRect(rectBottom, drawRectPaint);
            listBottomRect.add(rectBottom);
            drawTextPaint.setColor(valueTextColor);

            if (isDisplayValue && stackViewData.getTwoLevelTotal() > 0) {
                valueHeight = (rectBottom.bottom - (rectBottom.bottom - rectBottom.top) / 2);
                float baseline = getTextBsaeLine(valueHeight, drawTextPaint);
                canvas.drawText(stackViewData.getTwoLevelText(), rectRight, baseline, drawTextPaint);
            } else if (stackViewData.getTwoLevelTotal() <= 0) {
                rectBottom.top = rectBottom.bottom;
            }


            if (i == rectClickIndex && isHasSelectStyle) {
                drawRectPaint.setColor(topSelectColor);
            } else {
                drawRectPaint.setColor(topRectColor);
            }
            Log.e("0000", rectBottom.top + "-----" + rectBottom.bottom);
            Rect rectTop = new Rect();
            rectTop.left = rectBottom.left;
            rectTop.top = stackViewHeight - top;
            rectTop.right = rectBottom.right;
            rectTop.bottom = rectBottom.top;
            Log.e("1111", rectTop.top + "-----" + rectTop.bottom);

            canvas.drawRect(rectTop, drawRectPaint);
            listTopRect.add(rectTop);
            if (isDisplayValue && stackViewData.getOneLevelTotal() > 0) {
                valueHeight = (rectTop.bottom - (rectTop.bottom - rectTop.top) / 2);
                float baseline = getTextBsaeLine(valueHeight, drawTextPaint);
                canvas.drawText(stackViewData.getOneLevelTotal() + "", rectRight, baseline, drawTextPaint);
            }
            drawTextPaint.setTextSize(valueTextSize);
            if (isHasSelectStyle && i == rectClickIndex) {
                drawTextPaint.setColor(bottomSelectColor);
            } else {
                drawTextPaint.setColor(bottomRectColor);
            }
            canvas.drawText(stackViewData.getSumTotalText() + "", rectRight, rectTop.top - TEXT_MARGIN, drawTextPaint);

            rectRight = rectRight + (xLength * 2);
        }
        if (isHasSelectStyle) {
            drawRoundRect(getSelectRect(), canvas);
        }


    }


    private void drawTagText(Canvas canvas, int textY) {
        //绘制底部标记tag字体
        drawTagPaint.setTextAlign(Paint.Align.RIGHT);
        drawTagPaint.setColor(tagColor);
        drawTagPaint.setTextSize(tagTextSize);

        float yDrawTag = (getOtherHeight(drawTagPaint) + textY) + dp2px(1, context);
        float xDrawTag = getWidth() / 2 - TEXT_MARGIN;
        canvas.drawText(leftTagText, xDrawTag, yDrawTag, drawTagPaint);
        drawTagPaint.setTextAlign(Paint.Align.LEFT);


        xDrawTag = getWidth() / 2 + TEXT_MARGIN * 3;
        canvas.drawText(rightTagText, xDrawTag, yDrawTag, drawTagPaint);

        float width = drawTagPaint.measureText(leftTagText);
        drawTagPaint.setColor(topRectColor);
        float xDraw = getWidth() / 2 - TEXT_MARGIN * 3 - width;
        yDrawTag = yDrawTag - dp2px(5, context);
        canvas.drawCircle(xDraw, yDrawTag, dp2px(5, context), drawTagPaint);

        drawTagPaint.setColor(bottomRectColor);
        xDraw = getWidth() / 2 + TEXT_MARGIN;
        canvas.drawCircle(xDraw, yDrawTag, dp2px(5, context), drawTagPaint);
    }


    /**
     * 判断点击的是哪个统计图
     *
     * @param x
     * @param y
     * @return
     */
    public int getClickRectIndex(int x, int y) {
        int index = -1;
        for (int i = 0; i < listBottomRect.size(); i++) {
            Rect rect = listBottomRect.get(i);
            if (rect.contains(x, y)) {
                return i;
            }
        }
        for (int i = 0; i < listTopRect.size(); i++) {
            Rect rect = listTopRect.get(i);
            if (rect.contains(x, y)) {
                return i;
            }
        }
        return index;
    }

    private Rect getSelectRect() {
        Rect rect = new Rect();
        if (rectClickIndex < 0) {
            return null;
        }
        if (listBottomRect.size() - 1 >= rectClickIndex) {
            Rect rect1 = listBottomRect.get(rectClickIndex);
            rect.bottom = rect1.bottom;
            rect.left = rect1.left;
            rect.right = rect1.right;
        }

        if (listTopRect.size() - 1 >= rectClickIndex) {
            Rect rect1 = listTopRect.get(rectClickIndex);
            rect.left = rect1.left;
            rect.right = rect1.right;
            rect.top = rect1.top;
        }

        return rect;
    }

    private void drawRoundRect(Rect rect, Canvas canvas) {
        if (rect == null) {
            return;
        }
        StringBuilder stringBuilderSum = new StringBuilder();
        StringBuilder stringBuilderOneLevel = new StringBuilder();
        StringBuilder stringBuilderTwoLevel = new StringBuilder();
        paintRoundRect.setColor(bottomRectColor);
        paintRoundRect.setTextSize(tagTextSize);
        paintRoundRect.setAntiAlias(true);
        boolean isOverstep = false;
        int rectFWidth = 130;
        if (stackViewDataList.size() - 1 >= rectClickIndex && rectClickIndex >= 0) {
            StackViewData stackViewData = stackViewDataList.get(rectClickIndex);
            stringBuilderSum.append(stackViewData.getBottomText()).append(sumTotalDescribe).append(":").append(stackViewData.getSumTotalText());
            stringBuilderOneLevel.append(leftTagText).append(":").append(stackViewData.getOneLevelText());
            stringBuilderTwoLevel.append(rightTagText).append(":").append(stackViewData.getTwoLevelText());
            float arr[] = {paintRoundRect.measureText(stringBuilderSum.toString()), paintRoundRect.measureText(stringBuilderOneLevel.toString()), paintRoundRect.measureText(stringBuilderTwoLevel.toString())};
            rectFWidth = (int) getMax(arr) + bubbleSpace * 2;

        }


        RectF rectF = new RectF();
        if (getWidth() - rect.right >= rectFWidth) {
            rectF.left = rect.right + bubbleSpace;
            rectF.right = rectF.left + rectFWidth;
            isOverstep = false;
        } else {
            rectF.right = rect.left - bubbleSpace;
            rectF.left = rectF.right - rectFWidth;
            isOverstep = true;

        }

        rectF.top = rect.top - MARGIN_TOP;
        rectF.bottom = rectF.top + getOtherHeight(paintRoundRect) * 3 + bubbleSpace;


        paintRoundRect.setStyle(Paint.Style.FILL_AND_STROKE);
        paintRoundRect.setStrokeWidth(dp2px(1f, context));
        canvas.drawRoundRect(rectF, roundSize, roundSize, paintRoundRect);


        rectF.left = rectF.left + paintRoundRect.getStrokeWidth();
        rectF.right = rectF.right - paintRoundRect.getStrokeWidth();
        rectF.top = rectF.top + paintRoundRect.getStrokeWidth();
        rectF.bottom = rectF.bottom - paintRoundRect.getStrokeWidth();
        paintRoundRect.setStyle(Paint.Style.FILL_AND_STROKE);
        paintRoundRect.setColor(Color.WHITE);
        canvas.drawRoundRect(rectF, roundSize, roundSize, paintRoundRect);
        drawPathLine(paintRoundRect, rectF, isOverstep, canvas);
        String[] describe = {stringBuilderSum.toString(), stringBuilderOneLevel.toString(), stringBuilderTwoLevel.toString()};
        drawShowText(paintRoundRect, rectF, canvas, describe);

    }


    private void drawPathLine(Paint paint, RectF rectF, boolean isOverstep, Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(bottomRectColor);
        Path path = new Path();
        Path pathWrite = new Path();
        int triangleSpace = dp2px(5, context);
        float centerY = rectF.top + (rectF.bottom - rectF.top) / 2;
        float centerX;
        if (!isOverstep) {
            centerX = rectF.left - paint.getStrokeWidth();
            path.moveTo(centerX, centerY - triangleSpace);
            path.lineTo(centerX - bubbleSpace, centerY);
            path.lineTo(centerX, centerY + triangleSpace);
            path.close();
            canvas.drawPath(path, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            centerX = centerX + paint.getStrokeWidth();
            pathWrite.moveTo(centerX, centerY - triangleSpace + paint.getStrokeWidth());
            pathWrite.lineTo(centerX - bubbleSpace, centerY);
            pathWrite.lineTo(centerX, centerY + triangleSpace - paint.getStrokeWidth());

        } else {
            centerX = rectF.right + paint.getStrokeWidth();
            path.moveTo(centerX, centerY - triangleSpace);
            path.lineTo(centerX + bubbleSpace, centerY);
            path.lineTo(centerX, centerY + triangleSpace);
            path.close();
            canvas.drawPath(path, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            centerX = centerX - paint.getStrokeWidth();
            pathWrite.moveTo(centerX, centerY - triangleSpace + paint.getStrokeWidth());
            pathWrite.lineTo(centerX + bubbleSpace, centerY);
            pathWrite.lineTo(centerX, centerY + triangleSpace - paint.getStrokeWidth());
        }


        //根据Path进行绘制，绘制三角形
        pathWrite.close();
        canvas.drawPath(pathWrite, paint);

    }

    private void drawShowText(Paint paint, RectF rectF, Canvas canvas, String[] strings) {
        paint.setTextSize(tagTextSize);
        paint.setColor(Color.BLACK);
        int textHeight = getOtherHeight(drawTagPaint);
        float textMarginTop = (rectF.bottom - rectF.top - textHeight * 3) / 2;
        float textX = rectF.left + bubbleSpace;
        float textY = rectF.top + textMarginTop + textHeight - dp2px(3, context);
        canvas.drawText(strings[0], textX, textY, paint);
        canvas.drawText(strings[1], textX, textY + textHeight, paint);
        canvas.drawText(strings[2], textX, textY + textHeight * 2, paint);


    }


    public boolean isDisplayValue() {
        return isDisplayValue;
    }

    public void setDisplayValue(boolean isDisplayValue) {
        this.isDisplayValue = isDisplayValue;
        this.postInvalidate();
    }

    public boolean isHasSelectStyle() {
        return isHasSelectStyle;
    }

    public void setHasSelectStyle(boolean hasSelectStyle) {
        isHasSelectStyle = hasSelectStyle;
        this.postInvalidate();
    }

    private double getDoubleValue(int a, int b) {
        return a * 1. / b * 1.;
    }

    public interface OnRectClickLister {
        void setOnRectClickLister(int index, StackViewData stackViewData);

        void setOnRectClickCancelLister();
    }

    public void setOnRectClickLister(OnRectClickLister onRectClickLister) {
        this.onRectClickLister = onRectClickLister;
    }

    /**
     * 测量文字的高度
     *
     * @param paint
     * @return
     */
    public int getTextHeight(Paint paint) {
        return (int) paint.getTextSize();//文字高
    }

    /**
     * 文字宽度
     *
     * @param text
     * @param paint
     * @return
     */
    public int getTextWidth(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    public int getOtherHeight(Paint paint) {
        Paint.FontMetricsInt metrics = paint.getFontMetricsInt();
        return metrics.descent - metrics.ascent;
    }

    private float getMax(float arr[]) {
        float max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    /**
     * 落差值太大时处理
     *
     * @return
     */
    public int getMaxHeight(int rectHeight) {
        if (rectHeight <= 0) {
            return 0;
        } else if (rectHeight < 30) {
            return 30;
        }
        return rectHeight;
    }

    public float getTextBsaeLine(int height, Paint mPaint) {
        return height + mPaint.getTextSize() / 2 - (int) (mPaint.getFontMetrics().descent / 2);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
                int indexSelect = getClickRectIndex(x, y);
                if (indexSelect >= 0) {
                    if (rectClickIndex == indexSelect) {
                        if (onRectClickLister != null) {
                            onRectClickLister.setOnRectClickCancelLister();
                        }
                        rectClickIndex = -1;
                    } else {
                        rectClickIndex = indexSelect;
                        if (onRectClickLister != null) {
                            if (stackViewDataList.size() - 1 >= indexSelect) {
                                onRectClickLister.setOnRectClickLister(indexSelect, stackViewDataList.get(indexSelect));
                            }
                        }
                    }

                    if (isHasSelectStyle) {
                        this.postInvalidate();
                    }
                    Log.e("click", "x--" + x + "y---" + y);
                }

                break;
        }
        return true;
    }

    public int getCountTextColor() {
        return countTextColor;
    }

    public void setCountTextColor(int countTextColor) {
        this.countTextColor = countTextColor;
    }

    public int getValueTextColor() {
        return valueTextColor;
    }

    public void setValueTextColor(int valueTextColor) {
        this.valueTextColor = valueTextColor;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getTopRectColor() {
        return topRectColor;
    }

    public void setTopRectColor(int topRectColor) {
        this.topRectColor = topRectColor;
    }

    public int getBottomRectColor() {
        return bottomRectColor;
    }

    public void setBottomRectColor(int bottomRectColor) {
        this.bottomRectColor = bottomRectColor;
    }

    public int getxAxisTextColor() {
        return xAxisTextColor;
    }

    public void setxAxisTextColor(int xAxisTextColor) {
        this.xAxisTextColor = xAxisTextColor;
    }

    public int getTopSelectColor() {
        return topSelectColor;
    }

    public void setTopSelectColor(int topSelectColor) {
        this.topSelectColor = topSelectColor;
    }

    public int getBottomSelectColor() {
        return bottomSelectColor;
    }

    public void setBottomSelectColor(int bottomSelectColor) {
        this.bottomSelectColor = bottomSelectColor;
    }

    public int getTagColor() {
        return tagColor;
    }

    public void setTagColor(int tagColor) {
        this.tagColor = tagColor;
    }

    public int getValueTextSize() {
        return valueTextSize;
    }

    public void setValueTextSize(int valueTextSize) {
        this.valueTextSize = valueTextSize;
    }

    public int getxAxisTextSize() {
        return xAxisTextSize;
    }

    public void setxAxisTextSize(int xAxisTextSize) {
        this.xAxisTextSize = xAxisTextSize;
    }

    public int getTagTextSize() {
        return tagTextSize;
    }

    public void setTagTextSize(int tagTextSize) {
        this.tagTextSize = tagTextSize;
    }

    public int getCHART_TAG_SIZE() {
        return CHART_TAG_SIZE;
    }

    public String getLeftTagText() {
        return leftTagText;
    }

    public void setLeftTagText(String leftTagText) {
        this.leftTagText = leftTagText;
    }

    public String getRightTagText() {
        return rightTagText;
    }

    public void setRightTagText(String rightTagText) {
        this.rightTagText = rightTagText;
    }

    public String getSumTotalDescribe() {
        return sumTotalDescribe;
    }

    public void setSumTotalDescribe(String sumTotalDescribe) {
        this.sumTotalDescribe = sumTotalDescribe;
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
