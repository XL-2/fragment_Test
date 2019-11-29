package com.example.lenovo.fragment_test;

import android.os.Handler;
import android.os.Message;

import java.util.Calendar;


public class ActionThread extends Thread {

    private int startHour;
    private int startMin;
    private int endHour;
    private int endMin;
    private Handler handler;
    private Calendar calendar;

    private boolean action = true;

    private int flag = 0;

    public ActionThread(int startHour, int startMin, int endHour, int endMin, Handler handler) {
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
        this.handler = handler;
    }

    @Override
    public void run() {
        while (action) {
            calendar = Calendar.getInstance();
            if (calendar.get(Calendar.HOUR_OF_DAY) == startHour && calendar.get(Calendar.MINUTE) == startMin && flag == 0) {
                Message message = new Message();
                message.obj = "Open";
                message.what = 1;
                handler.sendMessage(message);
                flag = 1;
            }
            if (calendar.get(Calendar.HOUR_OF_DAY) == endHour && calendar.get(Calendar.MINUTE) == endMin) {
                Message message = new Message();
                message.obj = "Close";
                message.what = 2;
                handler.sendMessage(message);
                flag = 0;
                break;
            }
        }
        try {
            Thread.sleep(500);
            Thread.interrupted();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onStop(){
        if(action){
            action = false;
        }
    }

}
