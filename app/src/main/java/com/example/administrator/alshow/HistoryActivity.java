package com.example.administrator.alshow;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ToggleButton;

import com.example.administrator.alshow.model.Groove;
import com.example.administrator.alshow.model.PositiveBar;
import com.example.administrator.alshow.service.MyIntentService;
import com.example.administrator.alshow.service.StatusTable;
import com.example.administrator.alshow.view.GetChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import java.util.List;

public class HistoryActivity extends AppCompatActivity implements MyIntentService.UpdateUI {
    private EditText grooveIdView;
    private EditText anodeIdView;
    private ToggleButton ifAView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        grooveIdView=(EditText) findViewById(R.id.grooveId);
        anodeIdView=(EditText) findViewById(R.id.anodeId);
        ifAView=(ToggleButton) findViewById(R.id.ifA);
        Button getHistory=(Button) findViewById(R.id.btnGetHistory);
        getHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int grooveId=Integer.parseInt(grooveIdView.getText().toString());
                int anodeId=Integer.parseInt(anodeIdView.getText().toString());
                boolean ifA=ifAView.isChecked();
                Intent intent=new Intent(HistoryActivity.this, MyIntentService.class);
                intent.setAction(MyIntentService.ACTION_GETHISTORY);
                intent.putExtra(MyIntentService.EXTRA_PARAM_GROOVEID,grooveId);
                intent.putExtra(MyIntentService.EXTRA_PARAM_ANODEID,anodeId);
                intent.putExtra(MyIntentService.EXTRA_PARAM_IFA,ifA);
                MyIntentService.setUpdateUI(HistoryActivity.this);
                startService(intent);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    @Override
    public void updateUi(Message msg) {
        if(msg.what== StatusTable.ACTION_GETBARHISTORY){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ScrollingView scrollingMain=(ScrollingView) findViewById(R.id.scroll_main);
                    LineChart chatI=(LineChart) ((View)scrollingMain).findViewById(R.id.chartLineI);
                    LineChart chatV=(LineChart)((View)scrollingMain).findViewById(R.id.chartLineV);
                    LineChart chatT=(LineChart)((View)scrollingMain).findViewById(R.id.chartLineT);
                    List<PositiveBar> anodeHistory=(List<PositiveBar>)msg.obj;
                    chatI=GetChart.getAnodeHistory(chatI,anodeHistory, GetChart.Kind.I);
                    chatI.invalidate();
                    chatV=GetChart.getAnodeHistory(chatV,anodeHistory, GetChart.Kind.V);
                    chatV.invalidate();
                    chatT=GetChart.getAnodeHistory(chatT,anodeHistory, GetChart.Kind.T);
                    chatT.invalidate();
                }
            });
        }

    }
}
