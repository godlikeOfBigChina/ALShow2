package com.example.administrator.alshow.service;

import android.os.Message;

import com.example.administrator.alshow.model.Groove;
import com.example.administrator.alshow.model.OpDiary;
import com.example.administrator.alshow.model.PositiveBar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by godlike on 2016/10/25.
 */
public class MyService{
    private Connection conn;

    //================================================后台服务======================================
    public Connection conncet() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://172.16.10.151:3306/ddrs_db?useSSL=false&allowPublicKeyRetrieval=false",
            "admin","ABCabc123");
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Groove getGroove(int id){
        Groove groove=new Groove();
        groove.setPotNo(id);
        try {
            conn=conncet();
            PreparedStatement pst=conn.prepareStatement( "SELECT g.POT_NO,g.POT_NAME,g.IP_ADDR,b.ROD_NO,b.ROD_NAME,b.SIDE_TYPE,d.CURRENT,d.VOLTAGE,d.TEMPERATURE,d.MEASURE_TIME  FROM pot_measure_data d,rod_info b,pot_info g,\n" +
                    "(SELECT MAX(dd.MEASURE_TIME) AS maxt FROM pot_measure_data dd) AS t\n" +
                    "WHERE g.POT_NO=? AND g.POT_NO=d.POT_NO AND g.POT_NO=b.POT_NO AND d.MEASURE_TIME=t.maxt\n" +
                    "GROUP BY b.ROD_NO;");
            pst.setInt(1,groove.getPotNo());
            ResultSet rst=pst.executeQuery();
            List<PositiveBar> listA=new ArrayList<>();
            List<PositiveBar> listB=new ArrayList<>();
            while(rst.next()){
                groove.setPotNo(rst.getInt("POT_NO"));
                groove.setPotName(rst.getString("POT_NAME"));
                groove.setIpAddress(rst.getString("IP_ADDR"));
                PositiveBar bar=new PositiveBar();
                bar.setGrooveId(rst.getInt("POT_NO"));
                bar.setId(rst.getInt("ROD_NO"));
                bar.setIfA(rst.getString("SIDE_TYPE").equals("A"));
                bar.setCurrent(rst.getFloat("CURRENT"));
                bar.setVoltage(rst.getFloat("VOLTAGE"));
                bar.setTempareture(rst.getFloat("TEMPERATURE"));
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

    public List<PositiveBar> getPositiveBarHistory(int grooveId,boolean ifA,int anodeId){
        List<PositiveBar> anodeHistory=new ArrayList<>();
        try {
            conn=conncet();
            PreparedStatement pst=conn.prepareStatement( "select * from pot_measure_data where POT_NO=? and ROD_NO=?");
            pst.setInt(1,grooveId);
            pst.setInt(2,ifA?anodeId:anodeId+24);
            ResultSet rst=pst.executeQuery();
            while(rst.next()){
                PositiveBar bar =new PositiveBar();
                bar.setGrooveId(grooveId);
                bar.setIfA(ifA);
                bar.setId(anodeId);
                bar.setDatetime(new Date(rst.getTimestamp("MEASURE_TIME").getTime()));
                bar.setCurrent(rst.getFloat("CURRENT"));
                bar.setVoltage(rst.getFloat("VOLTAGE"));
                bar.setTempareture(rst.getFloat("TEMPERATURE"));
                anodeHistory.add(bar);
//                Log.e("**********",bar.getDatetime().toLocaleString());
            }
            conn.close();
            return anodeHistory;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<OpDiary> getReadLog(String username){
        List<OpDiary> logs=new ArrayList<>();
        try {
            conn=conncet();
            PreparedStatement pst=conn.prepareStatement( "select * from op_log where user=?");
            pst.setString(1,username);
            ResultSet rst=pst.executeQuery();
            while(rst.next()){
                OpDiary opRow=new OpDiary();
                opRow.setOpTime(rst.getString("datetime"));
                opRow.setUsername(rst.getString("user"));
                opRow.setOpType(rst.getInt("type"));
                opRow.setOpObject(rst.getString("content"));
                logs.add(opRow);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    public void handleActionWriteLog(OpDiary row){
        conn=conncet();
        PreparedStatement pst= null;
        try {
            pst = conn.prepareStatement( "insert into op_log values(?,?,?,?)");
            pst.setString(1,row.getOpTime());
            pst.setString(2,row.getUsername());
            pst.setInt(3,row.getOpType());
            pst.setString(4,row.getOpObject());
            pst.execute();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
