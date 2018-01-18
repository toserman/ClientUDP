package com.antonio.clientudp;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button test_btn;
    ImageButton turnOffPC_btn, turnOnPC_btn;
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
//    public static String SERVER_IP = "172.22.106.1";
    public static String SERVER_IP = "192.168.0.106";//Raspberry
    public static String HOME_PC_IP= "192.168.0.102"; //Home PC
//    public static String SERVER_IP = "192.168.0.105";

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
    }

    @Override
    public void onClick(View v){
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String TEST_SERVER_IP = "172.22.105.254";
        switch (v.getId()) {
            case R.id.img_btn_turnOn_pc:
                try	{
                    //TODO: Add Window for asking !!!

                    setCommandName(TURN_ON);
                    new SendUDPdata(SERVER_IP,PORT,getCommandName()).execute();
                    output_txtview.append(" Sent command:" + getCommandName() + " "
                                          + date +"\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.img_btn_turnOff_pc:
                //setCommandName(TEST);
//                new SendUDPdata(SERVER_IP,PORT,getCommandName()).execute();
//                String dataText = "TurnOff";
//                String port = "48655";
//                String host = "192.168.0.102";
                setCommandName(TURN_OFF);
                new SendUDPdata(HOME_PC_IP,PORT,getCommandName()).execute();
                output_txtview.append(" Sent command:" + getCommandName() + " "
                                      + date + "\n");
//                setCommandName(TURN_OFF);
//                new SendUDPdata(SERVER_IP,PORT,getCommandName()).execute();
                break;
            case R.id.btn_test:
                setCommandName(TEST);

                new SendUDPdata(TEST_SERVER_IP,PORT,getCommandName()).execute();
                output_txtview.append(" Sent command:" + getCommandName() + " "
                        + date + "\n");
//                setCommandName(TURN_OFF);
//                new SendUDPdata(SERVER_IP,PORT,getCommandName()).execute();
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

