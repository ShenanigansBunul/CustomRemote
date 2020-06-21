package com.example.customremote;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class PingClient implements Runnable {
    private final int PING_SERVER_PORT = 5600;

    public Boolean getRemoved() {
        return removed;
    }

    private Boolean removed = false;
    public void setRemoved(Boolean v){ this.removed = v;}

    private String ipAddress;
    public String getIPAddress() {
        return ipAddress;
    }

    private Boolean serverFound = null;
    public Boolean getServerFound() {
        return serverFound;
    }

    private String serverName = "";
    public String getServerName() {return serverName;}

    public PingClient(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public void run() {
        Socket serverSocket;
        boolean yee = false;
        try {
            serverSocket = new Socket();
            serverSocket.connect(new InetSocketAddress(ipAddress, PING_SERVER_PORT), 10000);
        } catch (IOException e) {
            serverFound = false;
            return;
        }

        try {
            BufferedReader bis = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            String inputLine;
            while((inputLine = bis.readLine()) != null){
                serverName = inputLine;
            }
            bis.close();
        } catch (IOException e) {
            try {
                serverSocket.close();
                serverFound = false;
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!serverName.equals("")) {
            serverFound = true;
        }
    }
}
