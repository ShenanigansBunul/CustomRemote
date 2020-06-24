package com.example.customremote;

import java.util.ArrayList;

public class RemoteButton implements Runnable{
    public RemoteButton(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    boolean running = false;
    private String text;

    public ArrayList<RemoteButtonAction> getActions() {
        return actions;
    }

    public void setActions(ArrayList<RemoteButtonAction> actions) {
        this.actions = actions;
    }

    public void clearActions(){
        this.actions.clear();
    }

    public void addAction(RemoteButtonAction act){
        this.actions.add(act);
    }

    ArrayList<RemoteButtonAction> actions = new ArrayList<>();

    @Override
    public void run() {
        running = true;
        for(RemoteButtonAction q: actions){
            q.run();
        }
    }
}
