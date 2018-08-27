package com.example.administrator.alshow;

import android.content.Intent;
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

import com.example.administrator.alshow.model.Groove;
import com.example.administrator.alshow.service.MyService;
import com.example.administrator.alshow.service.MyServiceConnection;
import com.example.administrator.alshow.view.GetChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class Activity2 extends AppCompatActivity {
    private MyService myService;
    private MyServiceConnection connection = new MyServiceConnection();

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
        mViewPager.setAdapter(mSectionsPagerAdapter);
        Intent intent1 = new Intent(getApplicationContext(), MyService.class);
        bindService(intent1, connection, BIND_AUTO_CREATE);
        myService=connection.myService;
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
            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 0://main.xml
                    View main = inflater.inflate(R.layout.main, container, false);
                    ScrollingView scrollingMain=main.findViewById(R.id.scroll_main);
                    BarChart chart= ((View)scrollingMain).findViewById(R.id.A_chart_I);
                    Groove groove=new Groove();
                    groove.setId(1);
                    chart= GetChart.getBarChart(chart,groove,true, GetChart.Kind.I);
                    chart.invalidate();

                    return main;
                case 1:
                    View history = inflater.inflate(R.layout.history, container, false);
                    ScrollingView scrollingHistory=history.findViewById(R.id.scroll_history);
                    return history;
                case 2:
                    View alert = inflater.inflate(R.layout.alert, container, false);
                    ScrollingView scrollingAlert=alert.findViewById(R.id.scroll_alert);
                    return alert;
                default:
                    return null;
            }
        }
    }

}
