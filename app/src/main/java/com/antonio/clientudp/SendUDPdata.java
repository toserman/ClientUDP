package com.antonio.clientudp;

import android.os.AsyncTask;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendUDPdata extends AsyncTask<Void, Void, Void> {

    private int udpPort;
    private String SendMsg;
    private DatagramSocket DgrmSocket;
    private InetAddress IPAddress;

    public SendUDPdata(DatagramSocket Socket, InetAddress Ipaddress, int port, String Message) {
        this.udpPort = port;
        this.SendMsg = Message;
        this.DgrmSocket = Socket;
        this.IPAddress = Ipaddress;
    }

    protected Void doInBackground(Void... arg0) {

        try {
            Log.d(MainActivity.TAG, "Send to " + IPAddress + " Command: " + SendMsg );
            DatagramPacket dp = null;
            dp = new DatagramPacket(SendMsg.getBytes(), SendMsg.length(), IPAddress, udpPort);
            DgrmSocket.send(dp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Log.d("MY","ASYNC TASK WAS FINISHED !!!");
    }
}