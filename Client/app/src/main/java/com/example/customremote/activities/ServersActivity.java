package com.example.customremote.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.customremote.CommandServer;
import com.example.customremote.MenuActivity;
import com.example.customremote.R;
import com.example.customremote.ServerListInfo;
import com.example.customremote.SpecializedNetworkDiscovery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

class FindServers extends AsyncTask<Void, String, ArrayList<ServerListInfo>>{
    private ServerListAdapter sva;
    FindServers(ServerListAdapter sva){
        this.sva = sva;
    }
    @Override
    protected ArrayList<ServerListInfo> doInBackground(Void... voids) {
        final SpecializedNetworkDiscovery specializedNetworkDiscovery = new SpecializedNetworkDiscovery();
        Thread t = new Thread(specializedNetworkDiscovery);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return specializedNetworkDiscovery.getServerList();
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected void onPostExecute(final ArrayList<ServerListInfo> serverListInfos) {
        super.onPostExecute(serverListInfos);
        sva.setList(serverListInfos);
        sva.notifyDataSetChanged();
    }
}

class ServerListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<ServerListInfo> list;
    private ArrayList<Boolean> favorited = new ArrayList<>();
    private Context context;
    private ServersActivity app_context;

    public Boolean getFavorited(int index) {
        return favorited.get(index);
    }

    public void setFavorited(int index, Boolean f) {
        favorited.set(index, f);
    }

    public void genFavorited(){
        favorited.clear();
        for(ServerListInfo i: list){
            if (app_context.isInFavorites(i))
                favorited.add(true);
            else
                favorited.add(false);
        }
    };


    public ServerListAdapter(ArrayList<ServerListInfo> list, Context context, ServersActivity app_context) {
        this.list = list;
        this.context = context;
        this.app_context = app_context;
        genFavorited();
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
        svName.setText(list.get(position).getName());
        TextView svIp = (TextView)view.findViewById(R.id.svIp);
        svIp.setText(list.get(position).getIp());

        Button conBtn= (Button)view.findViewById(R.id.btn);
        Button favBtn= (Button)view.findViewById(R.id.btnFav);

        final ServersActivity c = (ServersActivity)parent.getContext();
        ArrayList<ServerListInfo> a = c.getFavoritesFromPreferences();
        boolean f = false;
        for(ServerListInfo i: a){
            if (i.getIp().equals(list.get(position).getIp())){
                f = true;
                break;
            }
        }
        if(f){
            favBtn.setBackgroundResource(R.drawable.star);
        }


        conBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CommandServer cs = CommandServer.getInstance();
                if(!cs.isRunning()) {
                    cs.setIp(list.get(position).getIp());
                    new Thread(cs).start();
                }
            }
        });
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFavorited(position)){
                    c.removeFavoriteFromPreferences(list.get(position));
                }
                else{
                    Log.d("dbg_watsup", "clicky add");
                    c.addFavoriteToPreferences(list.get(position));
                }
                c.refreshLists();
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
        genFavorited();
    }
}

public class ServersActivity extends MenuActivity {
    private static ArrayList<ServerListInfo> favoriteList = new ArrayList<>();
    private static ArrayList<ServerListInfo> serverSearch = new ArrayList<>();
    ServerListAdapter sva, fva;

    void refreshLists(){
        ListView lv = findViewById(R.id.listview);
        ListView flv = findViewById(R.id.listviewFav);
        favoriteList = getFavoritesFromPreferences();
        fva = new ServerListAdapter(favoriteList, this.getApplicationContext(), this);
        //if(serverSearch.size() == 0)
        //    sva = new ServerListAdapter(serverSearch, this.getApplicationContext(), this);
        sva.notifyDataSetChanged();
        fva.notifyDataSetChanged();
        sva.genFavorited();
        lv.setAdapter(sva);
        flv.setAdapter(fva);
    }

    ArrayList<ServerListInfo> getFavoritesFromPreferences(){
        try {
            SharedPreferences sharedPref = ServersActivity.this.getPreferences(Context.MODE_PRIVATE);
            String resp = sharedPref.getString("fav_list","[]");
            JSONArray j = new JSONArray(resp);
            ArrayList<ServerListInfo> r = new ArrayList<>();
            for(int i = 0; i < j.length(); i++){
                JSONArray jj = (JSONArray) j.get(i);
                String ip = jj.getString(0);
                String name = jj.getString(1);
                r.add(new ServerListInfo(ip, name));
            }
            return r;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    void addFavoriteToPreferences(ServerListInfo fav){
        ArrayList<ServerListInfo> favs = getFavoritesFromPreferences();
        boolean found = false;
        for(ServerListInfo s: favs){
            if(s.getIp().equals(fav.getIp())){
                s.setName(fav.getName());
                found = true;
            }
        }
        if(!found)
            favs.add(fav);
        setFavoritesToPreferences(favs);
    }

    void removeFavoriteFromPreferences(ServerListInfo fav){
        ArrayList<ServerListInfo> favs = getFavoritesFromPreferences();
        Log.d("dbg_www","removin");
        int index = -1;
        for(int i = 0; i < favs.size(); i++){
            if (favs.get(i).getIp().equals(fav.getIp())){
                index = i;
                break;
            }
        }
        if(index > -1){
            favs.remove(index);
        }
        Log.d("dbg_www","removed");
        setFavoritesToPreferences(favs);
    }

    boolean isInFavorites(ServerListInfo f){
        for(ServerListInfo i: getFavoritesFromPreferences()){
            if (i.getIp().equals(f.getIp()) && i.getName().equals(f.getName())){
                return true;
            }
        }
        return false;
    }

    void setFavoritesToPreferences(ArrayList<ServerListInfo> favs){
        SharedPreferences sharedPref = ServersActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        ArrayList<ArrayList<String>> a = new ArrayList<>();
        for(ServerListInfo i : favs){
            a.add(i.toArrayList());
        }
        JSONArray j = new JSONArray(a);
        editor.putString("fav_list", String.valueOf(j)).apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setTitle("Server List");
        setContentView(R.layout.activity_main);
        final Button rb = findViewById(R.id.refresh_button);

        sva = new ServerListAdapter(serverSearch, this.getApplicationContext(), this);
        refreshLists();

        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FindServers findServers = new FindServers(sva);
                findServers.execute();
            }
        });

        //rb.callOnClick();
    }
}
