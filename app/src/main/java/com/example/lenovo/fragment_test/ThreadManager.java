package com.example.lenovo.fragment_test;

import android.util.Log;

import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ThreadManager {

    private static final ThreadManager threadManeger = new ThreadManager();

    private ThreadManager(){

    }

    public static ThreadManager getThreadManeger(){
        return threadManeger;
    }

    Vector<ComThread> vector = new Vector<ComThread>(2);

    public void add(ComThread comThread){
        vector.add(comThread);
    }

//    public void send(String string){
//        for(int i=0;i<vector.size();i++){
//            ComThread ct = vector.get(i);
//            ct.out(string);
//        }
//    }

    public void sendSTM(int i){
        ComThread ct = vector.get(0);
        ct.outSTM(i);
    }

    public void sendMobile(String string){
        for(int i=1;i<vector.size();i++){
            ComThread ct = vector.get(i);
            ct.out(string);
        }
    }

    public void sendJson(List<Map<String,Object>> list,long id){
        Log.i("sendJson一",id+"");
        for(int i=0;i<vector.size();i++) {
            ComThread ct = vector.get(i);
            Log.i("seendJson三",ct.currentThread().getId()+"");
            if(ct.currentThread().getId() == id){
                ct.sendJSON(list);
            }
        }
    }
}
