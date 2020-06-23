package com.example.customremote;

import java.util.ArrayList;

public class ServerListInfo {
    String ip;
    String name;
    ServerListInfo(String ip, String name){
        this.ip = ip;
        this.name = name;
    }

    public ArrayList<String> toArrayList(){
        ArrayList<String> r = new ArrayList<String>();
        r.add(ip);
        r.add(name);
        return r;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
