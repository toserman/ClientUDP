package com.antonio.clientudp;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity
        implements MyTestCallBack,View.OnClickListener{
    Button test_btn;
    TextView main_txtview;
    TextView testFb_txtview; //For Firebase condition
    Button fbSunny_btn, fbFoggy_btn;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mconditioRef = mRootRef.child("condition");
   // FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference myRef = database.getReference("condition");


    ServerUDPthread serverThread;
    static final int UDP_PORT = 48656;
    static boolean waitResponse;

    public void callback() {
        Log.e("TEST","MainActivity MY TEST CALL BACK !!");
    }

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

        test_btn = findViewById(R.id.btn_test);
        test_btn.setOnClickListener(this);
        main_txtview = findViewById(R.id.main_tview_log);
        main_txtview.setMovementMethod(new ScrollingMovementMethod());

        testFb_txtview = findViewById(R.id.tviewCondition);
        fbFoggy_btn = findViewById(R.id.btn_Foggy);
        fbSunny_btn = findViewById(R.id.btn_Sunny);

        waitResponse = true;

        //myRef.setValue("RRRRRRRRR");
    }

    @Override
    public void onClick(View v){
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        switch (v.getId()) {
            case R.id.btn_test:
                Log.e("MY","MainActivity Press Test Button");
                Intent test1 = new Intent(this,PcActivity.class);
                this.startActivity(test1);

//                setCommandName(TEST);
//                if(waitResponse) {
//                    SendUDPdata sendUDPpkt;
//                    sendUDPpkt = new SendUDPdata(TEST_SERVER_IP, PORT, getCommandName());
//                    sendUDPpkt.execute();
//                    waitResponse = false;
//                    output_txtview.append(" Sent command: " + getCommandName() + " " + date + "\n");
//                } else {
//                    output_txtview.append(" Don't send: " + getCommandName() + " WAITING RESPONSE " + date + "\n");
//                }
                break;
            case R.id.btn_Foggy:
                break;
            case R.id.btn_Sunny:
                break;



        }
    }

    protected void onStart() {
//        serverThread = new ServerUDPthread(this,UDP_PORT,MainActivity.this);
//        serverThread.setRunning(true);
//        serverThread.start();
//        Log.d(TAG,"SERVER STARTED IP:" + getIpAddress() + " PORT: " + UDP_PORT + "\n");
//        String srvRunMsg = "SERVER STARTED IP:" + getIpAddress() + " PORT: " + UDP_PORT + "\n";
//        main_txtview.setText(srvRunMsg);

        Log.e("TAG", "STATE onStart" );
          mconditioRef.addValueEventListener(new ValueEventListener() {
           // myRef.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                  Log.e("TAG", "onDataChange() Firebase");
                  String text = dataSnapshot.getValue(String.class);
                  Log.e("TAG", "onDataChange() Firebase text: " + text);
                  testFb_txtview.setText(text);
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });
        super.onStart();


    }
//    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.e("TAG", "onReceive() Data from Service:");
//            String text = intent.getStringExtra("SERVICE_DATA");
//            Log.e("TAG", "onReceive() Data from Service:" + text );
//           // mDateText.setText(date);
//        }
//    };
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
        // unregister local broadcast
        super.onPause();
    }
    @Override
    protected void onResume() {
        Log.e("TAG", "STATE onResume" );
//        // register local broadcast
//        IntentFilter filter = new IntentFilter(ServerUDPservice.MAINACTIVITY);
//        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
        super.onResume();
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

