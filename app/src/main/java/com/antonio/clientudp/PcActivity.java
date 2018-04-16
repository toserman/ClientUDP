package com.antonio.clientudp;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.lang.annotation.RetentionPolicy.SOURCE;
import android.support.v7.widget.SwitchCompat;

public class PcActivity extends AppCompatActivity
        implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    ImageButton turnOffPC_btn, turnOnPC_btn;
    Button test_btn;
    TextView output_txtview;
    String Message;
    SwitchCompat switchCompat;
    static boolean waitResponse;
    static boolean initState;
    boolean flagBound = false; //
    Messenger msgService = null;
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Retention(SOURCE)
    @StringDef({
            TURN_ON,
            TURN_OFF,
            CHECK_CONNECTION,
            TEST
    })
    public @interface CommandName {};
    public static final String TURN_ON = "TurnOn";
    public static final String TURN_OFF = "TurnOff";
    public static final String CHECK_CONNECTION = "CheckConnection";
    public static final String TEST = "TestPacket";
    public static final String TAG = "PcActivity";//MainActivity.class.getName();
    public static int SERVER_DST_PORT = 48656;
    //    public static String SERVER_IP = "172.22.106.57";
//    public static String HOME_PC_IP= SERVER_IP; //Home PC
    public static String SERVER_IP = "192.168.0.106";//Raspberry
    public static String HOME_PC_IP= "192.168.0.102"; //Home PC

    /* For TEST PACKET Button */
    //String TEST_SERVER_IP = "172.22.106.57";
     public static String TEST_SERVER_IP = "192.168.0.106";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pc);

        turnOnPC_btn = findViewById(R.id.img_btn_turnOn_pc);
        turnOffPC_btn = findViewById(R.id.img_btn_turnOff_pc);
        test_btn = findViewById(R.id.btn_test);
        turnOnPC_btn.setOnClickListener(this);
        turnOffPC_btn.setOnClickListener(this);
        test_btn = findViewById(R.id.second_test_btn);
        test_btn.setOnClickListener(this);
        test_btn.setOnClickListener(this);
        output_txtview = findViewById(R.id.pc_tview_log);
        output_txtview.setMovementMethod(new ScrollingMovementMethod());
        waitResponse = true;
        initState = true;
        Log.e(TAG,"onCreate" );

        switchCompat = findViewById(R.id.switch_compat);
        switchCompat.setSwitchPadding(200);
        switchCompat.setOnCheckedChangeListener(this);
        //switchCompat.setChecked(true);
        checkAvailability();
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
                        new SendUDPdata(SERVER_IP,SERVER_DST_PORT,getCommandName()).execute();
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
                    new SendUDPdata(SERVER_IP,SERVER_DST_PORT,getCommandName()).execute();
                    waitResponse = false;
                    output_txtview.append(" Sent command:" + getCommandName() + " " + date + "\n");
                } else {
                    output_txtview.append(" Don't send :" + getCommandName() + " WAITING RESPONSE " + date + "\n");
                }
                break;
            case R.id.second_test_btn:
                 setCommandName(TEST);
                if(waitResponse) {
                    SendUDPdata sendUDPpkt;
                    sendUDPpkt = new SendUDPdata(TEST_SERVER_IP, SERVER_DST_PORT, getCommandName());
                    sendUDPpkt.execute();
                    waitResponse = false;
                    output_txtview.append(" Sent command: " + getCommandName() + " " + date + "\n");
                } else {
                    output_txtview.append(" Don't send: " + getCommandName() + " WAITING RESPONSE " + date + "\n");
                }
                if (flagBound) {
                    //Can be moved in separate thread
                    Log.e(TAG,"Button" );
//                    int check = serverService.getServiceValue();
//                    Log.e(TAG, "PcActivity Button getServiceValue() = "
//                            + Integer.toString(check) );
                }
        }
    }

    public void checkAvailability() {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        setCommandName(CHECK_CONNECTION);
        Log.e(TAG,"checkAvailability()" + " waitResponse = " + Boolean.toString(waitResponse) );
        if(waitResponse) {
            new SendUDPdata(SERVER_IP,SERVER_DST_PORT,getCommandName()).execute();
            waitResponse = false;
            output_txtview.append(" Sent command:" + getCommandName() + " " + date + "\n");
        } else {
            output_txtview.append(" Don't send :" + getCommandName() + " WAITING RESPONSE " + date + "\n");
        }
    }


    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        switch (buttonView.getId()) {
            case R.id.switch_compat:
                Log.e("switch_compat", isChecked + "" + " initState = " + Boolean.toString(initState));
                if (!initState) {
                    if (isChecked) {
                        //Log.e("switch_compat", isChecked + "");
                        setCommandName(TURN_ON);
                        new SendUDPdata(SERVER_IP, SERVER_DST_PORT, getCommandName()).execute();

                    } else {
                       // Log.e("switch_compat", isChecked + "");
                        setCommandName(TURN_OFF);
                        new SendUDPdata(SERVER_IP, SERVER_DST_PORT, getCommandName()).execute();
                    }
                    output_txtview.append(" Sent command:" + getCommandName() + " " + date + "\n");
                }

               break;
        }

    }
    private ServiceConnection sConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            msgService = new Messenger(service);
            try {
                Log.e(TAG,"onServiceConnected()");
                Message msg = android.os.Message.obtain(null,ServerUDPservice.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                msgService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            flagBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //This is called when connection with the service crashed
            Log.e(TAG,"onServiceDisconnected()" );
            flagBound = false;
            msgService = null;

        }
    };
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ServerUDPservice.MSG_SET_STRING_VALUE:
                    String strTest = msg.getData().getString("strFromService");
                    Log.e(TAG,"Incoming Message FROM Service " +
                            "\n" + strTest);
                    Log.e(TAG,"handleMessage() strTest = " + strTest);
                    //TEST
                    String[] newstr = strTest.split("Msg:");
                    String test = newstr[1];
                    Log.e(TAG,"handleMessage() test = " + test);
                    //TODO: Crash when receive just TurnOn or TurnOff
                    if (!test.equals(TURN_ON) && !test.equals(TURN_OFF)) {
                        Log.e(TAG, "handleMessage() point ");
                        String[] endstr = test.split(":");
                        String check0 = endstr[0];
                        String check1 = endstr[1];
                        Log.e(TAG, "handleMessage() test = " + test + " check0 = " + check0 +
                                " check1 = " + check1);
                        if (check0.equals(CHECK_CONNECTION)) {
                            if (check1.equals(TURN_ON)) {
                                switchCompat.setChecked(true);
                                initState = false;
                            }
                            if (check1.equals(TURN_OFF)) {
                                switchCompat.setChecked(false);
                                initState = false;
                            }
                        }
                    }
                    Log.e(TAG, "handleMessage() Before Send TextView strTest = " + strTest);
                    output_txtview.append(strTest + "\n");
                    //waitResponse = true;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG,"********** onStart() ***********" );
        //Bind to serverService
        Intent intent = new Intent(this,ServerUDPservice.class);
        bindService(intent,sConnection,Context.BIND_AUTO_CREATE);
       // checkAvailability();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (flagBound) {
            Log.e(TAG,"onStop() unbindService");
            if (msgService != null) {
                try {
                    Log.e(TAG,"onStop() unbindService ");
                    Message msg = android.os.Message.obtain(null,ServerUDPservice.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    msgService.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            unbindService(sConnection);
            flagBound = false;
        }
    }
    public void setCommandName(@CommandName String name){
        this.Message = name;
    }
    public String getCommandName(){
        return this.Message;
    }

}
