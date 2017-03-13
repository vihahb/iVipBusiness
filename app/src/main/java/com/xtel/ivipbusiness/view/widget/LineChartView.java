package com.xtel.ivipbusiness.view.widget;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.DataObj;
import com.xtel.ivipbusiness.model.entity.ValueObj;
import com.xtel.ivipbusiness.view.MyApplication;

import java.util.ArrayList;

/**
 * Created by Vulcl on 3/10/2017
 */

public class LineChartView {
    private LineChart lineChart;

    public LineChartView(LineChart lineChart) {
        this.lineChart = lineChart;
        initLineChart();
    }

    private void initLineChart() {
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                lineChart.centerViewToAnimated(e.getX(), e.getY(), lineChart.getData().getDataSetByIndex(h.getDataSetIndex()).getAxisDependency(), 500);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // no description text
        lineChart.getDescription().setEnabled(false);

        // enable touch gestures
        lineChart.setTouchEnabled(true);

        lineChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);

        // set an alternative background color
        lineChart.setBackgroundColor(Color.LTGRAY);

        // add data
//        setData(20, 30);

        lineChart.animateX(2500);

        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
//        l.setTypeface(mTfLight);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);

//        XAxis xAxis = lineChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);
//        xAxis.setDrawAxisLine(true);
//        xAxis.setAxisMaximum(7);
//        xAxis.setAxisMinimum(0);
//
//        YAxis rightAxis = lineChart.getAxisRight();
//        rightAxis.setEnabled(false);
    }

    public void updateDay(int Day) {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisMaximum(Day);
        xAxis.setAxisMinimum(0);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    public void setData(int action, ArrayList<DataObj> arrayList) {
        ArrayList<Entry> yVals1 = new ArrayList<>();

        for (int i = 0; i < arrayList.size(); i++) {
            yVals1.add(new Entry(i, arrayList.get(i).getValues().get(0).getValue().intValue()));
        }

        LineDataSet set1;

        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            String at = null;

            if (action == 1)
                at = MyApplication.context.getString(R.string.statistic_checkin);
            else if (action == 2)
                at = MyApplication.context.getString(R.string.statistic_buy);

            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, at);

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);

            if (action == 1)
                set1.setColor(ColorTemplate.getHoloBlue());
            else
                set1.setColor(Color.RED);

            set1.setCircleColor(Color.WHITE);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);

            if (action == 1)
                set1.setFillColor(ColorTemplate.getHoloBlue());
            else
                set1.setFillColor(Color.RED);

            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(false);
            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);


            // create a data object with the datasets
            LineData data = new LineData(set1);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);

            // set data
            lineChart.setData(data);
        }

//        Cập nhật lại biểu đồ
        lineChart.invalidate();
    }

    public void setDoubleData(ArrayList<DataObj> arrayList) {
        ArrayList<Entry> yVals1 = new ArrayList<>();
        ArrayList<Entry> yVals2 = new ArrayList<>();

        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = 0; j < arrayList.get(i).getValues().size(); j++) {
                ArrayList<ValueObj> valueObjs = arrayList.get(i).getValues();
                if (valueObjs.get(j).getAction() == 1) {
                    yVals1.add(new Entry(i, valueObjs.get(j).getValue().intValue()));
                }
                if (arrayList.get(i).getValues().get(j).getAction() == 2) {
                    yVals2.add(new Entry(i, valueObjs.get(j).getValue().intValue()));
                }
            }
        }

        LineDataSet set1, set2;

        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) lineChart.getData().getDataSetByIndex(1);

            set1.setValues(yVals1);
            set2.setValues(yVals2);

            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, MyApplication.context.getString(R.string.statistic_checkin));
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(ColorTemplate.getHoloBlue());
            set1.setCircleColor(Color.WHITE);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(false);

            // create a dataset and give it a type
            set2 = new LineDataSet(yVals2, MyApplication.context.getString(R.string.statistic_buy));
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(Color.RED);
            set2.setCircleColor(Color.WHITE);
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(65);
            set2.setFillColor(Color.RED);
            set2.setDrawCircleHole(false);
            set2.setHighLightColor(Color.rgb(244, 117, 117));

            // create a data object with the datasets
            LineData data = new LineData(set1, set2);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);

            // set data
            lineChart.setData(data);
        }

//        Cập nhật lại biểu đồ
        lineChart.invalidate();
    }

    public void setVisibility(int visibility) {
        lineChart.setVisibility(visibility);
    }
}