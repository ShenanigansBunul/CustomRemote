package com.example.customremote.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.customremote.MenuActivity;
import com.example.customremote.R;
import com.example.customremote.Remote;
import com.example.customremote.RemoteButton;
import com.example.customremote.RemoteButtonAction;
import com.example.customremote.ServerListInfo;
import com.example.customremote.RemoteListJsonConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class RemoteListAdapter extends BaseAdapter implements ListAdapter {

    private final ArrayList<Remote> list;
    private final Context context;

    public RemoteListAdapter(ArrayList<Remote> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.remote_list_element, null);
        }

        final Button useButton = view.findViewById(R.id.btnUse);
        final Button editButton = view.findViewById(R.id.btnEdit);
        final TextView nameText = view.findViewById(R.id.remoteName);
        final TextView sizeText = view.findViewById(R.id.remoteSize);

        nameText.setText(list.get(position).getName());
        String nByN = list.get(position).getWidth() + " x " + list.get(position).getHeight();
        sizeText.setText(nByN);

        useButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RemoteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("remote_name", list.get(position).getName());
                intent.putExtra("all_data",RemoteListJsonConverter.remoteListToJson(list));
                context.getApplicationContext().startActivity(intent);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditRemoteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("remote_name", list.get(position).getName());
                intent.putExtra("all_data",RemoteListJsonConverter.remoteListToJson(list));
                context.getApplicationContext().startActivity(intent);
            }
        });

        return view;
    }
}

public class RemotesActivity extends MenuActivity {
    RemoteListAdapter rla;
    ArrayList<Remote> remotes;

    ArrayList<Remote> getRemotesFromPreferences() {
        SharedPreferences sharedPref = RemotesActivity.this.getPreferences(Context.MODE_PRIVATE);
        String resp = sharedPref.getString("remote_list", "{}");
        return RemoteListJsonConverter.jsonToRemoteList(resp);
    }

    void setRemotesToPreferences(ArrayList<Remote> rems) {
        SharedPreferences sharedPref = RemotesActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String x = RemoteListJsonConverter.remoteListToJson(rems);
        editor.putString("remote_list", x).apply();
    }

    ArrayList<Remote> getUpdatedRemotes(){
        Intent i = getIntent();
        String updated = i.getStringExtra("updated_list");
        if(updated != null) {
            ArrayList<Remote> newRemotes = RemoteListJsonConverter.jsonToRemoteList(updated);
            setRemotesToPreferences(newRemotes);
            return newRemotes;
        }
        return getRemotesFromPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        remotes = getUpdatedRemotes();
        rla.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remotes);
        this.getSupportActionBar().setTitle("Remote List");
        remotes = getUpdatedRemotes();
        rla = new RemoteListAdapter(remotes, this.getApplicationContext());
        final ListView lv = findViewById(R.id.listviewRemotes);
        Button add = findViewById(R.id.addRemoteBtn);
        lv.setAdapter(rla);
        rla.notifyDataSetChanged();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(RemotesActivity.this);
                dialog.setContentView(R.layout.add_remote_alert);
                final EditText n = dialog.findViewById(R.id.editName);
                final EditText w = dialog.findViewById(R.id.editWidth);
                final EditText h = dialog.findViewById(R.id.editHeight);
                Button ok = dialog.findViewById(R.id.createBtn);
                Button cancel = dialog.findViewById(R.id.cancelBtn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int wi = Integer.parseInt(w.getText().toString());
                        int he = Integer.parseInt(h.getText().toString());
                        if (wi > 0 && wi < 20 && he > 0 && he < 20) {
                            Remote newRemote = new Remote(n.getText().toString(), wi, he);
                            remotes.add(newRemote);
                            setRemotesToPreferences(remotes);
                        }
                        runOnUiThread(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                rla = new RemoteListAdapter(remotes, RemotesActivity.this.getApplicationContext());
                                lv.setAdapter(rla);
                                rla.notifyDataSetChanged();
                            }
                        }));
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        });
    }
}
