package com.antonio.clientudp;

import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button send_btn,send_wol_btn;
    //DatagramSocket ds;
    InetAddress IPAddress;
    String Message ;

    @Retention(SOURCE)
    @StringDef({
            TURN_ON,
            TURN_OFF
    })
    public @interface CommandName {};
    public static final String TURN_ON = "TurnOn";
    public static final String TURN_OFF = "TurnOff";

    public static final String TAG = "MY";//MainActivity.class.getName();
    public static int PORT = 48656;
    public static String SERVER_IP = "172.22.106.1";
//    public static String SERVER_IP = "192.168.0.106";//Raspberry
//    public static String SERVER_IP = "192.168.0.105";
    public static final String MAC_WOL = "90:2B:34:6A:19:0D";
    public static final String IP_WOL = "192.168.255.255";

    String macStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send_btn = findViewById(R.id.btn_send);
        send_wol_btn = findViewById(R.id.btn_send_wol);
        send_btn.setOnClickListener(this);
        send_wol_btn.setOnClickListener(this);

    }

    public void send_UDP_msg() {
                try {
//                      InetAddress IPAddress = InetAddress.getByName("172.22.11.182"); //Desktop
//                      InetAddress IPAddress = InetAddress.getByName("192.168.0.106");
                        InetAddress IPAddress = InetAddress.getByName("172.22.105.107");

                    Toast.makeText(getApplicationContext(), "send_UDP_msg UDP message to " + IPAddress, Toast.LENGTH_LONG).show();
                        Log.d(TAG,"IPAddress " + IPAddress);
                        DatagramPacket dp = null;
                       dp = new DatagramPacket(Message.getBytes(), Message.length(), IPAddress, PORT);
            //         ds.setBroadcast(true);
                     //  ds.send(dp);
                   } catch (Exception e) {
                        e.printStackTrace();
                    }
    }
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_send:
              //  send_UDP_msg();
                setCommandName(TURN_ON);
                new SendUDPdata(SERVER_IP,PORT,getCommandName()).execute();
//                setCommandName(TURN_OFF);
//                new SendUDPdata(SERVER_IP,PORT,getCommandName()).execute();
                break;
            case R.id.btn_send_wol:
                try	{
                    macStr = SendWOL.cleanMac(MAC_WOL);
                    Log.e(TAG,"Sending WOL to: " + macStr);
                    SendWOL.send(macStr, IP_WOL);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    protected void onPause() {
        Log.e("TAG", "STATE onPause" );
        super.onPause();
    }
    @Override
    protected void onResume() {
        Log.e("TAG", "STATE onResume" );
        super.onResume();
    }

    public void setCommandName(@CommandName String name){
        this.Message = name;
    }
    public String getCommandName(){
        return this.Message;
    }


}

