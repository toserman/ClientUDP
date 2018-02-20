package com.antonio.clientudp;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.ViewDebug;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static java.lang.Boolean.TRUE;

/**
 * Created by toserman on 2/19/18.
 */

public class ServerUDPservice extends IntentService {

    private int srv_port;
    private final int UDP_SIZE = 65507;//Max size practical size
    DatagramSocket socket;
    volatile boolean run_flag; //TODO:May be not need

    public ServerUDPservice() {
        super("ServerUDPservice");
    }
    @Override
    //NO NEED FOR IntentService()
//    public int onStartCommand(Intent intent, int flags,int startId)
//    {
//        Log.e("MY","ServerUDPservice onStartCommand()");
//        testLoop();
//        return super.onStartCommand(intent, flags, startId);
//    }

    protected void onHandleIntent(Intent intent) {
        Log.e("MY","ServerUDPservice onHandleIntent()");
        //testLoop();
        if (intent != null) {
            this.srv_port = intent.getIntExtra("PORT",-1);
            runUDPserver();
            Log.e("MY","ServerUDPservice onHandleIntent()" + "PORT = " + Integer.toString(srv_port));
            // Do whatever you need to do here.
        }
    }

    public void runUDPserver() {
        this.run_flag = TRUE;
        try {
            if (socket == null) {
                socket = new DatagramSocket(null);
                socket.setReuseAddress(true);
                socket.setBroadcast(true);
                socket.bind(new InetSocketAddress(this.srv_port));
            }
            Log.e(TAG, "RUN FLAG = " + Boolean.toString(run_flag) + " PORT: " +srv_port);
            while(this.run_flag) {
                byte[] buf = new byte[UDP_SIZE];
                Log.e(TAG,"WAIT PACKET!!!!");
                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet); //this code block the program flow

                Log.e(TAG,"RECEIVED PACKET!!!!");

                InetAddress address = packet.getAddress();
                String strIPaddress = address.getHostAddress();//without '/' at the start

                int port = packet.getPort();

                String udp_data = new String(buf,0,packet.getLength());
                Log.e(TAG,"DATA:" + udp_data);

                Log.e(TAG, "RECEIVE PACKET : " + strIPaddress + ":" + port + " " + udp_data);
                String output = "Request from: " + strIPaddress + ":" + port + " Data:" + udp_data;
                Log.e(TAG, "TEST: " + output);
//                MainActivity.waitResponse = true;
                // }
                //updateOutput(output + "\n");//Update TextView in UI
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(socket != null){
                socket.close();
                Log.e(TAG, "socket.close()");
            }
        }
    }
    public void testLoop() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 6; i++) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    Toast.makeText(this,"SECOND:" + Integer.toString(i),Toast.LENGTH_LONG).show();
                    Log.e("MY","SECOND : " + Integer.toString(i));
                }
            }
        }).start();
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
