package com.example.administrator.alshow;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.alshow.service.MyService;
import com.example.administrator.alshow.service.MyServiceConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends Activity implements OnClickListener {
    // 声明控件对象

    private EditText et_name, et_pass;
    private Button mLoginButton;
    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button bt_pwd_eye;
    private TextWatcher username_watcher;//编辑框监听器
    private TextWatcher password_watcher;

    private String userName, userID;
    private MyService myService;
    private MyServiceConnection connection = new MyServiceConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        Intent intent1 = new Intent(getApplicationContext(), MyService.class);
        bindService(intent1, connection, BIND_AUTO_CREATE);
        et_name = (EditText) findViewById(R.id.username);
        et_pass = (EditText) findViewById(R.id.password);

        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
        bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eye);
        bt_username_clear.setOnClickListener(this);
        bt_pwd_clear.setOnClickListener(this);
        bt_pwd_eye.setOnClickListener(this);
        initWatcher();
        et_name.addTextChangedListener(username_watcher);
        et_pass.addTextChangedListener(password_watcher);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(this);

        findViewById(R.id.login_all).setOnClickListener(this);
    }

    @Override
    public void finish() {
        Intent intent = new Intent(this, MyService.class);
        unbindService(connection);
        stopService(intent);
        super.finish();
    }

    //edittext监听
    private void initWatcher() {
        username_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                et_pass.setText("");
                if (s.toString().length() > 0) {
                    bt_username_clear.setVisibility(View.VISIBLE);
                } else {
                    bt_username_clear.setVisibility(View.INVISIBLE);
                }
            }
        };

        password_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                } else {
                    bt_pwd_clear.setVisibility(View.INVISIBLE);
                }
            }
        };
    }


    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login_all:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                break;

            case R.id.login_button:
                try {
                    login();
                } catch (NoSuchAlgorithmException|InterruptedException e) {
//                            e.printStackTrace();
                    Toast.makeText(LoginActivity.this,"加密出错",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_username_clear:
                et_name.setText("");
                et_pass.setText("");
                break;

            case R.id.bt_pwd_clear:
                et_pass.setText("");
                break;

            case R.id.bt_pwd_eye:
                if (et_pass.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    bt_pwd_eye.setBackgroundResource(R.drawable.login_button_eye_s);
                    et_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                } else {
                    bt_pwd_eye.setBackgroundResource(R.drawable.login_button_eye_n);
                    et_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                et_pass.setSelection(et_pass.getText().toString().length());
                break;
        }
    }

    /**
     * 登陆
     */
    private void login() throws NoSuchAlgorithmException, InterruptedException {
        myService = connection.myService;
        String name, code;
        name = et_name.getText().toString();
        code = et_pass.getText().toString();
        //password is needed to decode
        String decodeResult = getMD5(code);
        if (!name.equals("") && !code.equals("")) {
            this.userName = name;
            this.userID = myService.login(name, decodeResult);
            if (!this.userID.equals("0")) {
                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, Activity2.class);
                intent.putExtra("userName", userName);
                intent.putExtra("userID", userID);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "用户名或密码为空！", Toast.LENGTH_SHORT).show();
        }
        if (name.equals("admin") && code.equals("admin")) {
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, Activity2.class);
            intent.putExtra("userName", userName);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
//        stopService(new Intent(this,MyService.class));
        super.onPause();
    }

    private long firstTime = 0;

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            finish();
        }
    }

    //对密码进行加密的方法
    public String getMD5(String passwd) throws NoSuchAlgorithmException{
        byte[] todayMD5 = MessageDigest.getInstance("MD5").digest(passwd.getBytes());
        StringBuilder s=new StringBuilder();
        for(byte b:todayMD5){
            s.append(Byte.toString(b));
        }
//        System.out.println(s.toString());
        return s.toString();
    }
}

