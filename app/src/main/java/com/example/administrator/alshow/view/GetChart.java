package com.example.administrator.alshow.view;

import android.graphics.Color;
import android.util.Log;

import com.example.administrator.alshow.model.Groove;
import com.example.administrator.alshow.model.PositiveBar;
import com.example.administrator.alshow.service.MyService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class GetChart {
    public enum Kind{I,V,T}

    public static BarChart getBarChart(BarChart barChart, Groove groove,boolean ifA,Kind kind){
        List entry=new ArrayList<Float>();
        BarDataSet dataSet;
        if(ifA){
            for (PositiveBar bar:groove.getBarsOfA()) {
                entry.add(new BarEntry(bar.getId(),kind==Kind.I?bar.getCurrent():(kind==Kind.V?bar.getVoltage():bar.getTempareture())));
            }
        }else{
            for (PositiveBar bar:groove.getBarsOfA()) {
                entry.add(new BarEntry(bar.getId(),kind==Kind.I?bar.getCurrent():(kind==Kind.V?bar.getVoltage():bar.getTempareture())));
            }
        }
        dataSet = new BarDataSet(entry, kind==Kind.I?"电流":(kind==Kind.V?"电压":"温度")); // add entries to dataset
        dataSet.setColor(kind==Kind.I?Color.GREEN:(kind==Kind.V?Color.RED:Color.BLUE));
        dataSet.setValueTextColor(Color.BLACK);
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        return barChart;
    }

    public static LineChart getAnodeHistory(LineChart chart,List<PositiveBar> anodeList, Kind kind){
//        Log.e("size:"+anodeList.size(),anodeList.get(0).toString());
        String[] xLables=new String[anodeList.size()];
        List<Entry> entry=new ArrayList<Entry>();
        int i=0;
        for (PositiveBar bar:anodeList) {
            entry.add(new Entry(i,(kind==Kind.I?bar.getCurrent():(kind==Kind.V?bar.getVoltage():bar.getTempareture()))*1000));
            xLables[i++]=bar.getDatetime().toLocaleString().substring(13);
        }
        LineDataSet dataSet = new LineDataSet(entry, kind==Kind.I?"电流":(kind==Kind.V?"电压:mV":"温度")); // add entries to dataset
        dataSet.setColor(kind==Kind.I?Color.GREEN:(kind==Kind.V?Color.RED:Color.BLUE));
        dataSet.setValueTextColor(Color.BLACK);
        LineData data = new LineData(dataSet);
        final String[] xAixsLabels=xLables;
        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xAixsLabels[(int)value];
            }
        });
        chart.setData(data);
        return chart;
    }

}
