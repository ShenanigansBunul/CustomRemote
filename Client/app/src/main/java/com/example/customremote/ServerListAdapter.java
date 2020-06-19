package com.example.customremote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ServerListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;

    public ServerListAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.server_list_element, null);
        }

        TextView svName= (TextView)view.findViewById(R.id.svName);
        svName.setText(list.get(position));

        Button conbtn= (Button)view.findViewById(R.id.btn);

        conbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CommandServer cs = CommandServer.getInstance();
                Log.d("dbg", String.valueOf(v.getId()));
                //cs.setIp(v.)
                //new Thread(cs).start();
                //cs.sendMessage("test msg");
                //cs.sendMessage("test msg 2");
            }
        });
        /*addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                notifyDataSetChanged();
            .
            }
        });*/

        return view;
    }
}