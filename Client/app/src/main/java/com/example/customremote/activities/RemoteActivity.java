package com.example.customremote.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.customremote.CommandServer;
import com.example.customremote.R;
import com.example.customremote.Remote;
import com.example.customremote.RemoteButton;
import com.example.customremote.RemoteListJsonConverter;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class RemoteActivity extends AppCompatActivity {

    Remote getByName(ArrayList<Remote> rms, String name){
        for(Remote r: rms){
            if (r.getName().equals(name))
                return r;
        }
        return null;
    }

    int width;
    int height;
    ArrayList<Remote> remotes;
    Remote currentRemote;
    ArrayList<RemoteButton> buttons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);
        LinearLayout mainLayout = findViewById(R.id.mainList);

        CommandServer cs = CommandServer.getInstance();

        Intent intent = getIntent();
        String remoteDataJson = intent.getStringExtra("all_data");
        if(remoteDataJson != null) {
            String remoteName = intent.getStringExtra("remote_name");
            remotes = RemoteListJsonConverter.jsonToRemoteList(remoteDataJson);
            currentRemote = getByName(remotes, remoteName);
            buttons = currentRemote.getButtons();
        }

        this.getSupportActionBar().setTitle("Remote: "+currentRemote.getName());
        width = currentRemote.getWidth();
        height = currentRemote.getHeight();

        for(int i = 0; i < width; i++){
            LinearLayout row = new LinearLayout(this);
            mainLayout.addView(row);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1.0f
            );
            row.setLayoutParams(param);
            row.setOrientation(LinearLayout.HORIZONTAL);
            for(int j = 0; j < height; j++){
                Button btn = new Button(this);
                LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                btn.setLayoutParams(param2);
                row.addView(btn);
                final int bIndex = i * height + j;
                RemoteButton thisButton = buttons.get(bIndex);
                btn.setText(thisButton.getText());
                if(thisButton.getActions().size() < 1){
                    btn.setClickable(false);
                    btn.setFocusable(false);
                    btn.setVisibility(View.INVISIBLE);
                }
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Thread t = new Thread(buttons.get(bIndex));
                        t.start();
                    }
                });
            }
        }
    }
}
