package com.example.customremote;

import android.text.Editable;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class RemoteButtonAction implements Runnable{
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
    private Map<String, String> params = new HashMap<String, String>();

    public Map<String, String> getParams() {
        return params;
    }
    public RemoteButtonAction(String type){
        this.type=type;
    }

    public void setParams(Map<String, String> m) {
        params = m;
    }

    @Override
    public void run() {
        try {
            if (type.equals("Wait")) {
                Thread.sleep(Integer.parseInt(params.get("wait")));
            } else if (type.equals("mouseclick")) {

            }
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void setParam(String k, String v) {
        params.put(k, v);
    }
}
