package com.example.customremote.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.customremote.R;
import com.example.customremote.Remote;
import com.example.customremote.RemoteButton;
import com.example.customremote.RemoteButtonAction;
import com.example.customremote.RemoteListJsonConverter;

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

    int posFromString(String type){
        final String[] actionArray = EditButtonActivity.this.getResources().getStringArray(R.array.Actions);
        for(int i = 0; i < actionArray.length; i++)
        {
            if (actionArray[i].equals(type)){
                return i;
            }
        }
        return 0;
    }

    void updateActionLayout(){
        LinearLayout actionLayout = findViewById(R.id.actionsLayout);
        actionLayout.removeAllViews();
        for(int i = 0; i < actions.size(); i++){
            final RemoteButtonAction q = actions.get(i);
            final LayoutInflater inflater;
            inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final LinearLayout action = (LinearLayout) inflater.inflate(R.layout.action_list_element , null);
            actionLayout.addView(action);

            final Spinner typeSpinner = action.findViewById(R.id.typeSpinner);
            final String[] actionArray = EditButtonActivity.this.getResources().getStringArray(R.array.Actions);
            typeSpinner.setSelection(posFromString(q.getType()));
            final int oldPosition = typeSpinner.getSelectedItemPosition();

            final LinearLayout paramLayout = action.findViewById(R.id.paramLayout);

            typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selected = actionArray[position];
                    q.setType(selected);
                    if (selected.equals("Nothing")) {

                    } else if (selected.equals("Wait")) {
                        LayoutInflater timeParamInflater = (LayoutInflater) EditButtonActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final LinearLayout timeParamLayout = (LinearLayout) timeParamInflater.inflate(R.layout.param_edit_number, null);
                        final TextView paramName = timeParamLayout.findViewById(R.id.paramName);
                        final EditText paramInput = timeParamLayout.findViewById(R.id.paramInput);
                        paramName.setText("Duration (ms):");
                        String tString = q.getParams().get("wait");
                        if (tString != null)
                            paramInput.setText(tString);
                        else
                            paramInput.setText("0");

                        paramInput.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String p = paramInput.getText().toString();
                                if (p.equals(""))
                                    p = "0";
                                q.setParam("wait", p);
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });

                        paramLayout.addView(timeParamLayout);

                    } else if (selected.equals("Press Key")) {

                    } else if (selected.equals("Click")) {

                    } else if (selected.equals("Press Key (Special)")) {

                    }

                    if(position != oldPosition)
                        updateActionLayout();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            Button delButton = action.findViewById(R.id.removeActionButton);
            final int delIndex = i;
            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actions.remove(delIndex);
                    updateActionLayout();
                }
            });
        }
        Log.d("d","upd8");
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
                RemoteButtonAction rba = new RemoteButtonAction("");
                actions.add(rba);
                updateActionLayout();
            }
        });
        updateActionLayout();
    }
}
