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
import android.widget.Toast;

import com.example.administrator.alshow.model.Groove;
import com.example.administrator.alshow.model.User;
import com.example.administrator.alshow.service.MyIntentService;
import com.example.administrator.alshow.service.StatusTable;
import com.example.administrator.alshow.view.GetChart;
import com.github.mikephil.charting.charts.BarChart;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MyIntentService.UpdateUI {
    private User user;
    private EditText grooveId;
    private Button search;
    private static int REQUEST_CODE_SCAN=0;

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
        user=(User) getIntent().getSerializableExtra("USER");
        //显示用户信息
        TextView idView=(TextView) navigationView.getHeaderView(0).findViewById(R.id.userId);
        idView.setText(user.getId());
        TextView nameView=(TextView)navigationView.getHeaderView(0).findViewById(R.id.userName);
        nameView.setText(user.getName());
        //设置查询控件
        grooveId=(EditText) findViewById(R.id.grooveId);
        search=(Button) findViewById(R.id.btnGetGrooveId);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!"".equals(grooveId.getText().toString())){
                    //请求数据
                    Intent intentService=new Intent(getBaseContext(),MyIntentService.class);
                    intentService.setAction(MyIntentService.ACTION_GETGROOVE);
                    intentService.putExtra(MyIntentService.EXTRA_PARAM_GROOVEID,Integer.parseInt(grooveId.getText().toString()));
                    startService(intentService);
                    MyIntentService.setUpdateUI(HomeActivity.this);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }else{
                    Toast.makeText(getBaseContext(),"请输入槽号",Toast.LENGTH_SHORT).show();
                }

            }
        });
        Button btnScan=(Button) findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(),CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
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
            Intent intent=new Intent(this,HistoryActivity.class);
            intent.putExtra(MyIntentService.EXTRA_PARAM_GROOVEID,grooveId.getText().toString());
            startActivity(intent);
        } else if (id == R.id.nav_alert) {
            startActivity(new Intent(this,AlertActivity.class));
        } else if (id == R.id.nav_diary) {
            Intent intent=new Intent(this,OpDiaryActivity.class);
            intent.putExtra(MyIntentService.EXTRA_PARAM_USER,user);
            startActivity(intent);
        } else if (id == R.id.nav_situation) {
            startActivity(new Intent(this,SituationActivity.class));
        } else if (id == R.id.nav_comm) {
            //startActivity(new Intent(this,ComActivity.class));
        } else if (id == R.id.nav_control) {
            Intent intent=new Intent(this,ControlActivity.class);
            intent.putExtra(MyIntentService.EXTRA_PARAM_USER,user);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void updateUi(Message msg) {
        if(msg.what== StatusTable.WORKNET_ERROR){
            Toast.makeText(getBaseContext(), "工作网络错误", Toast.LENGTH_SHORT).show();
        }else if(msg.what== StatusTable.ACTION_GETGROOVE){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ScrollingView scrollingMain=(ScrollingView) findViewById(R.id.scroll_main);
                    BarChart chartAI= (BarChart) ((View)scrollingMain).findViewById(R.id.A_chart_I);
                    BarChart chartAV= (BarChart)((View)scrollingMain).findViewById(R.id.A_chart_V);
                    BarChart chartAT= (BarChart)((View)scrollingMain).findViewById(R.id.A_chart_T);
                    BarChart chartBI= (BarChart)((View)scrollingMain).findViewById(R.id.B_chart_I);
                    BarChart chartBV= (BarChart)((View)scrollingMain).findViewById(R.id.B_chart_V);
                    BarChart chartBT= (BarChart)((View)scrollingMain).findViewById(R.id.B_chart_T);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String rst=data.getStringExtra(Constant.CODED_CONTENT);
                String code=rst.split("DJ")[2].substring(1,5);
                if(!"".equals(code)) {
                    grooveId.setText(code);
                    search.performClick();
                }else{
                    Toast.makeText(getBaseContext(),"二维码不符合编码规则",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
