package com.example.customremote.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.customremote.CommandServer;
import com.example.customremote.MenuActivity;
import com.example.customremote.R;

import java.time.Instant;

public class TrackpadActivity extends MenuActivity {

    public float g_x;
    public float g_y;
    long clickTime;
    float total_movement;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final CommandServer cs = CommandServer.getInstance();
        setContentView(R.layout.activity_trackpad);
        final SurfaceView surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setOnTouchListener(new View.OnTouchListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            public boolean onTouch(View v, MotionEvent event){
                float x = event.getX();
                float y = event.getY();
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    g_x = x;
                    g_y = y;
                    total_movement = 0;
                    clickTime = System.currentTimeMillis();
                }
                else if(event.getAction() == MotionEvent.ACTION_MOVE) {
                    double delta_x = x - g_x;
                    double delta_y = y - g_y;
                    delta_x = (Math.round(delta_x * 100.0)/100.0);
                    delta_y = (Math.round(delta_y * 100.0)/100.0);
                    total_movement += Math.abs(delta_x) + Math.abs(delta_y);
                    cs.sendMessage("MD:"+delta_x+","+delta_y+"\n");
                    g_x = x;
                    g_y = y;
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    long releaseTime = System.currentTimeMillis();
                    if(total_movement < 10.0){
                        if(releaseTime - clickTime > 500){
                            cs.sendMessage("MRC"+"\n");
                        }
                        else{
                            cs.sendMessage("MLC"+"\n");
                        }
                    }
                }
                return true;
            }
        });

    }
}
