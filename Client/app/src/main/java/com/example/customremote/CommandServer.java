package com.example.customremote;

import android.os.SystemClock;
import android.util.Log;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

class CommandServer implements Runnable{
    private Socket socket;
    private PrintWriter writer;
    private boolean isRunning;
    private Queue<String> messageQueue;
    private static CommandServer instance;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    private String ip;
    private CommandServer(){

    }
    @Override
    public void run(){
        try{
            socket = new Socket(ip, 5800);
            writer = new PrintWriter(socket.getOutputStream());
            messageQueue = new LinkedBlockingQueue<String>();
            isRunning = true;
            while(isRunning){
                while(!messageQueue.isEmpty()){
                    writer.print(messageQueue.poll());
                    writer.flush();
                }
            }
            writer.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean sendMessage(String message){
        if(!isRunning){
            return false;
        }
        messageQueue.add(message);
        return true;
    }
    public void stopServer(){
        isRunning = false;
    }
    public static CommandServer getInstance(){
        if(instance == null){
            instance = new CommandServer();
        }
        return instance;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
