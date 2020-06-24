package com.example.customremote.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.customremote.R;
import com.example.customremote.Remote;
import com.example.customremote.RemoteButton;
import com.example.customremote.RemoteButtonAction;
import com.example.customremote.RemoteListJsonConverter;
import com.example.customremote.actions.Nothing;

import java.util.ArrayList;

public class EditButtonActivity extends AppCompatActivity {

    ArrayList<Remote> remotes;
    RemoteButton currentButton;
    Remote currentRemote;
    String remoteName;
    ArrayList<RemoteButton> buttons;
    ArrayList<RemoteButtonAction> actions;
    int buttonIndex;

    Remote getByName(ArrayList<Remote> rms, String name){
        for(Remote r: rms){
            if (r.getName().equals(name))
                return r;
        }
        return null;
    }

    void updateActionLayout(){
        LinearLayout actionLayout = findViewById(R.id.actionsLayout);
        actionLayout.removeAllViews();
        for(RemoteButtonAction q: actions){
            LayoutInflater inflater;
            inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout action = (LinearLayout) inflater.inflate(R.layout.action_list_element , null);

            actionLayout.addView(action);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_button);
        final Context c = this.getApplicationContext();

        Button cancelBtn = findViewById(R.id.actionCancel);
        Button saveBtn = findViewById(R.id.actionSave);
        Button deleteBtn = findViewById(R.id.actionDelete);

        Button addAction = findViewById(R.id.actionAdd);

        final EditText textInput = findViewById(R.id.buttonTextEdit);

        Intent i = getIntent();
        remoteName = i.getStringExtra("remote_name");
        String remoteDataJson = i.getStringExtra("all_data");
        buttonIndex = i.getIntExtra("button_i", -1);
        remotes = RemoteListJsonConverter.jsonToRemoteList(remoteDataJson);
        currentRemote = getByName(remotes, remoteName);
        buttons = currentRemote.getButtons();
        currentButton = buttons.get(buttonIndex);
        actions = currentButton.getActions();

        int hFromIndex = (buttonIndex)%currentRemote.getHeight() + 1;
        int wFromIndex = (buttonIndex)/currentRemote.getHeight() + 1;
        this.getSupportActionBar().setTitle("Edit Button X="+String.valueOf(wFromIndex)+", Y="+String.valueOf(hFromIndex));
        textInput.setText(currentButton.getText());

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, EditRemoteActivity.class);
                currentButton.setText("");
                currentButton.clearActions();
                String newRemotes = RemoteListJsonConverter.remoteListToJson(remotes);
                intent.putExtra("updated_buttons", newRemotes);
                intent.putExtra("remote_name", currentRemote.getName());
                startActivity(intent);
                finish();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, EditRemoteActivity.class);
                String newRemotes = RemoteListJsonConverter.remoteListToJson(remotes);
                intent.putExtra("updated_buttons", newRemotes);
                intent.putExtra("remote_name", currentRemote.getName());
                startActivity(intent);
                finish();
            }
        });

        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = textInput.getText().toString();
                currentButton.setText(newText);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        addAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nothing act = new Nothing();
                actions.add(act);
                updateActionLayout();
            }
        });
    }
}
