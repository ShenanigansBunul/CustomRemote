package com.example.customremote;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ServersActivity extends MenuActivity {
    private static ArrayList<ServerListInfo> serverInfo = new ArrayList<>();
    ServerListAdapter sva;
    boolean stop = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button rb = findViewById(R.id.refresh_button);
        ListView lv = findViewById(R.id.listview);
        this.getSupportActionBar().setTitle("Server List");

        sva = new ServerListAdapter(serverInfo, this.getApplicationContext());
        lv.setAdapter(sva);

        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SpecializedNetworkDiscovery specializedNetworkDiscovery = new SpecializedNetworkDiscovery();
                Thread t = new Thread(specializedNetworkDiscovery);
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                serverInfo = specializedNetworkDiscovery.getServerList();
                ServersActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sva.setList(serverInfo);
                        sva.notifyDataSetChanged();
                    }
                });
            }
        });

        //rb.callOnClick();
    }
}
