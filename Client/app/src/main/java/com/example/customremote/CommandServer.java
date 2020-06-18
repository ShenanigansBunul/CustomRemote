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
    private CommandServer(){

    }
    @Override
    public void run(){
        try{
            socket = new Socket("192.168.1.9", 5800);
            writer = new PrintWriter(socket.getOutputStream());
            messageQueue = new LinkedBlockingQueue<String>();
            isRunning = true;
            while(isRunning){
                while(!messageQueue.isEmpty()){
                    Log.d("dbg", "ruleaza 2");
                    writer.print(messageQueue.poll());
                    writer.flush();
                }
            }
            writer.close();
            socket.close();
        } catch (Exception e) {
            Log.d("dbg", "exceptie bai");
            e.printStackTrace();
        }
    }
    public boolean sendMessage(String message){
        if(!isRunning){
            Log.d("dbg", "false");
            return false;
        }
        Log.d("dbg", "true");
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
