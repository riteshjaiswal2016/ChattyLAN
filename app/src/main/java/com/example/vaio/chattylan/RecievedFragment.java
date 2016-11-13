package com.example.vaio.chattylan;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RecievedFragment extends Fragment {
    final String TAG = "tag";
    static ListView recievedMsgList;
    static ArrayList<String> recievedMsgAryList;
    ArrayList<String> recievedMsgAryList1;
    static ArrayAdapter<String> adapter;
    static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.recieved_fragment,container,false);

        recievedMsgList = (ListView)view.findViewById(R.id.recievedMsgList);
        recievedMsgList.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        recievedMsgList.setStackFromBottom(true);

        recievedMsgAryList =new ArrayList<>();

        adapter = new ArrayAdapter<>(view.getContext(),android.R.layout.simple_list_item_1,recievedMsgAryList);

        recievedMsgList.setAdapter(adapter);

        Log.i(TAG,"rcv created");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG,"notify rcv");
        recievedMsgAryList1 = MessangerAcitvity.dbHandler.recieveQuery(MessangerAcitvity.chosenHostIp);

        for(int i=0;i<recievedMsgAryList1.size();i++){
            recievedMsgAryList.add(recievedMsgAryList1.get(i));
            adapter.notifyDataSetChanged();
        }


    }
}
