package com.jia.service.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class LocalBindService extends Service {

    private LocalBinder iBinder = new LocalBinder();

    private CallBack callBack;
    public LocalBindService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //do something
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    callBack.call("complete");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("jys","onBind...");
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("jys","onUnbind...");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i("jys","onDestroy...");
        super.onDestroy();
    }

    public class  LocalBinder extends Binder{
        public LocalBindService getService(){
            return  LocalBindService.this;
        }
    }


    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }

    public interface CallBack{
        void call(String state);
    }


}
