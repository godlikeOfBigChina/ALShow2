package com.example.administrator.alshow.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class OpDataAdaptor extends BaseAdapter {
    Context context;
    private ArrayList<OpDiary> diarys;

    public OpDataAdaptor(Context context, ArrayList<OpDiary> diarys) {
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
        return Long.parseLong(diarys.get(position).getOpTime());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OpDiaryViewHolder holder=new OpDiaryViewHolder(context,parent);
        for(OpDiary t:diarys){
            holder.setUsername(t.getUsername());
            holder.setOpTime(t.getOpTime());
            holder.setOpType(t.getOpType());
            holder.setOpObject(t.getOpObject());
        }
        return holder.getView();
    }
}
