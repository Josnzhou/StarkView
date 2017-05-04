package demo.vew.stack.com.stackview;

/**
 * Created by zhouwan on 2017/4/20.
 */

public class LineData {
    private int value;
    private String xAxisValue = "";
    private String valueText = "";
    private long time;


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getxAxisValue() {
        return xAxisValue;
    }

    public void setxAxisValue(String xAxisValue) {
        this.xAxisValue = xAxisValue;
    }

    public String getValueText() {
        return valueText;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
