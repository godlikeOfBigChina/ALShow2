package com.example.administrator.alshow.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by godlike on 2016/10/31.
 */
public class MyServiceConnection implements ServiceConnection {
    public MyService myService;
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        myService=((MyService.MyBinder)service).getMyService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        myService.onUnbind(null);
    }
}
