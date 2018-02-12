package com.antonio.clientudp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class MainActivity extends AppCompatActivity
        implements MyTestCallBack,View.OnClickListener{
    Button test_btn;
    ImageButton turnOffPC_btn, turnOnPC_btn;
    TextView output_txtview;
    ServerUDPthread serverThread;
    public Handler hdThread; //Handler for receiving msg from Server Thread
    static final int UDP_PORT = 48656;
    static boolean waitResponse;
    InetAddress IPAddress;
    String Message ;

    public void callback() {
        Log.e("TEST","MY TEST CALL BACK !!");
    }

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
//    public static String SERVER_IP = "172.22.106.57";
//    public static String HOME_PC_IP= SERVER_IP; //Home PC
    public static String SERVER_IP = "192.168.0.106";//Raspberry
    public static String HOME_PC_IP= "192.168.0.102"; //Home PC

    /* For TEST PACKET Button */
    String TEST_SERVER_IP = "172.22.106.57";
   // public static String TEST_SERVER_IP = "192.168.0.106";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        turnOnPC_btn = findViewById(R.id.img_btn_turnOn_pc);
        turnOffPC_btn = findViewById(R.id.img_btn_turnOff_pc);
        test_btn = findViewById(R.id.btn_test);
        test_btn.setOnClickListener(this);
        turnOnPC_btn.setOnClickListener(this);
        turnOffPC_btn.setOnClickListener(this);
        output_txtview = findViewById(R.id.tview_log);
        output_txtview.setMovementMethod(new ScrollingMovementMethod());

        waitResponse = true;

        //TODO: CAN BE LEAKAGE !! INVESTIGATE !!!
        hdThread = new Handler() {
            public void handleMessage(android.os.Message msg) {
                final String msgRcv = (String)msg.obj;
                Log.e("TAG","RECEIVE MESSAGE FROM THREAD :" + msgRcv);
                super.handleMessage(msg);
            }
        };

    }

    @Override
    public void onClick(View v){
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        switch (v.getId()) {
            case R.id.img_btn_turnOn_pc:
                try	{
                    //TODO: Add Window for asking !!!
                    setCommandName(TURN_ON);
                    if(waitResponse) {
                        new SendUDPdata(SERVER_IP,PORT,getCommandName()).execute();
                        waitResponse = false;
                        output_txtview.append(" Sent command:" + getCommandName() + " " + date + "\n");
                    } else {
                        output_txtview.append(" Don't send :" + getCommandName() + " WAITING RESPONSE " + date + "\n");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.img_btn_turnOff_pc:
                setCommandName(TURN_OFF);
                if(waitResponse) {
                    new SendUDPdata(SERVER_IP,PORT,getCommandName()).execute();
                    waitResponse = false;
                    output_txtview.append(" Sent command:" + getCommandName() + " " + date + "\n");
                } else {
                    output_txtview.append(" Don't send :" + getCommandName() + " WAITING RESPONSE " + date + "\n");
                }
                break;
            case R.id.btn_test:
                Intent test = new Intent(this,PcActivity.class);
                this.startActivity(test);
                setCommandName(TEST);
                if(waitResponse) {
                    SendUDPdata sendUDPpkt;
                    sendUDPpkt = new SendUDPdata(TEST_SERVER_IP, PORT, getCommandName());
                    sendUDPpkt.execute();
                    waitResponse = false;
                    output_txtview.append(" Sent command: " + getCommandName() + " " + date + "\n");
                } else {
                    output_txtview.append(" Don't send: " + getCommandName() + " WAITING RESPONSE " + date + "\n");
                }

                break;
        }
    }


    protected void onStart() {
        serverThread = new ServerUDPthread(this,UDP_PORT,MainActivity.this,hdThread);
        serverThread.setRunning(true);
        serverThread.start();
        String srvRunMsg = "SERVER STARTED IP:" + getIpAddress() + " PORT: " + UDP_PORT + "\n";
        output_txtview.setText(srvRunMsg);
        super.onStart();
    }
    @Override
    protected void onStop() {
        if(serverThread != null){
            serverThread.setRunning(false);
            serverThread = null;
        }
        super.onStop();
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

    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }


}

