package com.example.administrator.alshow.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class AlertDataAdaptor extends BaseAdapter {
    Context context;
    private ArrayList<AlertDiary> diarys;


    public AlertDataAdaptor(Context context, ArrayList<AlertDiary> diarys) {
        this.context = context;
        this.diarys = diarys;
    }


    @Override
    public int getCount() {
        return diarys.size();
    }

    @Override
    public Object getItem(int position) {
        return diarys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(diarys.get(position).getNumberOfBar());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlertDiaryViewHolder holder=new AlertDiaryViewHolder(context,parent);
        for(AlertDiary t:diarys){
            holder.setNumBar(t.getNumberOfBar());
            holder.setTime(t.getOccurTime());
            holder.setAlertType(Integer.toString(t.getAlertClass()));
            holder.setDescription(t.getDescription());
        }
        return holder.getView();
    }
}
