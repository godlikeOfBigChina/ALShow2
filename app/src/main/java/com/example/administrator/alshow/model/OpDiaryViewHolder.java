package com.example.administrator.alshow.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.alshow.R;

public class OpDiaryViewHolder {
    private TextView username;
    private TextView opTime;
    private TextView opType;
    private TextView opObject;

    private View view;

    public OpDiaryViewHolder(Context context, ViewGroup parent){
        view= LayoutInflater.from(context).inflate(R.layout.diary_item,parent,false);
        username=(TextView)view.findViewById(R.id.user_name);
        opTime=(TextView)view.findViewById(R.id.op_time);
        opType=(TextView)view.findViewById(R.id.op_type);
        opObject=(TextView)view.findViewById(R.id.op_object);
    }

    public void setUsername(String userName){
        username.setText("操作用户:"+userName);
    }

    public void setOpTime(String optime){
        opTime.setText("操作时间:"+optime);
    }

    public void setOpType(int optype){
        String txt=null;
        switch (optype){
            case 0:
                txt="报警";
                break;
            case 1:
                txt="登录";
                break;
            case 2:
                txt="退出";
                break;
            case 3:
                txt="伸出";
                break;
            case 4:
                txt="退回";
                break;
            case 5:
                txt="其它";
                break;
            default:
                break;
        }
        opType.setText("操作类型:"+txt);
    }

    public void setOpObject(String object){
        opObject.setText("操作对象:"+object);
    }

    public View getView(){
        return view;
    }
}
