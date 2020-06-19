package com.example.customremote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServersActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CommandServer cs = CommandServer.getInstance();

        ArrayList<String> list = new ArrayList<String>(Arrays.asList("192.168.1.9,222,333,444,555,666".split(",")));
        final ListView lv = findViewById(R.id.listview);
        lv.setAdapter(new ServerListAdapter(list, this.getApplicationContext()));


        Log.d("dbg", "test msg sent");
    }
}
