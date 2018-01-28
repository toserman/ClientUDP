package com.antonio.clientudp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import static android.content.ContentValues.TAG;

public class ServerUDPthread extends Thread {
    private int srv_port;
    private final int UDP_SIZE = 65507;//Max size practical size
    DatagramSocket socket;
    volatile boolean run_flag;
    TextView txt_output;
    Context context;
    Handler hd;
    MyTestCallBack test;

    public ServerUDPthread (MyTestCallBack check,int port, Context con, Handler inpHd){
        this.context = con;
        this.srv_port = port;
        this.hd = inpHd;
        this.test = check;
    }

    public void setRunning(boolean flag) {
        this.run_flag = flag;
    }

    public void run() {
        try {
            txt_output = ((Activity)context).findViewById(R.id.tview_log);

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
                test.callback(); //TESTTTTTT
//                if (udp_data.equals(MainActivity.TURN_ON) || udp_data.equals(MainActivity.TURN_OFF))
//                {
                    MainActivity.waitResponse = true;
               // }
                updateOutput(output + "\n");//Update TextView in UI
//                new ActionTask().execute(udp_data);

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
    /*Update TextView in main thread*/
    private void updateOutput(final String text) {
        Handler handler = new Handler(context.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                txt_output.append(text);
            }
        });
    }

}
