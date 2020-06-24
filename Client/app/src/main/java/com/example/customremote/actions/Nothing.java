package com.example.customremote.actions;

import com.example.customremote.RemoteButtonAction;

import java.util.HashMap;
import java.util.Map;

public class Nothing implements RemoteButtonAction {
    public String type = "none";
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
    }
}