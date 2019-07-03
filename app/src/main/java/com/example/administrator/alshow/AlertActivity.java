package com.example.administrator.alshow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.administrator.alshow.model.AlertDiary;
import com.example.administrator.alshow.model.AlertDataAdaptor;

import java.util.ArrayList;

public class AlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        AlertDiary row=new AlertDiary();
        row.setNumberOfBar("1001");
        row.setOccurTime("2019-07-3 18:00:00");
        row.setAlertClass(53);
        row.setDescription("未知原因报警，需要尽快排查");
        ArrayList<AlertDiary> list=new ArrayList<AlertDiary>();
        list.add(row);list.add(row);list.add(row);list.add(row);list.add(row);list.add(row);list.add(row);
        AlertDataAdaptor adaptor=new AlertDataAdaptor(AlertActivity.this,list);
        ListView listView=(ListView)findViewById(R.id.list_view);
        listView.setAdapter(adaptor);
    }

}
