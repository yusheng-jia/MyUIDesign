package com.jia.service.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SampleService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("jys","SampleService onCreate ... ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("jys","SampleService onStartCommand ... ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("jys","SampleService onDestroy ... ");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void doSomething(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
