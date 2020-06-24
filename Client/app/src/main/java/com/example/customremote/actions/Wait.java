package com.example.customremote.actions;

import com.example.customremote.RemoteButtonAction;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class Wait implements RemoteButtonAction {
    public String type = "wait";
    private Map<String, String> params = new HashMap<String, String>();

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public void setParams(Map<String, String> m) {
        this.params = m;
    }

    @Override
    public void performAction() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
