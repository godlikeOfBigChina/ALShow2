package com.example.administrator.alshow.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.example.administrator.alshow.model.Groove;
import com.example.administrator.alshow.model.Motor;
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

    public static final String EXTRA_PARAM_USERNAME = "com.example.administrator.alshow.service.extra.USERNAME";
    public static final String EXTRA_PARAM_PASSWORDS = "com.example.administrator.alshow.service.extra.PASSWORDS";
    public static final String EXTRA_PARAM_GROOVEID = "com.example.administrator.alshow.service.extra.GROOVEID";
    public static final String EXTRA_PARAM_IFA = "com.example.administrator.alshow.service.extra.IFA";
    public static final String EXTRA_PARAM_ANODEID = "com.example.administrator.alshow.service.extra.ANODEID";

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
            }
        }
    }


    private void handleActionLogin(String username, String passwords) {
        conn = conncet();
        Message msg=new Message();
        msg.what=StatusTable.ACTION_LOGIN;
        msg.obj=ACTION_LOGIN;
        try {
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
                msg.arg1=RESUTL_LOGIN_OK;
//              System.out.println(ifLogin);
                msg.obj=user;
            }else{
                msg.arg1=RESUTL_LOGIN_FAIL;
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            msg.arg1=StatusTable.RESUTL_LOGIN_FAIL;
        }
        updateUI.updateUi(msg);
    }

    private void handleActionGrooveId(int param1) {
        Message msg=new Message();
        msg.what=StatusTable.ACTION_GETGROOVE;
        Groove groove=new Groove();
        groove.setId(param1);
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
            msg.obj=groove;
        } catch (SQLException e) {
            e.printStackTrace();
            msg.obj=null;
        }
        updateUI.updateUi(msg);
    }

    private void handleActionGetHistory(int grooveId,boolean ifA,int anodeId){
        Message msg=new Message();
        msg.what=StatusTable.ACTION_GETBARHISTORY;
        List<PositiveBar> anodeHistory=new ArrayList<>();

        try {
            conn=conncet();
            PreparedStatement pst=conn.prepareStatement( "select * from historyBar " +
                    "where grooveId=? and ifA=? and id=? and datetime like '2018-07-15 08:%'");
            pst.setInt(1,grooveId);
            pst.setInt(2,ifA?1:0);
            pst.setInt(3,anodeId);
            ResultSet rst=pst.executeQuery();
            while(rst.next()){
                PositiveBar bar =new PositiveBar();
                bar.setGrooveId(rst.getInt(1));
                bar.setIfA(rst.getBoolean(2));
                bar.setId(rst.getInt(3));
                bar.setDatetime(new Date(rst.getTimestamp(4).getTime()));
                bar.setCurrent(rst.getFloat(5));
                bar.setVoltage(rst.getFloat(6));
                bar.setTempareture(rst.getFloat(7));
                anodeHistory.add(bar);
//                Log.e("**********",bar.getDatetime().toLocaleString());
            }
            conn.close();
            msg.obj=anodeHistory;
        } catch (SQLException e) {
            e.printStackTrace();
            msg.obj=null;
        }
        updateUI.updateUi(msg);
    }

    public Connection conncet() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://172.16.10.151:3306/test?useSSL=false&allowPublicKeyRetrieval=true",
                    "admin","ABCabc123");
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface UpdateUI {
        void updateUi(Message message);
    }
}
