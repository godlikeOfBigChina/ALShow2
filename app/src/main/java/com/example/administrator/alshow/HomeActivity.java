package com.example.administrator.alshow;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.alshow.model.Groove;
import com.example.administrator.alshow.model.User;
import com.example.administrator.alshow.service.MyIntentService;
import com.example.administrator.alshow.view.GetChart;
import com.github.mikephil.charting.charts.BarChart;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MyIntentService.UpdateUI {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //接收登录用户
        user=new User();
        Intent intent=getIntent();
        user.setId(intent.getStringExtra("userId"));
        user.setName(intent.getStringExtra("userName"));
        user.setRank(intent.getIntExtra("userRank",2));
        //显示用户信息
        TextView idView=navigationView.getHeaderView(0).findViewById(R.id.userId);
        idView.setText(user.getId());
        TextView nameView=navigationView.getHeaderView(0).findViewById(R.id.userName);
        nameView.setText(user.getName());
        //设置查询控件
        EditText grooveId=findViewById(R.id.grooveId);
        Button button=findViewById(R.id.btnGetGrooveId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求数据
                Intent intentService=new Intent(getBaseContext(),MyIntentService.class);
                intentService.setAction(MyIntentService.ACTION_GETGROOVE);
                intentService.putExtra(MyIntentService.EXTRA_PARAM_GROOVEID,Integer.parseInt(grooveId.getText().toString()));
                startService(intentService);
                MyIntentService.setUpdateUI(HomeActivity.this);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            startActivity(new Intent(this,HistoryActivity.class));
        } else if (id == R.id.nav_alert) {
            startActivity(new Intent(this,AlertActivity.class));
        } else if (id == R.id.nav_diary) {
            startActivity(new Intent(this,OpDiaryActivity.class));
        } else if (id == R.id.nav_situation) {
            startActivity(new Intent(this,SituationActivity.class));
        } else if (id == R.id.nav_comm) {
            startActivity(new Intent(this,ComActivity.class));
        } else if (id == R.id.nav_control) {
            /*AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("无法使用");
            builder.setMessage("功能未完成或权限不足");
            AlertDialog dialog = builder.create();
            dialog.show();*/
            startActivity(new Intent(this,ControlActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void updateUi(Message msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ScrollingView scrollingMain=findViewById(R.id.scroll_main);
                BarChart chartAI= ((View)scrollingMain).findViewById(R.id.A_chart_I);
                BarChart chartAV= ((View)scrollingMain).findViewById(R.id.A_chart_V);
                BarChart chartAT= ((View)scrollingMain).findViewById(R.id.A_chart_T);
                BarChart chartBI= ((View)scrollingMain).findViewById(R.id.B_chart_I);
                BarChart chartBV= ((View)scrollingMain).findViewById(R.id.B_chart_V);
                BarChart chartBT= ((View)scrollingMain).findViewById(R.id.B_chart_T);
                Groove groove=(Groove) msg.obj;
                chartAI= GetChart.getBarChart(chartAI,groove,true, GetChart.Kind.I);
                chartAI.invalidate();
                chartAV= GetChart.getBarChart(chartAV,groove,true, GetChart.Kind.V);
                chartAV.invalidate();
                chartAT= GetChart.getBarChart(chartAT,groove,true, GetChart.Kind.T);
                chartAT.invalidate();
                chartBI= GetChart.getBarChart(chartBI,groove,false, GetChart.Kind.I);
                chartBI.invalidate();
                chartBV= GetChart.getBarChart(chartBV,groove,false, GetChart.Kind.V);
                chartBV.invalidate();
                chartBT= GetChart.getBarChart(chartBT,groove,false, GetChart.Kind.T);
                chartBT.invalidate();

            }
        });
    }
}
