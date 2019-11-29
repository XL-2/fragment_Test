package com.example.lenovo.fragment_test;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ListenerThread extends Thread {

    private ServerSocket serverSocket;
    private Handler handler;
    private Socket socket;

    public ListenerThread (Handler h){
        this.handler = h;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(5000);

            while (true){
                socket = serverSocket.accept();
                Message message = new Message();
                message.obj = "有设备接入";
                handler.sendMessage(message);
                ComThread comThread = new ComThread(socket,handler);
                ThreadManager.getThreadManeger().add(comThread);
                comThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
