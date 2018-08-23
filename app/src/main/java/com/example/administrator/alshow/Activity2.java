package com.example.administrator.alshow;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class Activity2 extends AppCompatActivity {
    //use github version control
    private LineChart chart;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    List entry=new ArrayList<Integer>();
                    entry.add(new Entry(1,2));
                    entry.add(new Entry(2,4));
                    entry.add(new Entry(3,2));
                    LineDataSet dataSet = new LineDataSet(entry, "阳极棒图1"); // add entries to dataset
                    dataSet.setColor(Color.GREEN);
                    dataSet.setValueTextColor(Color.GREEN);
                    LineData lineData = new LineData(dataSet);
                    chart.setData(lineData);
                    chart.invalidate();
                    return true;
                case R.id.navigation_dashboard:
                    List entry1=new ArrayList<Integer>();
                    entry1.add(new Entry(1,9));
                    entry1.add(new Entry(2,3));
                    entry1.add(new Entry(3,6));
                    LineDataSet dataSet1 = new LineDataSet(entry1, "历史棒图"); // add entries to dataset
                    dataSet1.setColor(Color.GREEN);
                    dataSet1.setValueTextColor(Color.GREEN);
                    LineData lineData1 = new LineData(dataSet1);
                    chart.setData(lineData1);
                    chart.invalidate();
                    return true;
                case R.id.navigation_notifications:
                    List entry2=new ArrayList<Integer>();
                    entry2.add(new Entry(1,8));
                    entry2.add(new Entry(2,4));
                    entry2.add(new Entry(3,2));
                    LineDataSet dataSet2 = new LineDataSet(entry2, "通讯状态"); // add entries to dataset
                    dataSet2.setColor(Color.GREEN);
                    dataSet2.setValueTextColor(Color.GREEN);
                    LineData lineData2 = new LineData(dataSet2);
                    chart.setData(lineData2);
                    chart.invalidate();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        chart = this.findViewById(R.id.chart);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
