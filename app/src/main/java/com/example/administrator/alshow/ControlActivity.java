package com.example.administrator.alshow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.administrator.alshow.model.Configuration;
import com.example.administrator.alshow.model.Groove;
import com.example.administrator.alshow.model.MotorImpl;
import com.example.administrator.alshow.model.OpDiary;
import com.example.administrator.alshow.model.User;
import com.example.administrator.alshow.service.MotorDriver;

import com.example.administrator.alshow.service.MyIntentService;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ControlActivity extends AppCompatActivity implements MyIntentService.UpdateUI{
    private final int REQUEST_CODE_SCAN=1;
    private Button allOut,allIn;
    private Button scan;
    private Spinner rodId;
    private ToggleButton isAbtn;
    private Button singleOut,singleIn;
    private MotorImpl motor;
    private MotorDriver driver;
    private boolean isOk;
    private View.OnClickListener listener;
    private TextView potNo;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        user=(User)getIntent().getSerializableExtra(MyIntentService.EXTRA_PARAM_USER);
        listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.scan){
                    Intent intent=new Intent(getBaseContext(),CaptureActivity.class);
                    ZxingConfig config = new ZxingConfig();
                    config.setShowbottomLayout(true);
                    config.setPlayBeep(true);
                    config.setShake(true);
                    config.setShowAlbum(false);
                    config.setShowFlashLight(true);
                    intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                }
                if ("".equals(potNo.getText().toString())){
                    Toast.makeText(getBaseContext(),"请扫描槽号",Toast.LENGTH_SHORT).show();
                }else{
                    switch (v.getId()){
                        case R.id.all_out:
                            multiOprate(false,true,false,true);
                            break;
                        case R.id.all_in:
                            multiOprate(false,true,false,false);
                            break;

                        case R.id.single_out:
                            singleOperate(true);
                            break;
                        case R.id.single_in:
                            singleOperate(false);
                            break;
                    }
                }
            }
        };
        allOut=(Button)findViewById(R.id.all_out);
        allOut.setOnClickListener(listener);
        allIn=(Button)findViewById(R.id.all_in);
        allIn.setOnClickListener(listener);
        singleOut=(Button)findViewById(R.id.single_out);
        singleOut.setOnClickListener(listener);
        singleIn=(Button)findViewById(R.id.single_in);
        singleIn.setOnClickListener(listener);
        rodId=(Spinner) findViewById(R.id.bar_id);
        isAbtn=(ToggleButton) findViewById(R.id.isA);
        scan=(Button)findViewById(R.id.scan);
        scan.setOnClickListener(listener);
        potNo=(TextView)findViewById(R.id.pot_no);

    }

    private void multiOprate(boolean isSingle,boolean isBoth,boolean isA,boolean isOut){
        Thread thread=new Thread(new Runnable() {
            boolean rtv;
            @Override
            public void run() {
                driver=new MotorDriver();
                try {
                    rtv=driver.drive(isSingle,isBoth,isA,isOut,-1);
                    Intent intent=new Intent(getBaseContext(),MyIntentService.class);
                    intent.setAction(MyIntentService.ACTION_WRITELOG);
                    OpDiary row= new OpDiary();
                    SimpleDateFormat format=new SimpleDateFormat("yyyMMdd HH:mm:ss");
                    row.setOpTime(format.format(new Date()));
                    row.setUsername(user.getId());
                    row.setOpType(isOut?3:4);
                    row.setOpObject(potNo.getText().toString());
                    intent.putExtra(MyIntentService.EXTRA_PARAM_LOGROW,row);
                    startService(intent);
                } catch (Exception e) {
                    rtv=false;
                }
                isOk=rtv;
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            isOk=false;
        }
        Toast.makeText(getBaseContext(),isOk?"操作成功":"操作异常",Toast.LENGTH_SHORT).show();
    }

    private void singleOperate(boolean isOut){
        int id=Integer.parseInt(rodId.getSelectedItem().toString());
        boolean isA = isAbtn.isChecked();
        if(id<0||id>24){
            Toast.makeText(getBaseContext(), "导杆编号不存在", Toast.LENGTH_SHORT).show();
        }else {
            Thread thread = new Thread(new Runnable() {
                boolean rtv;

                @Override
                public void run() {
                    motor = new MotorImpl(id, isA);
                    try {
                        rtv = isOut ? motor.forward() : motor.backward();
                    } catch (Exception e) {
                        rtv = false;
                    }
                    isOk = rtv;
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                isOk = false;
            }
            Intent intent=new Intent(getBaseContext(),MyIntentService.class);
            intent.setAction(MyIntentService.ACTION_WRITELOG);
            OpDiary row= new OpDiary();
            SimpleDateFormat format=new SimpleDateFormat("yyyMMdd HH:mm:ss");
            row.setOpTime(format.format(new Date()));
            row.setUsername(user.getId());
            row.setOpType(isOut?3:4);
            String content=potNo.getText().toString();
            content+=isA?"A":"B"+id;
            row.setOpObject(content);
            intent.putExtra(MyIntentService.EXTRA_PARAM_LOGROW,row);
            startService(intent);
            Toast.makeText(getBaseContext(), isOk ? "操作成功" : "操作异常", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String rst=data.getStringExtra(Constant.CODED_CONTENT);
                potNo.setText(rst);
                Intent intentService=new Intent(getBaseContext(),MyIntentService.class);
                intentService.setAction(MyIntentService.ACTION_GETGROOVE);
                intentService.putExtra(MyIntentService.EXTRA_PARAM_GROOVEID,Integer.parseInt(potNo.getText().toString()));
                startService(intentService);
                MyIntentService.setUpdateUI(ControlActivity.this);
            }
        }
    }

    @Override
    public void updateUi(Message message) {
        Groove groove=(Groove) message.obj;
        Configuration.ip=groove.getIpAddress();
    }
}
