package com.example.administrator.alshow.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Message;

import com.example.administrator.alshow.model.Groove;
import com.example.administrator.alshow.model.OpDiary;
import com.example.administrator.alshow.model.PositiveBar;
import com.example.administrator.alshow.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.administrator.alshow.service.StatusTable.RESUTL_LOGIN_FAIL;
import static com.example.administrator.alshow.service.StatusTable.RESUTL_LOGIN_OK;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    private Connection conn;
    private static UpdateUI updateUI;

    public static final String ACTION_LOGIN = "com.example.administrator.alshow.service.action.LOGIN";
    public static final String ACTION_GETGROOVE = "com.example.administrator.alshow.service.action.GetGroove";
    public static final String ACTION_GETHISTORY = "com.example.administrator.alshow.service.action.GetHistory";
    public static final String ACTION_GETLOGS = "com.example.administrator.alshow.service.action.GetLogs";
    public static final String ACTION_WRITELOG = "com.example.administrator.alshow.service.action.WriteLog";

    public static final String EXTRA_PARAM_USER="com.example.administrator.alshow.service.extra.USER";
    public static final String EXTRA_PARAM_USERNAME = "com.example.administrator.alshow.service.extra.USERNAME";
    public static final String EXTRA_PARAM_PASSWORDS = "com.example.administrator.alshow.service.extra.PASSWORDS";
    public static final String EXTRA_PARAM_GROOVEID = "com.example.administrator.alshow.service.extra.GROOVEID";
    public static final String EXTRA_PARAM_IFA = "com.example.administrator.alshow.service.extra.IFA";
    public static final String EXTRA_PARAM_ANODEID = "com.example.administrator.alshow.service.extra.ANODEID";
    public static final String EXTRA_PARAM_LOGROW = "com.example.administrator.alshow.service.extra.logrow";

    public MyIntentService() {
        super("MyIntentService");
    }

    public static void setUpdateUI(UpdateUI updateUi) {
        updateUI = updateUi;
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionLogin(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_LOGIN);
        intent.putExtra(EXTRA_PARAM_USERNAME, param1);
        intent.putExtra(EXTRA_PARAM_PASSWORDS, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionGetGroove(Context context, String param1) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_GETGROOVE);
        intent.putExtra(EXTRA_PARAM_GROOVEID, param1);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LOGIN.equals(action)) {
                String param1 = intent.getStringExtra(EXTRA_PARAM_USERNAME);
                String param2 = intent.getStringExtra(EXTRA_PARAM_PASSWORDS);
                handleActionLogin(param1, param2);
            } else if (ACTION_GETGROOVE.equals(action)) {
                int param1 = intent.getIntExtra(EXTRA_PARAM_GROOVEID,-1);
                handleActionGrooveId(param1);
            }else if (ACTION_GETHISTORY.equals(action)) {
                int grooveId = intent.getIntExtra(EXTRA_PARAM_GROOVEID,-1);
                boolean ifA=intent.getBooleanExtra(EXTRA_PARAM_IFA,false);
                int anodeId=intent.getIntExtra(EXTRA_PARAM_ANODEID,-1);
                handleActionGetHistory(grooveId,ifA,anodeId);
            }else if(ACTION_GETLOGS.equals(action)){
                String username=intent.getStringExtra(EXTRA_PARAM_USERNAME);
                handleActionReadLog(username);
            }else if(ACTION_WRITELOG.equals(action)){
                OpDiary row=(OpDiary)intent.getSerializableExtra(EXTRA_PARAM_LOGROW);
                handleActionWriteLog(row);
            }
        }
    }


    private void handleActionLogin(String username, String passwords) {
        Message msg=new Message();

        try {
            conn = conncet();
            PreparedStatement pst = conn.prepareStatement("select * from system_user where(ID=? and pass_words=?)");
            pst.setString(1, username);
            pst.setString(2, passwords);
            ResultSet rst = pst.executeQuery();
            int count=0;
            User user=new User();
            while (rst.next()) {
                user.setId(rst.getString(1));
                user.setName(rst.getString(2));
                user.setPassWords(rst.getString(3));
                user.setRank(rst.getInt(4));
                count++;
            }
            if (count == 1){
                msg.what=StatusTable.RESUTL_LOGIN_OK;
                msg.obj=user;
            }else{
                msg.what=StatusTable.RESUTL_LOGIN_FAIL;
            }
            conn.close();
        } catch (SQLException|ClassNotFoundException e) {
            msg.what=StatusTable.WORKNET_ERROR;
        }
        updateUI.updateUi(msg);
    }

    private void handleActionGrooveId(int param1) {
        Message msg=new Message();
        Groove groove=new Groove();
        groove.setPotNo(param1);
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
            msg.what=StatusTable.ACTION_GETGROOVE;
            msg.obj=groove;
        } catch (SQLException|ClassNotFoundException e) {
            //e.printStackTrace();
            msg.what=StatusTable.WORKNET_ERROR;
            msg.obj=null;
        }
        updateUI.updateUi(msg);
    }

    private void handleActionGetHistory(int grooveId,boolean ifA,int anodeId){
        Message msg=new Message();
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
            msg.what=StatusTable.ACTION_GETBARHISTORY;
            msg.obj=anodeHistory;
        } catch (SQLException|ClassNotFoundException e) {
            //e.printStackTrace();
            msg.what=StatusTable.WORKNET_ERROR;
            msg.obj=null;
        }
        updateUI.updateUi(msg);
    }

    private void handleActionReadLog(String username){
        Message msg=new Message();
        List<OpDiary> logs=new ArrayList<>();
        try {
            conn=conncet();
            PreparedStatement pst=conn.prepareStatement( "select * from op_log where user=? order by datetime DESC");
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
            msg.what=StatusTable.ACTION_GETREADLOGS;
            msg.obj=logs;
        } catch (SQLException|ClassNotFoundException e) {
            //e.printStackTrace();
            msg.what=StatusTable.WORKNET_ERROR;
            msg.obj=null;
        }
        updateUI.updateUi(msg);
    }

    private void handleActionWriteLog(OpDiary row){
        PreparedStatement pst= null;
        try {
            conn=conncet();
            pst = conn.prepareStatement( "insert into op_log values(?,?,?,?)");
            pst.setString(1,row.getOpTime());
            pst.setString(2,row.getUsername());
            pst.setInt(3,row.getOpType());
            pst.setString(4,row.getOpObject());
            pst.execute();
            conn.close();
        } catch (SQLException|ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection conncet() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://172.16.10.151:3306/ddrs_db?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true",
                "admin","ABCabc123");
//        conn = DriverManager.getConnection("jdbc:mysql://10.88.3.204:3306/ddrs_db?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true",
//                "administrator","ASDasd123");
        return conn;
    }

    public interface UpdateUI {
        void updateUi(Message message);
    }
}
