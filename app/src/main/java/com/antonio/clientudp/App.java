package com.antonio.clientudp;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

/**
 * Created by toserman on 2/25/18.
 */

public class App extends Application {
    public final static String TAG = "Application class";
    static final int SERVER_UDP_PORT = 48656;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"onCreate()");
        //Run service. UDP server for listening packets from server
        Intent iService = new Intent(this,ServerUDPservice.class);
        iService.putExtra("PORT",SERVER_UDP_PORT);
        startService(iService);
    }
}
