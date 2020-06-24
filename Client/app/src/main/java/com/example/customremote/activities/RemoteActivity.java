package com.example.customremote.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

import java.lang.reflect.Array;
import java.util.ArrayList;


public class RemoteActivity extends AppCompatActivity {
    int width;
    int height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);
        LinearLayout mainLayout = findViewById(R.id.mainList);
        this.getSupportActionBar().setTitle("Remote (NAME HERE)");
        CommandServer cs = CommandServer.getInstance();

        width = 3;
        height = 3;

        for(int i = 0; i < height; i++){
            LinearLayout row = new LinearLayout(this);
            mainLayout.addView(row);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1.0f
            );
            row.setLayoutParams(param);
            row.setOrientation(LinearLayout.HORIZONTAL);
            for(int j = 0; j < width; j++){
                Button btn = new Button(this);
                LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                btn.setLayoutParams(param2);
                row.addView(btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

        }

    }
}
