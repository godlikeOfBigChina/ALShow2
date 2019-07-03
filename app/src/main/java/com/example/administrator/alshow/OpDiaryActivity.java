package com.example.administrator.alshow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.administrator.alshow.model.OpDataAdaptor;
import com.example.administrator.alshow.model.OpDiary;

import java.util.ArrayList;

public class OpDiaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_op_diary);
        OpDiary row=new OpDiary();
        row.setUsername("张三");
        row.setOpTime("2019-6-1 00:00:00");
        row.setOpType(11);
        row.setOpObject("探针伸出");
        ArrayList<OpDiary> list=new ArrayList<>();
        list.add(row);list.add(row);list.add(row);list.add(row);list.add(row);list.add(row);
        ListView listView=(ListView)findViewById(R.id.op_list_view);
        listView.setAdapter(new OpDataAdaptor(this,list));
    }
}
