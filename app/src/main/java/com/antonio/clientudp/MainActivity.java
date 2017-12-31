package com.antonio.clientudp;

import android.content.Intent;
import android.net.Uri;
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
    Button send_btn;

    private static final String TAG = "MY";//MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send_btn = (Button)findViewById(R.id.btn_send);
        send_btn.setOnClickListener(this);

    }
    public void new_send_UDP()
    {
        String dataText = "Hello";
        String port = "4445";
        String host = "192.168.0.106";
        String uriString = "udp://" + host + ":" + port + "/";
        uriString += Uri.encode(dataText);

        Toast.makeText(getApplicationContext(), "Send UDP message new_send_UDP() ", Toast.LENGTH_LONG).show();
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivity(intent);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_send:
                Log.e(TAG,"Button: SEND UDP");
            break;
        }


    }
}
