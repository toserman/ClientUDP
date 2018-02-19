package com.antonio.clientudp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by toserman on 2/19/18.
 */

public class ServerUDPservice extends Service {

    public void runServerUDP() {
        Log.e("MY","ServerUDPservice runServerUDP()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags,int startId)
    {
        Log.e("MY","ServerUDPservice onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate()
    {
        Log.e("MY","ServerUDPservice OnCreate()");
        super.onCreate();
    }

    @Override
    public void onDestroy()
    {
        Log.e("MY","ServerUDPservice OnDestroy()");
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Need to implement !!!");
    }
}
