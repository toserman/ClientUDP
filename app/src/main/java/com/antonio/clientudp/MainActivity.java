package com.antonio.clientudp;

import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button send_btn,send_turnOnPC_btn;
    TextView output_txtview;
    //DatagramSocket ds;
    InetAddress IPAddress;
    String Message ;

    @Retention(SOURCE)
    @StringDef({
            TURN_ON,
            TURN_OFF,
            TEST
    })
    public @interface CommandName {};
    public static final String TURN_ON = "TurnOn";
    public static final String TURN_OFF = "TurnOff";
    public static final String TEST = "TestPacket";

    public static final String TAG = "MY";//MainActivity.class.getName();
    public static int PORT = 48656;
    public static String SERVER_IP = "172.22.106.1";
//    public static String SERVER_IP = "192.168.0.106";//Raspberry
//    public static String SERVER_IP = "192.168.0.105";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send_btn = findViewById(R.id.btn_send);
        send_turnOnPC_btn = findViewById(R.id.btn_turnOn_pc);
        send_btn.setOnClickListener(this);
        send_turnOnPC_btn.setOnClickListener(this);
        output_txtview = findViewById(R.id.tview_log);

    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_send:
                setCommandName(TEST);
                new SendUDPdata(SERVER_IP,PORT,getCommandName()).execute();
                output_txtview.append("Sent command:" + getCommandName() + "\n");
//                setCommandName(TURN_OFF);
//                new SendUDPdata(SERVER_IP,PORT,getCommandName()).execute();
                break;
            case R.id.btn_turnOn_pc:
                try	{
                    setCommandName(TURN_ON);
                    new SendUDPdata(SERVER_IP,PORT,getCommandName()).execute();
                    output_txtview.append("Sent command:" + getCommandName() + "\n");
                    Log.e(TAG,"Sending TurnOn to PC");
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

