package com.example.administrator.alshow;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class Activity2 extends AppCompatActivity {
    //use github version control
    private LineChart chart_line;
    private BarChart chart_bar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    chart_line.setVisibility(View.VISIBLE);
                    List entry=new ArrayList<Integer>();
                    entry.add(new Entry(1,2));
                    entry.add(new Entry(2,4));
                    entry.add(new Entry(3,2));
                    LineDataSet dataSet = new LineDataSet(entry, "阳极棒图1"); // add entries to dataset
                    dataSet.setColor(Color.GREEN);
                    dataSet.setValueTextColor(Color.GREEN);
                    LineData lineData = new LineData(dataSet);
                    chart_line.setData(lineData);
                    chart_line.invalidate();
                    chart_bar.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    chart_bar.setVisibility(View.VISIBLE);
                    List entry1=new ArrayList<Integer>();
                    entry1.add(new BarEntry(1,9));
                    entry1.add(new BarEntry(2,3));
                    entry1.add(new BarEntry(3,6));
                    BarDataSet dataSet1 = new BarDataSet(entry1, "历史棒图"); // add entries to dataset
                    dataSet1.setColor(Color.GREEN);
                    dataSet1.setValueTextColor(Color.GREEN);
                    BarData lineData1 = new BarData(dataSet1);
                    chart_bar.setData(lineData1);
                    chart_bar.invalidate();
                    chart_line.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    chart_line.setVisibility(View.VISIBLE);
                    List entry2=new ArrayList<Integer>();
                    entry2.add(new Entry(1,8));
                    entry2.add(new Entry(2,4));
                    entry2.add(new Entry(3,2));
                    LineDataSet dataSet2 = new LineDataSet(entry2, "通讯状态"); // add entries to dataset
                    dataSet2.setColor(Color.GREEN);
                    dataSet2.setValueTextColor(Color.GREEN);
                    LineData lineData2 = new LineData(dataSet2);
                    chart_line.setData(lineData2);
                    chart_line.invalidate();
                    chart_bar.setVisibility(View.INVISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        chart_line = this.findViewById(R.id.chart_line);
        chart_bar = this.findViewById(R.id.chart_bar);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
