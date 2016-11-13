package com.example.vaio.chattylan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashScreen extends AppCompatActivity {
    Thread t;
    Handler handler;
    final String TAG = "tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

    }


    @Override
    protected void onStart()
    {
        super.onStart();

        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                AlertDialog.Builder builder = (AlertDialog.Builder)msg.obj;
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        };

       Thread p =new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (networkInfo.isConnected()) {
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    finish();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                    builder.setTitle("Caution!");
                    builder.setMessage("Please Connect to a Network ").setCancelable(false);

                    builder.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });

                    Message msg=Message.obtain();
                    msg.obj=builder;
                    handler.sendMessage(msg);
                }
            }});

        p.start();
    }
}

