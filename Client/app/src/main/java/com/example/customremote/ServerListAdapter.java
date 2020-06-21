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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ServerListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<ServerListInfo> list;
    private Context context;

    public ServerListAdapter(ArrayList<ServerListInfo> list, Context context) {
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
        svName.setText(list.get(position).name);
        TextView svIp = (TextView)view.findViewById(R.id.svIp);
        svIp.setText(list.get(position).ip);

        Button conbtn= (Button)view.findViewById(R.id.btn);

        conbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CommandServer cs = CommandServer.getInstance();
                Log.d("dbg_serverlist", list.get(position).name);
                cs.setIp(list.get(position).ip);
                new Thread(cs).start();
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

    public void setList(ArrayList<ServerListInfo> serverInfo) {
        this.list = serverInfo;
    }
}