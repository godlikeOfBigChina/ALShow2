package com.example.administrator.alshow.view;

import android.graphics.Color;

import com.example.administrator.alshow.model.Groove;
import com.example.administrator.alshow.model.PositiveBar;
import com.example.administrator.alshow.service.MyService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class GetChart {
    public enum Kind{I,V,T}

    public static BarChart getBarChart(BarChart barChart, Groove groove,boolean ifA,Kind kind){
        List entry=new ArrayList<Integer>();
        groove=new MyService().getGroove(groove.getId());
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
        dataSet.setColor(Color.RED);
        dataSet.setValueTextColor(Color.RED);
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        return barChart;
    }
}
