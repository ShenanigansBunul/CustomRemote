package com.example.customremote;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public abstract class NetworkDiscovery implements Runnable {
    @Override
    public void run() {
        String localNetworkAddress = null;
        try {
            localNetworkAddress = getLocalNetworkAddress();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        List<PingClient> pingClientList = new ArrayList<>();
        PingClient pingClient;
        String deviceLocalAddress;
        for (int deviceNumber = 1; deviceNumber < 256; ++deviceNumber) {
            deviceLocalAddress = localNetworkAddress + deviceNumber;

            pingClient = new PingClient(deviceLocalAddress);
            (new Thread(pingClient)).start();

            pingClientList.add(pingClient);
        }

        boolean allDevicesQueried = false;
        Boolean serverFound;
        while (!allDevicesQueried) {
            allDevicesQueried = true;

            for (PingClient currentPingClient: pingClientList) {
                serverFound = currentPingClient.getServerFound();
                if (serverFound == null){
                    if(!currentPingClient.getRemoved()){
                        allDevicesQueried = false;
                    }
                }
                else if (serverFound){
                    notifyComputerDiscovered(currentPingClient.getIPAddress(), currentPingClient.getServerName());
                }
            }

            //pingClientList.removeIf((PingClient currentPingClient) -> currentPingClient.getServerFound() != null);
            for(PingClient currentPingClient: pingClientList){
                if(currentPingClient.getServerFound() != null){
                    currentPingClient.setRemoved(true);
                }
            }
        }
    }

    private static String getLocalNetworkAddress() throws SocketException, UnknownHostException {
        final DatagramSocket socket = new DatagramSocket();

        socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
        String localMachineIP = socket.getLocalAddress().getHostAddress();

        return localMachineIP.substring(0, localMachineIP.lastIndexOf(".") + 1);
    }

    protected abstract void notifyComputerDiscovered(String computerLocalAddress, String computerName);
}
