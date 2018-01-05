package com.antonio.clientudp;

import android.os.AsyncTask;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class SendUDPdata extends AsyncTask<Void, Void, Void> {

    private int udpPort;
    private String SendMsg;
    private DatagramSocket DgrmSocket;
    private String ipAddress;

    public SendUDPdata(String inpIpaddress, int port, String Message) {
        this.udpPort = port;
        this.SendMsg = Message;
        this.ipAddress = inpIpaddress;
    }

    protected Void doInBackground(Void... arg0) {
        try {
            DatagramPacket dp = null;
            InetAddress IPAddress = InetAddress.getByName(ipAddress);
            Log.d(MainActivity.TAG, "Send to " + IPAddress + " Command: " + SendMsg );
            dp = new DatagramPacket(SendMsg.getBytes(), SendMsg.length(), IPAddress, udpPort);

            DgrmSocket = new DatagramSocket(null);
            DgrmSocket.setReuseAddress(true);
            DgrmSocket.setBroadcast(true);
            DgrmSocket.bind(new InetSocketAddress(this.udpPort));

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