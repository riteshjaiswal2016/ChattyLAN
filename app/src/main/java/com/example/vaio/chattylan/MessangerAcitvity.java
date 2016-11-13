package com.example.vaio.chattylan;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessangerAcitvity extends AppCompatActivity {
    DataOutputStream dataOutputStream;
    final int PORT = 7000;
    final String TAG = "tag";
    Socket socket1;
    EditText messageBoxText;
    TextView recievedMsgText,databaseText;
    static String chosenHostIp;
    static DBHandler dbHandler;

    Handler activityHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG,msg.obj.toString());
        }
    };

    Messenger activityMessenger = new Messenger(activityHandler);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messanger_acitvity);
        Log.i(TAG,"msgr act wo");
        dbHandler = new DBHandler(MessangerAcitvity.this,null);

       // databaseText = (TextView)findViewById(R.id.databaseText);
        recievedMsgText = (TextView)findViewById(R.id.recievedMsgText);
        messageBoxText = (EditText)findViewById(R.id.messageBoxText);

        Bundle bundle = this.getIntent().getExtras();
        chosenHostIp = bundle.getString("chosenHostIp");

        Intent intent =new Intent(MessangerAcitvity.this,ServerService.class);
        intent.putExtra("Messenger",activityMessenger);

        startService(intent);
        Log.i(TAG,"activity created , :"+chosenHostIp);
    }


    public void onSendClick(View view){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm a");
        String currentDateTimeString = sdf.format(new Date());

        dbHandler.addRow(chosenHostIp,1,messageBoxText.getText().toString(),currentDateTimeString);


        SentFragment.sentMsgAryList.add(chosenHostIp+":"+currentDateTimeString+" :"+"\n"
                                        +messageBoxText.getText().toString());

        SentFragment.adapter.notifyDataSetChanged();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket1 = new Socket(chosenHostIp, PORT);

                    dataOutputStream = new DataOutputStream(socket1.getOutputStream());

                    dataOutputStream.writeUTF(messageBoxText.getText().toString());

                    dataOutputStream.close();
                    socket1.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }}).start();
    }
}
