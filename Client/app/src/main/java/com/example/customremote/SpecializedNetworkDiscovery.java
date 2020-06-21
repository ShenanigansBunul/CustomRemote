package com.example.customremote;

import java.util.ArrayList;

public class SpecializedNetworkDiscovery extends NetworkDiscovery {
    public ArrayList<ServerListInfo> getServerList() {
        return serverList;
    }

    private ArrayList<ServerListInfo> serverList = new ArrayList<ServerListInfo>();
    @Override
    protected void notifyComputerDiscovered(String computerLocalAddress, String computerName) {
        ServerListInfo q = new ServerListInfo(computerLocalAddress, computerName);
        boolean add = true;
        for(ServerListInfo x: serverList){
            if (q.ip.equals(x.ip)) {
                add = false;
                break;
            }
        }
        if(add) {
            serverList.add(q);
        }
    }
}
