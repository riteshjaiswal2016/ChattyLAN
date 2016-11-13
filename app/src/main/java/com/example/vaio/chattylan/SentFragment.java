package com.example.vaio.chattylan;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SentFragment extends Fragment {
    final String TAG ="tag";
    ListView sentMsgList;
    static ArrayList<String> sentMsgAryList;
    static ArrayAdapter<String> adapter;
    ArrayList<String> sentMsgAryList1;

    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sent_fragment,container,false);

        sentMsgList = (ListView)view.findViewById(R.id.sentMsgList);
        sentMsgList.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        sentMsgList.setStackFromBottom(true);

        sentMsgAryList =new ArrayList<>();

        adapter = new ArrayAdapter<>(view.getContext(),android.R.layout.simple_list_item_1,sentMsgAryList);

        sentMsgList.setAdapter(adapter);

        Log.i(TAG,"SentFragement created");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sentMsgAryList1 = MessangerAcitvity.dbHandler.sentQuery(MessangerAcitvity.chosenHostIp);

        for(int i=0;i<sentMsgAryList1.size();i++){
            sentMsgAryList.add(sentMsgAryList1.get(i));
            adapter.notifyDataSetChanged();
        }

        //adapter.notifyDataSetChanged();
        Log.i(TAG,"notify sent");
    }

}
