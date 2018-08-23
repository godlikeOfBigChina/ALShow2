package com.example.administrator.alshow.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.administrator.alshow.model.Groove;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


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
            PreparedStatement pst=conn.prepareStatement( "select * from PostiveBar where grooveId=?");
            pst.setInt(1,groove.getId());
            ResultSet rst=pst.executeQuery();
            while(rst.next()){

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groove;
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
                            System.out.println(ifLogin);
                        }
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
