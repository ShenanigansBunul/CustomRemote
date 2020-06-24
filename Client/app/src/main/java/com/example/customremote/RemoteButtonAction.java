package com.example.customremote;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public interface RemoteButtonAction {
    public String type = null;
    public Map<String,String> getParams();
    public void setParams(Map<String,String> m);
    public void performAction();
}
