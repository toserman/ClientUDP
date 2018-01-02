package com.antonio.clientudp;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button send_btn, send_btn1;
    DatagramSocket ds;
//    InetAddress IPAddress;

    private static final String TAG = "MY";//MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send_btn = (Button)findViewById(R.id.btn_send);
        send_btn1 = (Button)findViewById(R.id.btn_send2);
        send_btn.setOnClickListener(this);
        send_btn1.setOnClickListener(this);

        //TODO: NEED TO MOVE !!!
        try {
            ds = new DatagramSocket(48655);
            //IPAddress = InetAddress.getByName("172.22.11.182");
//            IPAddress = InetAddress.getByName("192.168.0.106");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //FOR AVOID NETWORK EXCEPTION
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }
    public void send_UDP()
    {
        String dataText = "Hello";
        //String port = "4445";
        String port = "48655";
        String host = "192.168.0.106";
        //String host = "172.22.11.182";
        String uriString = "udp://" + host + ":" + port + "/";
        uriString += Uri.encode(dataText);

        Toast.makeText(getApplicationContext(), "Send UDP message new_send_UDP() ", Toast.LENGTH_SHORT).show();
        Uri uri = Uri.parse(uriString);
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            Log.e(TAG,"Button: SEND UDP to : " + uri);
            startActivity(intent);
        } catch (Exception e) {
            String data = e.getMessage();
            Log.e(TAG,"DATA MESSAGE: " + data)                                                                                                                   ;
        }
    }

    public void send_UDP_msg() {
              Toast.makeText(getApplicationContext(), "Send UDP message", Toast.LENGTH_LONG).show();
                //DatagramSocket ds = null;
                String Message = "Android Message";
                try {
//                      InetAddress IPAddress = InetAddress.getByName("172.22.11.182");
                      InetAddress IPAddress = InetAddress.getByName("192.168.0.106");

                        Log.d("MY","IPAddress " + IPAddress);
                        DatagramPacket dp = null;
                       dp = new DatagramPacket(Message.getBytes(), Message.length(), IPAddress, 48655);
            //         ds.setBroadcast(true);
                       ds.send(dp);
                   } catch (Exception e) {
                        e.printStackTrace();
                    }
    }
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_send:
                send_UDP();
                break;
            case R.id.btn_send2:
                send_UDP_msg();
                break;
        }


    }
}
