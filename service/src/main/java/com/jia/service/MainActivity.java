package com.jia.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jia.service.service.LocalBindService;
import com.jia.service.service.SampleService;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    private LocalBindService localBindService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //startService
//        Intent startIntent = new Intent(this, SampleService.class);
//        startService(startIntent);

        //bindService
        Intent bindIntent = new Intent(this, LocalBindService.class);
        bindService(bindIntent,this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("jys","MainActivity - > onDestroy()...");
//        Intent intent = new Intent(this, SampleService.class);
//        stopService(intent);
        unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        LocalBindService.LocalBinder binder = (LocalBindService.LocalBinder) iBinder;
        localBindService = binder.getService();
        localBindService.setCallBack(new LocalBindService.CallBack() {
            @Override
            public void call(String state) {
                Log.i("jys","state:" + state);
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        localBindService = null;
    }
}
