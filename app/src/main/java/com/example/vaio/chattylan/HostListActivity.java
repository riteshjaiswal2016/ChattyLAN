package com.example.vaio.chattylan;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HostListActivity extends AppCompatActivity {
    ListView hostList;
    ListAdapter adapter;
    List<String> arrayList;
    final String TAG = "tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_list);

        arrayList = (ArrayList<String>) this.getIntent().getSerializableExtra("HostList");

        int k =arrayList.size();
        String[] hostListArray =  arrayList.toArray(new String[k]);

        Log.i(TAG,""+k);
        hostList = (ListView)findViewById(R.id.hostList);
        adapter = new ArrayAdapter<>(HostListActivity.this, android.R.layout.simple_list_item_1, hostListArray);
        hostList.setAdapter(adapter);

        hostList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HostListActivity.this,MessangerAcitvity.class);
                String chosenHostIp = (String) hostList.getItemAtPosition(position);
                Log.i(TAG,chosenHostIp);
                intent.putExtra("chosenHostIp",chosenHostIp);
                startActivity(intent);
                Log.i(TAG,"Messenger activity started");
            }
        });


    }
}
