package com.example.vaio.chattylan;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.audiofx.BassBoost;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

public class MainActivity extends AppCompatActivity {
    final String TAG = "tag";
    ListAdapter adapter;
    List<String> arrayList;
    ListView ipList;
    Handler handler1;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pd = new ProgressDialog(MainActivity.this);
        pd.setIndeterminate(false);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        arrayList = new ArrayList<>();

        handler1= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.arg1 == 1) {
                    Intent hostListInetent = new Intent(MainActivity.this,HostListActivity.class);

                    hostListInetent.putExtra("HostList",(ArrayList<String>)arrayList);
                    startActivity(hostListInetent);
                    finish();
                }
                else {
                    arrayList.add(msg.obj.toString());
                    Log.i(TAG,msg.obj.toString()+" Added");
                }
            }};


        Thread b= new Thread(
                new Runnable() {
                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    @Override
                    public void run() {

                        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);

                        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
                        int ipAddress = dhcpInfo.ipAddress;
                        int gateway = dhcpInfo.gateway;
                        String gateways = Formatter.formatIpAddress(gateway);
                        String ips = Formatter.formatIpAddress(ipAddress);
                        Log.i(TAG,""+ipAddress);
                        Log.i(TAG,""+gateways);

                        try {
                            InetAddress inetAddress =InetAddress.getByName(ips);
                            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);
                            Log.i(TAG,""+networkInterface.getInterfaceAddresses().get(1).getNetworkPrefixLength());
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (SocketException e) {
                            e.printStackTrace();
                        }

                        int ipa= (ipAddress &(0xFF));
                        int ipb= (ipAddress &(0xFF00))>>8;
                        int ipc= (ipAddress &(0xFF0000))>>16;
                        int ipd= (ipAddress &(0xFF000000))>>24;

                        int netlen=24;

                        String ipas8bit =String.format("%8s",Integer.toBinaryString(ipa)).replace(' ', '0');
                        String ipbs8bit =String.format("%8s",Integer.toBinaryString(ipb)).replace(' ', '0');
                        String ipcs8bit =String.format("%8s",Integer.toBinaryString(ipc)).replace(' ', '0');
                        String ipds8bit =String.format("%8s",Integer.toBinaryString(ipd)).replace(' ', '0');

                        String ipbinstring = ipas8bit+ipbs8bit+ipcs8bit+ipds8bit;

                        char[] ipchararray = ipbinstring.toCharArray();

                        char[] netaddresschararr =new char[netlen];

                        for(int i=0;i<=netlen-1;i++)
                            netaddresschararr[i]=ipchararray[i];

                        String netaddstring=new String(netaddresschararr);

                        double lastadd = pow(2,32-netlen)-1;

                        for(double i=1;i<20;i++) {
                            pd.setMax((int) (lastadd-1));
                            pd.setProgress((int) i);
                            if(i==lastadd-1)
                                pd.dismiss();

                            String hostaddstring = String.format("%" + (32 - netlen) + "s", Integer.toBinaryString((int) i)).replace(' ', '0');

                            String fullip = netaddstring.concat(hostaddstring);

                            Long parsedipadd = Long.parseLong(fullip, 2);

                            int parsedipadda = (int) ((parsedipadd & (0xFF000000))>>24);
                            int parsedipaddb = (int) ((parsedipadd & (0x00FF0000))>>16);
                            int parsedipaddc = (int) ((parsedipadd & (0x0000FF00))>>8);
                            int parsedipaddd = (int) ((parsedipadd & (0x000000FF)));

                            String fullips = parsedipadda+"."+parsedipaddb+"."+parsedipaddc+"."+parsedipaddd;

                            if(fullips.equals(gateways))
                                continue;

                            try {
                                Log.i(TAG, "Checking..." + fullips);
                                Message ipmsg = Message.obtain();

                                if (InetAddress.getByName(fullips).isReachable(100)) {
                                    Log.i(TAG, fullips + " is Reachable.");
                                    ipmsg.obj = fullips;
                                    ipmsg.arg2 = 0;
                                    ipmsg.arg1 = 0;

                                    handler1.sendMessage(ipmsg);
                                }
                            } catch (UnknownHostException e1) {
                                e1.printStackTrace();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }

                        Message listmsg = Message.obtain();
                        listmsg.arg1 = 1;
                        handler1.sendMessage(listmsg);
                    }
                });

        b.setPriority(Thread.MAX_PRIORITY);
        b.start();
     }



}
