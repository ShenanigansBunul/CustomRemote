package com.example.customremote;

public interface RemoteButtonAction {
    String key = null;
    int duration_milis = 0;
    public void performAction();
}
