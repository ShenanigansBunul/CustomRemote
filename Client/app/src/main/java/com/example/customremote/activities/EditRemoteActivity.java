package com.example.customremote.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.customremote.R;
import com.example.customremote.Remote;
import com.example.customremote.RemoteButton;
import com.example.customremote.RemoteListJsonConverter;

import java.util.ArrayList;

public class EditRemoteActivity extends AppCompatActivity {

    Remote currentRemote;
    ArrayList<Remote> remotes;
    ArrayList<RemoteButton> buttons;

    Remote getByName(ArrayList<Remote> rms, String name){
        for(Remote r: rms){
            if (r.getName().equals(name))
                return r;
        }
        return null;
    }

    void updateButtonButtonList(){
        final LinearLayout buttonButtonList = findViewById(R.id.buttonSettingsLayout);
        buttonButtonList.removeAllViews();
        for(int wi = 0; wi < currentRemote.getWidth(); wi++)
            for(int hi = 0; hi < currentRemote.getHeight(); hi++)
            {
                Button buttonButton = new Button(this);
                String butText = "Edit Button X=" + String.valueOf(wi+1) +", Y=" + String.valueOf(hi+1);
                buttonButton.setText(butText);
                buttonButtonList.addView(buttonButton);
                final int buttonIndex = wi*currentRemote.getHeight()+hi;
                final Context c = this.getApplicationContext();
                buttonButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(c, EditButtonActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("button_i", buttonIndex);
                        intent.putExtra("all_data", RemoteListJsonConverter.remoteListToJson(remotes));
                        intent.putExtra("remote_name",currentRemote.getName());
                        c.startActivity(intent);
                    }
                });
            }

        while (currentRemote.getHeight() * currentRemote.getWidth() > buttons.size()){
            buttons.add(new RemoteButton(""));
        }
    }

    void loadFromEditButton(){
        Intent i = getIntent();
        String remoteName = i.getStringExtra("remote_name");
        String upb = i.getStringExtra("updated_buttons");
        if(upb != null){
            remotes = RemoteListJsonConverter.jsonToRemoteList(upb);
            currentRemote = getByName(remotes,remoteName);
            buttons = currentRemote.getButtons();
            updateButtonButtonList();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadFromEditButton();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_remote);
        this.getSupportActionBar().setTitle("Edit Remote");

        final Context c = this.getApplicationContext();

        Button cancelBtn = findViewById(R.id.cancelRemote);
        Button saveBtn = findViewById(R.id.saveRemote);
        Button deleteBtn = findViewById(R.id.deleteRemote);

        final EditText nameInput = findViewById(R.id.name_input);
        final EditText widthInput = findViewById(R.id.width_input);
        final EditText heightInput = findViewById(R.id.height_input);

        Intent i = getIntent();
        String remoteDataJson = i.getStringExtra("all_data");
        if(remoteDataJson != null) {
            String remoteName = i.getStringExtra("remote_name");
            remotes = RemoteListJsonConverter.jsonToRemoteList(remoteDataJson); //else returning from editbuttonactivity
            currentRemote = getByName(remotes, remoteName);
            buttons = currentRemote.getButtons();
        }
        else
            loadFromEditButton();

        nameInput.setText(currentRemote.getName());
        widthInput.setText(String.valueOf(currentRemote.getWidth()));
        heightInput.setText(String.valueOf(currentRemote.getHeight()));

        updateButtonButtonList();

        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newName = nameInput.getText().toString();
                if(newName.equals(""))
                    newName = "NO_NAME";
                currentRemote.setName(newName);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        widthInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String wString = widthInput.getText().toString();
                if(!wString.equals("")) {
                    int newWidth = Integer.parseInt(wString);
                    if (newWidth > 20)
                        newWidth = 20;
                    if (newWidth < 1)
                        newWidth = 1;
                    currentRemote.setWidth(newWidth);
                }
                else
                    currentRemote.setWidth(1);
            }
            @Override
            public void afterTextChanged(Editable s) {
                String wString = widthInput.getText().toString();
                if(!wString.equals("")) {
                    int newWidth = Integer.parseInt(wString);
                    if(newWidth > 20)
                        widthInput.setText(String.valueOf(20));
                }
                updateButtonButtonList();
            }
        });
        heightInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String hString = heightInput.getText().toString();
                if(!hString.equals("")){
                    int maxHeight = Integer.parseInt(hString);
                    if(maxHeight > 20)
                        maxHeight = 20;
                    if(maxHeight < 1)
                        maxHeight = 1;
                    currentRemote.setHeight(maxHeight);
                }
                else
                    currentRemote.setHeight(1);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String hString = heightInput.getText().toString();
                if(!hString.equals("")) {
                    int newHeight = Integer.parseInt(hString);
                    if(newHeight > 20)
                        heightInput.setText(String.valueOf(20));
                }
                updateButtonButtonList();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, RemotesActivity.class);
                finish();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, RemotesActivity.class);
                remotes.remove(currentRemote);
                String newRemotes = RemoteListJsonConverter.remoteListToJson(remotes);
                intent.putExtra("updated_list", newRemotes);
                startActivity(intent);
                finish();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, RemotesActivity.class);
                String newRemotes = RemoteListJsonConverter.remoteListToJson(remotes);
                intent.putExtra("updated_list", newRemotes);
                startActivity(intent);
                finish();
            }
        });
    }
}
