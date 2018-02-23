package com.antonio.clientudp;

import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class PcActivity extends AppCompatActivity
        implements View.OnClickListener{

    ImageButton turnOffPC_btn, turnOnPC_btn;
    TextView output_txtview;
    String Message;
    static boolean waitResponse;

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
        setContentView(R.layout.activity_pc);

        turnOnPC_btn = findViewById(R.id.img_btn_turnOn_pc);
        turnOffPC_btn = findViewById(R.id.img_btn_turnOff_pc);
        turnOnPC_btn.setOnClickListener(this);
        turnOffPC_btn.setOnClickListener(this);
        output_txtview = findViewById(R.id.pc_tview_log);
        output_txtview.setMovementMethod(new ScrollingMovementMethod());
        waitResponse = true;
        Log.e("TAG", "PcActivity onCreate" );
    }

    @Override
    public void onClick(View v) {
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
        }
    }
    public void setCommandName(@CommandName String name){
        this.Message = name;
    }
    public String getCommandName(){
        return this.Message;
    }

}
