package com.example.customremote.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.customremote.actions.KeyPress;
import com.example.customremote.actions.MouseClick;
import com.example.customremote.actions.Wait;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public View getView(int position, View convertView, ViewGroup parent) {
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
                intent.putExtra("activity_name", nameText.getText().toString());
                context.getApplicationContext().startActivity(intent);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditRemoteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("activity_name", nameText.getText().toString());
                context.getApplicationContext().startActivity(intent);
            }
        });

        return view;
    }
}

public class RemotesActivity extends MenuActivity {
    RemoteListAdapter rla;
    ArrayList<Remote> remotes;

    /*ArrayList<Remote> getRemotesFromPreferences() {
        try {
            SharedPreferences sharedPref = RemotesActivity.this.getPreferences(Context.MODE_PRIVATE);
            String resp = sharedPref.getString("remote_list", "[]");
            JSONArray j = new JSONArray(resp);
            ArrayList<Remote> r = new ArrayList<>();
            for (int i = 0; i < j.length(); i++) {
                JSONArray jj = (JSONArray) j.get(i);
                String name = jj.getString(0);
                int w = jj.getInt(1);
                int h = jj.getInt(2);
                r.add(new Remote(name, w, h));
            }
            Log.d("dbg",r.toString());
            return r;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    void setRemotesToPreferences(ArrayList<Remote> rems) {
        SharedPreferences sharedPref = RemotesActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        ArrayList<ArrayList<String>> a = new ArrayList<>();
        for (Remote i : rems) {
            a.add(i.toArrayList());
        }
        JSONArray j = new JSONArray(a);
        editor.putString("remote_list", String.valueOf(j)).apply();
    }*/

    ArrayList<Remote> getRemotesFromPreferences() {
        try {
            SharedPreferences sharedPref = RemotesActivity.this.getPreferences(Context.MODE_PRIVATE);
            String resp = sharedPref.getString("remote_list", "{}");

            JSONObject j = new JSONObject(resp);
            ArrayList<Remote> r = new ArrayList<>();
            for (Iterator<String> it = j.keys(); it.hasNext(); ) {
                String k = it.next();
                if (j.get(k) instanceof JSONObject) {
                    JSONObject a = (JSONObject) j.get(k);
                    Remote rem = new Remote(k, Integer.parseInt(a.getString("width")), Integer.parseInt(a.getString("height")));
                    JSONArray bJsonArray = a.getJSONArray("buttons");
                    ArrayList<RemoteButton> rb = new ArrayList<>();
                    for (int m = 0; m < bJsonArray.length(); m++) {
                        JSONObject but = bJsonArray.getJSONObject(m);
                        RemoteButton remBut = new RemoteButton(but.getString("text"));
                        rem.addButton(remBut);

                        JSONArray baJsonArray = but.getJSONArray("actions");
                        for (int n = 0; n < baJsonArray.length(); n++) {
                            JSONObject act = baJsonArray.getJSONObject(n);
                            RemoteButtonAction ac = new Wait();
                            String type = act.getString("type");
                            if (type.equals("wait")) {
                                ac = new Wait();
                            } else if (type.equals("keypress")) {
                                ac = new KeyPress();
                            } else if (type.equals("mouseclick")) {
                                ac = new MouseClick();
                            }
                            Map<String, String> params = new HashMap<>();
                            JSONObject parJson = act.getJSONObject("params");
                            for (Iterator<String> it2 = parJson.keys(); it2.hasNext(); ) {
                                String k2 = it2.next();
                                if (parJson.get(k2) instanceof String) {
                                    params.put(k2, (String) parJson.get(k2));
                                }
                            }
                            ac.setParams(params);
                        }
                        rb.add(remBut);
                    }
                    r.add(rem);
                }
            }
            return r;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    void setRemotesToPreferences(ArrayList<Remote> rems) {
        SharedPreferences sharedPref = RemotesActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        try {
            JSONObject j = new JSONObject();
            for (Remote r : rems) {
                JSONObject rJson = new JSONObject();
                rJson.put("width", r.getWidth());
                rJson.put("height", r.getHeight());
                JSONArray bJsonArray = new JSONArray();
                for (RemoteButton b : r.getButtons()) {
                    JSONObject bJson = new JSONObject();
                    bJson.put("text", b.getText());
                    bJsonArray.put(bJson);

                    JSONArray baJsonArray = new JSONArray();
                    for (RemoteButtonAction rba : b.getActions()) {
                        JSONObject rbaJson = new JSONObject();
                        rbaJson.put("type", rba.type);
                        JSONObject params_json = new JSONObject();
                        rbaJson.put("params", params_json);
                        for (Map.Entry<String, String> entry : rba.getParams().entrySet()) {
                            params_json.put(entry.getKey(), entry.getValue());
                        }
                        baJsonArray.put(rbaJson);
                    }
                    bJson.put("actions", baJsonArray);
                }
                rJson.put("buttons", bJsonArray);
                j.put(r.getName(), rJson);
            }
            editor.putString("remote_list", String.valueOf(j)).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        remotes = new ArrayList<>();
        setContentView(R.layout.activity_remotes);
        this.getSupportActionBar().setTitle("Remote List");
        remotes = getRemotesFromPreferences();
        rla = new RemoteListAdapter(remotes, this.getApplicationContext());
        ListView lv = findViewById(R.id.listviewRemotes);
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
                            rla.notifyDataSetChanged();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
}
