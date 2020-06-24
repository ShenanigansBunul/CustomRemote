package com.example.customremote;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.customremote.activities.RemotesActivity;
import com.example.customremote.activities.ScreenActivity;
import com.example.customremote.activities.ServersActivity;
import com.example.customremote.activities.TrackpadActivity;

public abstract class MenuActivity extends AppCompatActivity {
    void switchToActivity(Class act){
        Intent intent = new Intent(this, act);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.remotes:
                switchToActivity(RemotesActivity.class);
                return true;
            case R.id.servers:
                switchToActivity(ServersActivity.class);
                return true;
            case R.id.screen:
                switchToActivity(ScreenActivity.class);
                return true;
            case R.id.trackpad:
                switchToActivity(TrackpadActivity.class);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
}
