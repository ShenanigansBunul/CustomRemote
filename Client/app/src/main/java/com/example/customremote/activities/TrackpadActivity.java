package com.example.customremote.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.customremote.CommandServer;
import com.example.customremote.MenuActivity;
import com.example.customremote.R;

import org.w3c.dom.Text;

import java.time.Instant;

public class TrackpadActivity extends MenuActivity {

    public float g_x;
    public float g_y;
    long clickTime;
    boolean sent = false;
    float total_movement;
    String lastText;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setTitle(R.string.trackpad);
        final CommandServer cs = CommandServer.getInstance();
        setContentView(R.layout.activity_trackpad);
        final SurfaceView surfaceView = findViewById(R.id.surfaceView);
        final EditText keyboardInput = findViewById(R.id.keyboardInput);
        surfaceView.setOnTouchListener(new View.OnTouchListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            public boolean onTouch(View v, MotionEvent event){
                float x = event.getX();
                float y = event.getY();
                keyboardInput.setText("");
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
        keyboardInput.setSingleLine();
        keyboardInput.setImeOptions(EditorInfo.IME_ACTION_SEND);
        keyboardInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    cs.sendMessage("KEY:13\n");
                    sent = true;
                    keyboardInput.setText("");
                }
                return false;
            }
        });
        keyboardInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                int q = newText.length() - lastText.length();

                if(q < 0)
                    for (; q < 0; q++)
                        if(!sent)
                            cs.sendMessage("KEY:8\n");
                        else
                            sent = false;
                else
                    for(; q > 0; q--)
                        cs.sendMessage("WRT:"+String.valueOf(newText.charAt(newText.length()-q))+"\n");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
