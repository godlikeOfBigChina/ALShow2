package com.example.administrator.alshow.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.administrator.alshow.model.Groove;
import com.example.administrator.alshow.model.Motor;
import com.example.administrator.alshow.model.PositiveBar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by godlike on 2016/10/25.
 */
public class MyService extends Service {
    private String ifLogin="0";
    private Connection conn;

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    public IBinder onBind(Intent intent) {
        MyBinder myBinder = new MyBinder();
        return myBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    public class MyBinder extends Binder {

        public MyService getMyService() {
            return MyService.this;
        }
    }



    //================================================后台服务======================================
    public Connection conncet() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://172.16.10.111:3306/test?useSSL=false&allowPublicKeyRetrieval=true",
                    "admin",
                    "admin");
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public Groove getGroove(int id){
        Groove groove=new Groove();
        groove.setId(id);
        try {
            conn=conncet();
            PreparedStatement pst=conn.prepareStatement( "select * from positiveBarNow where grooveId=?");
            pst.setInt(1,groove.getId());
            ResultSet rst=pst.executeQuery();
            List<PositiveBar> listA=new ArrayList<>();
            List<PositiveBar> listB=new ArrayList<>();
            while(rst.next()){
                PositiveBar bar=new PositiveBar();
                bar.setGrooveId(rst.getInt(1));
                bar.setId(rst.getInt(2));
                bar.setIfA(rst.getBoolean(3));
                bar.setCurrent(rst.getFloat(4));
                bar.setVoltage(rst.getFloat(5));
                bar.setTempareture(rst.getFloat(6));
                if(bar.isIfA()){
                    listA.add(bar);
                }else{
                    listB.add(bar);
                }
            }
            groove.setBarsOfA(listA);
            groove.setBarsOfB(listB);
            conn.close();
            return groove;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PositiveBar getPositiveBar(int grooveId,int id,boolean ifA){
        PositiveBar bar =new PositiveBar();
        try {
            conn=conncet();
            PreparedStatement pst=conn.prepareStatement( "select * from PositiveBarNow where grooveId=? and id=? and ifA=?");
            pst.setInt(1,grooveId);
            pst.setInt(2,id);
            pst.setBoolean(3,ifA);
            ResultSet rst=pst.executeQuery();
            while(rst.next()){
                bar.setGrooveId(rst.getInt(1));
                bar.setId(rst.getInt(2));
                bar.setIfA(rst.getBoolean(3));
                bar.setCurrent(rst.getFloat(4));
                bar.setVoltage(rst.getFloat(5));
                bar.setTempareture(rst.getFloat(6));
                bar.setMotor(getMotor(rst.getInt(7)));
            }
            conn.close();
            return bar;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Motor getMotor(int id){
        Motor motor=new Motor();
        try {
            conn=conncet();
            PreparedStatement pst=conn.prepareStatement( "select * from motors where id=?");
            pst.setInt(1,id);
            ResultSet rst=pst.executeQuery();
            while(rst.next()){
                motor.setId(rst.getInt(1));
                motor.setSpeed(rst.getInt(2));
                motor.setSteps(rst.getInt(3));
                motor.setCurrents(rst.getInt(4));
                motor.setAcceleration(rst.getInt(5));
                motor.setPosition(rst.getInt(6));
                motor.setAlertStatue(rst.getInt(7));
                motor.setRounds(rst.getInt(8));
                motor.setBackSpeed(rst.getInt(9));
            }
            conn.close();
            return motor;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



    /*
     * 登录
     * */
    public String login(final String userName,final String userPassWord) throws InterruptedException {

        if (!userName.equals("") && !userPassWord.equals("")) {
            //userName and usePassWord exist only,can login
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    conn = conncet();
                    try {
                        PreparedStatement pst = conn.prepareStatement("select count(*) from system_user where(ID=? and pass_words=?)");
                        pst.setString(1, userName);
                        pst.setString(2, userPassWord);
                        ResultSet rst = pst.executeQuery();
                        while (rst.next()) {
                            if (rst.getInt(1) == 1) ifLogin = "1";
//                            System.out.println(ifLogin);
                        }
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        ifLogin = "0";
                    }
                }
            });
            thread.start();
            thread.join();
            return ifLogin;
        }else{
            ifLogin="0";
        }
        return ifLogin;
    }
}
