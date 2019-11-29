package com.example.lenovo.fragment_test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Link_Fragment extends Fragment implements View.OnClickListener {

    private TextView Ip;
    private TextView SSID;
    private TextView PassWord;
    private Handler handler;
    private String ssid = "SSID";
    private String password = "PassWord";
    private String ip = "IP地址";
    private ListView listView;

    public Link_Fragment() {

    }

    public void onAttach(Context context) {
        super.onAttach(context);
        AdminActivity adminActivity = (AdminActivity) context;
        handler = adminActivity.getHandler();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_link, container, false);
        v.findViewById(R.id.bt_Open).setOnClickListener(this);
        v.findViewById(R.id.bt_Close).setOnClickListener(this);
        Ip = v.findViewById(R.id.Ip);
        SSID = v.findViewById(R.id.SSID);
        PassWord = v.findViewById(R.id.Password);
        listView = v.findViewById(R.id.lt_Wifi);

        /**
         * listView监听
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        SSID.setText(ssid);
        PassWord.setText(password);
        Ip.setText(ip);
    }

    /**
     * 按钮监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Open:
                /**
                 * 8.0打开WiFi热点
                 */
                if (!getApEnable(getContext())) {
                    final WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
                    wifiManager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {
                        @Override
                        public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                            super.onStarted(reservation);
                            ssid = reservation.getWifiConfiguration().SSID;
                            password = reservation.getWifiConfiguration().preSharedKey;
                            ip = getDeviceIpAddress();
                            SSID.setText(ssid);
                            PassWord.setText(password);
                            Ip.setText(ip);

                            ListenerThread listenerThread = new ListenerThread(handler);
                            Log.e("ApStetting","创建线程");
                            listenerThread.start();
                        }
                    }, null);


                } else {
                    Toast.makeText(getContext(), "已经开启热点^_^", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_Close:
                if (getApEnable(getContext())) {
                    closeHotSpotForAndroid_O();
                    ssid = "SSID";
                    password = "PassWord";
                    ip = "IP地址";
                    SSID.setText(ssid);
                    PassWord.setText(password);
                    Ip.setText(ip);
                } else {
                    Toast.makeText(getContext(), "已经关闭热点^_^", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 获取热点状态
     *
     * @param context
     * @return
     */
    public static boolean getApEnable(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            return false;
        }
        try {
            Method method = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取IP地址
     */
    public String getDeviceIpAddress() {
        try {

            for (Enumeration<NetworkInterface> enumeration = NetworkInterface
                    .getNetworkInterfaces(); enumeration.hasMoreElements(); ) {
                NetworkInterface networkInterface = enumeration.nextElement();
                for (Enumeration<InetAddress> enumerationIpAddr = networkInterface
                        .getInetAddresses(); enumerationIpAddr
                             .hasMoreElements(); ) {
                    InetAddress inetAddress = enumerationIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress.getAddress().length == 4) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("ERROR:", e.toString());
        }
        return null;
    }


    /**
     * 关闭热点
     */

    public void closeHotSpotForAndroid_O() {
        ConnectivityManager connManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Field iConnMgrField;
        try {
            iConnMgrField = connManager.getClass().getDeclaredField("mService");
            iConnMgrField.setAccessible(true);
            Object iConnMgr = iConnMgrField.get(connManager);
            Class<?> iConnMgrClass = Class.forName(iConnMgr.getClass().getName());
            Method stopTethering = iConnMgrClass.getMethod("stopTethering", int.class);
            stopTethering.invoke(iConnMgr, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
