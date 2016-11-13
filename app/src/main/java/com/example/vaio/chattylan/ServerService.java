package com.example.vaio.chattylan;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.widget.ArrayAdapter;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;

public class ServerService extends Service {
    ServerSocket serverSocket;
    Socket socket;
    DataInputStream dataInputStream;
    final int PORT = 7000;
    Messenger messenger;
    final String TAG="tag";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Log.i(TAG,"Got messenger on service");
        messenger = intent.getParcelableExtra("Messenger");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(PORT);

                    while (true) {
                        socket = serverSocket.accept();
                        dataInputStream = new DataInputStream(socket.getInputStream());

                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                        String msg = (dataInputStream.readUTF());

                        StringBuffer stringBuffer = new StringBuffer(socket.getInetAddress().toString());
                        stringBuffer.deleteCharAt(0);

                        MessangerAcitvity.dbHandler.addRow(stringBuffer.toString() , 0 , msg,currentDateTimeString);




                        RecievedFragment.adapter = new ArrayAdapter<>(RecievedFragment.view.getContext(),android.R.layout.simple_list_item_1,
                                RecievedFragment.recievedMsgAryList);

                        RecievedFragment.recievedMsgList.setAdapter(RecievedFragment.adapter);

                        RecievedFragment.recievedMsgAryList.add(stringBuffer+":"+currentDateTimeString+" :"+"\n"+msg);


                        RecievedFragment.adapter.notifyDataSetChanged();

                        //Log.i(TAG,RecievedFragment.recievedMsgAryList.get(RecievedFragment.recievedMsgAryList.size()-1));
                        Log.i(TAG,"hello");

                        Message clientmessage = Message.obtain();
                        clientmessage.obj=msg;

                        messenger.send(clientmessage);

                        dataInputStream.close();
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }}).start();

        return super.onStartCommand(intent, flags, startId);
    }

}
