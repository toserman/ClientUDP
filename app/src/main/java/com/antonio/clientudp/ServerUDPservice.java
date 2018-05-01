package com.antonio.clientudp;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static java.lang.Boolean.TRUE;

/**
 * Created by toserman on 2/19/18.
 */

public class ServerUDPservice extends IntentService {
    private int srv_port;
    final static String TAG = "ServerUDPservice";
    private final int UDP_SIZE = 65507;//Max size practical size
    DatagramSocket socket;
    volatile boolean run_flag; //TODO:May be not need
    public static final String MAINACTIVITY = "MainActivity";
    /* Data for Messenger interface */
    ArrayList<Messenger> mClients = new ArrayList<Messenger>();
    final Messenger mMessenger = new Messenger(new IncomingHandler());
    static final int MSG_REGISTER_CLIENT = 1;
    static final int MSG_UNREGISTER_CLIENT = 2;
    static final int MSG_SET_INT_VALUE = 3;
    static final int MSG_SET_STRING_VALUE = 4;
    static final int MSG_CHECK_CONNECTION = 5;

    public ServerUDPservice() {
        super("ServerUDPservice");
    }

    class IncomingHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    Log.e(TAG,"handleMessage() MSG_REGISTER_CLIENT");
                    break;
                case MSG_UNREGISTER_CLIENT:
                    Log.e(TAG,"handleMessage() MSG_UNREGISTER_CLIENT");
                    mClients.remove(msg.replyTo);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    protected void onHandleIntent(Intent intent) {
        Log.e(TAG,"onHandleIntent()");
       // testLoop();
        if (intent != null) {
            this.srv_port = intent.getIntExtra("PORT", -1);
            runUDPserver();
            Log.e(TAG, " onHandleIntent()" + "PORT = " + Integer.toString(srv_port));
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
            Log.e(TAG,"RUN FLAG = " + Boolean.toString(run_flag) + " PORT: " + srv_port);
            while (this.run_flag) {
                byte[] buf = new byte[UDP_SIZE];
                Log.e(TAG,"WAIT PACKET!!!!");
                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet); //this code block the program flow

                InetAddress address = packet.getAddress();
                String strIPaddress = address.getHostAddress();//without '/' at the start
                int port = packet.getPort();
                String udp_data = new String(buf, 0, packet.getLength());
                Log.e(TAG,"DATA:" + udp_data);
                Log.e(TAG,"RECEIVE PACKET : " + strIPaddress + ":" + port + " " + udp_data);
                String output = "Server: " + strIPaddress + ":" + port + " Msg:" + udp_data;
                sendMessageToActivity(output);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
                Log.e(TAG,"socket.close()");
            }
        }
    }

    private void sendMessageToActivity(final String strRcv) {
        //Send String data
        for (int i = mClients.size()-1; i >=0; i--) {
            try {
                Bundle b = new Bundle();
                b.putString("strFromService", strRcv);
                if (strRcv.indexOf(PcActivity.CHECK_CONNECTION) > -1) {
                    Log.e(TAG,"sendMessageToActivity() string contains " + PcActivity.CHECK_CONNECTION);
                    Message msg = Message.obtain(null, MSG_CHECK_CONNECTION);
                    msg.setData(b);
                    mClients.get(i).send(msg);
                    Log.e(TAG,"sendMessageToActivity() to MSG_CHECK_CONNECTION ");
                }
            } catch (RemoteException e) {
                mClients.remove(0);
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
                    Log.e(TAG,"SECOND : " + Integer.toString(i));
                }
            }
        }).start();
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG,"OnCreate()");
        super.onCreate();
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG,"OnDestroy()");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG,"OnBind()");
        //return mBinder;
        return mMessenger.getBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.e(TAG,"OnRebind()");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG,"OnUnbind()");
        return super.onUnbind(intent);
    }

    public int getServiceValue() {
        return 9999;
    }
}
