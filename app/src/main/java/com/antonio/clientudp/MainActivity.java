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

    private static final String TAG = "MY";//MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send_btn = (Button)findViewById(R.id.btn_send);
        send_btn.setOnClickListener(this);

        //TODO: NEED TO MOVE !!!
        try {
            ds = new DatagramSocket(48656);
            //IPAddress = InetAddress.getByName("172.22.11.182");
//            IPAddress = InetAddress.getByName("192.168.0.106");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //FOR AVOID NETWORK EXCEPTION
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

//    @Retention(SOURCE)
//    @StringDef({
//            POWER_SERVICE,
//            WINDOW_SERVICE,
//            LAYOUT_INFLATER_SERVICE
//    })
//    public @interface ServiceName {}
//    public static final String POWER_SERVICE = "power";
//    public static final String WINDOW_SERVICE = "window";
//    public static final String LAYOUT_INFLATER_SERVICE = "layout_inflater";
//  ...
//    public abstract Object getSystemService(@ServiceName String name);


    public void send_UDP_msg() {
                //DatagramSocket ds = null;
                String Message = "Android Message";
                try {
//                      InetAddress IPAddress = InetAddress.getByName("172.22.11.182"); //Desktop
                      InetAddress IPAddress = InetAddress.getByName("192.168.0.106");
                     //   InetAddress IPAddress = InetAddress.getByName("172.22.106.125");
                    Toast.makeText(getApplicationContext(), "send_UDP_msg UDP message to " + IPAddress, Toast.LENGTH_LONG).show();
                        Log.d("MY","IPAddress " + IPAddress);
                        DatagramPacket dp = null;
                       dp = new DatagramPacket(Message.getBytes(), Message.length(), IPAddress, 48656);
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
                send_UDP_msg();
                break;
        }


    }
}
