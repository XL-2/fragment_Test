package com.example.lenovo.fragment_test;


import android.os.Handler;
import android.os.Message;
import org.json.JSONArray;
import java.io.InputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class ComThread extends Thread {

    private Socket socket;
    private InputStream inputStream;
    private Handler handler;
    private DataAdapter dataAdapter;

    public ComThread(Socket s, Handler h) {
        this.socket = s;
        this.handler = h;
    }

    public void out(String string) {
        try {
            socket.getOutputStream().write(string.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void outSTM(int i) {
        try {
            socket.getOutputStream().write(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendJSON(List<Map<String, Object>> list) {
        JSONArray jsonArray = new JSONArray(list);
        System.out.println(jsonArray);
        try {
            socket.getOutputStream().write(jsonArray.toString().getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            inputStream = socket.getInputStream();
            byte[] buffer = new byte[128];

            while (true) {
                int count = inputStream.read(buffer);
                String s = new String(buffer, 0, count);
                Message message = new Message();
                message.what = 1;
                message.obj = s;
                handler.sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static byte[] hexStr2Bytes(String paramString) {
        int i = paramString.length() / 2;

        byte[] arrayOfByte = new byte[i];
        int j = 0;
        while (true) {
            if (j >= i)
                return arrayOfByte;
            int k = 1 + j * 2;
            int l = k + 1;
            arrayOfByte[j] = (byte) (0xFF & Integer.decode(
                    "0x" + paramString.substring(j * 2, k)
                            + paramString.substring(k, l)).intValue());
            ++j;
        }
    }

}
