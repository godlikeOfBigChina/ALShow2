package com.example.administrator.alshow.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.alshow.R;

public class AlertDiaryViewHolder {

    private TextView numBar;
    private TextView time;
    private TextView alertType;
    private TextView description;

    private View view;

    public AlertDiaryViewHolder(Context context, ViewGroup parent) {
        view= LayoutInflater.from(context).inflate(R.layout.alert_item,parent,false);
        numBar=(TextView)view.findViewById(R.id.numBar);
        time=(TextView)view.findViewById(R.id.time);
        alertType=(TextView)view.findViewById(R.id.type);
        description=(TextView)view.findViewById(R.id.description);
    }

    public void setNumBar(String number){
        numBar.setText("阳极编号:"+number);
    }

    public void setTime(String timeString){
        time.setText("报警时间:"+timeString);
    }

    public void setAlertType(String type){
        alertType.setText("报警类型:"+type);
    }

    public void setDescription(String des){
        description.setText("报警描述:"+des);
    }

    public View getView(){
        return view;
    }
}
