package com.example.administrator.alshow;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
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
    private BarChart chart;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private PlaceholderFragment[] fragments;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0,true);
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(1,true);
                    return true;
                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(2,true);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mSectionsPagerAdapter=new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.viewPager);
        Fragment fragment=mSectionsPagerAdapter.getItem(0);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.content_main, container, false);
            ScrollingView scrollingView=rootView.findViewById(R.id.scroll);
            BarChart chart= ((View)scrollingView).findViewById(R.id.chart);
            List entry=new ArrayList<Integer>();
            BarDataSet dataSet;
            if( getArguments().getInt(ARG_SECTION_NUMBER)==0){
                entry.add(new BarEntry(1,2));
                entry.add(new BarEntry(2,0));
                entry.add(new BarEntry(3,2));
                dataSet = new BarDataSet(entry, "电压"); // add entries to dataset
                dataSet.setColor(Color.RED);
                dataSet.setValueTextColor(Color.RED);
            }else if(getArguments().getInt(ARG_SECTION_NUMBER)==1){
                entry.add(new BarEntry(1,2));
                entry.add(new BarEntry(2,4));
                entry.add(new BarEntry(3,2));
                dataSet = new BarDataSet(entry, "电流"); // add entries to dataset
                dataSet.setColor(Color.GREEN);
                dataSet.setValueTextColor(Color.GREEN);
            }else{
                entry.add(new BarEntry(1,2));
                entry.add(new BarEntry(2,4));
                entry.add(new BarEntry(3,2));
                dataSet = new BarDataSet(entry, "温度"); // add entries to dataset
                dataSet.setColor(Color.BLUE);
                dataSet.setValueTextColor(Color.BLUE);
            }
            BarData barData = new BarData(dataSet);
            chart.setData(barData);
            chart.invalidate();
            return rootView;
        }
    }
}
