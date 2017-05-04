package demo.vew.stack.com.stackview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private StackView stackView;
    private LineChartView linechart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linechart = (LineChartView) findViewById(R.id.linechart);
        stackView = (StackView) findViewById(R.id.stack_view);
        ArrayList<StackViewData> stackViewDataArrayList = new ArrayList<>();
        StackViewData stackViewData = new StackViewData();
        stackViewData.setBottomText("2月1日");
        stackViewData.setTwoLevelTotal(1);
        stackViewData.setTwoLevelText(stackViewData.getTwoLevelTotal() + "");
        stackViewData.setOneLevelTotal(1);
        stackViewData.setOneLevelText(stackViewData.getOneLevelTotal() + "");
        stackViewData.setSumTotal(stackViewData.getOneLevelTotal() + stackViewData.getTwoLevelTotal());
        stackViewData.setSumTotalText(stackViewData.getSumTotal() + "");

        stackViewDataArrayList.add(stackViewData);
        for (int i = 0; i < 6; i++) {
            StackViewData stackViewData1 = new StackViewData();
            stackViewData1.setBottomText("2月1日");
            stackViewData1.setTwoLevelTotal(0);
            stackViewData1.setTwoLevelText(0 + "");
            stackViewData1.setOneLevelTotal(0);
            stackViewData1.setOneLevelText(0 + "");
            stackViewData1.setSumTotal(stackViewData1.getOneLevelTotal() + stackViewData1.getTwoLevelTotal());
            stackViewData1.setSumTotalText(stackViewData1.getSumTotal() + "");
            stackViewDataArrayList.add(stackViewData1);
        }

        stackView.updateStackView(stackViewDataArrayList);
        stackView.setHasSelectStyle(true);
        stackView.setOnRectClickLister(new StackView.OnRectClickLister() {

            @Override public void setOnRectClickCancelLister() {

            }

            @Override public void setOnRectClickLister(int index, StackViewData stackViewData) {

            }
        });
        List<LineData> lsit = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            LineData lineData = new LineData();
            if (i == 3) {
                lineData.setValue(100);
            } else {
                lineData.setValue(i * 10);
            }

            lineData.setxAxisValue(i + 1 + "月");
            lineData.setValueText(lineData.getValue() + "");
            lsit.add(lineData);
        }
        linechart.updateLineChart(lsit);
        linechart.setLineChartValueClickLister(new LineChartView.LineChartValueClickLister() {
            @Override public void onValueClick(LineData lineData) {

            }

            @Override public void onValueUnClick() {

            }
        });

    }
}
