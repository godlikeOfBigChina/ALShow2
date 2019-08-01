package com.example.administrator.alshow;

import android.content.Intent;
import android.os.Message;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.alshow.model.OpDataAdaptor;
import com.example.administrator.alshow.model.OpDiary;
import com.example.administrator.alshow.model.PositiveBar;
import com.example.administrator.alshow.model.User;
import com.example.administrator.alshow.service.MyIntentService;
import com.example.administrator.alshow.service.StatusTable;
import com.example.administrator.alshow.view.GetChart;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;

public class OpDiaryActivity extends AppCompatActivity implements MyIntentService.UpdateUI{

    ListView listView;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_op_diary);
        listView=(ListView)findViewById(R.id.op_list_view);
        Intent intentService=new Intent(getBaseContext(),MyIntentService.class);
        intentService.setAction(MyIntentService.ACTION_GETLOGS);
        user=(User) getIntent().getSerializableExtra(MyIntentService.EXTRA_PARAM_USER);
        intentService.putExtra(MyIntentService.EXTRA_PARAM_USERNAME,user.getId());
        startService(intentService);
        MyIntentService.setUpdateUI(OpDiaryActivity.this);
    }

    @Override
    public void updateUi(Message message) {
        if(message.what== StatusTable.ACTION_GETREADLOGS){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<OpDiary> list=(ArrayList<OpDiary>)message.obj;
                    listView.setAdapter(new OpDataAdaptor(getBaseContext(),list));
                    Toast.makeText(getBaseContext(),"正在更新...",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
