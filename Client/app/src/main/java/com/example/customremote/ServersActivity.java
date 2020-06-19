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
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ServersActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tv = (TextView) findViewById(R.id.textView);
        Log.d("dbg", "0");
        //new Thread(new CommandServer()).start();
        CommandServer cs = CommandServer.getInstance();
        new Thread(cs).start();
        //while(!cs.isRunning());
        cs.sendMessage("zarzavat");
        cs.sendMessage("zarzavat 2");
        Log.d("dbg", "o iesit ba");
        //Client tc = new Client("192.168.1.9",5800);
        //tc.startServer();
    }
}
