package com.example.lenovo.fragment_test;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;


public class Console_Fragment extends Fragment {

    private Switch Infrared;
    private Switch Light;
    private Button Thread;
    private Button Add;
    private Button Delet;
    private Handler handler;
    private AdminActivity adminActivity;
    private TimePicker startTime;
    private TimePicker endTime;
    private TextView message;
    private Button sync;
    private Button cancel;
    private CardView cardView;
    private RadioGroup model;
    private RadioButton rb_default;
    private RadioButton rb_beep;
    private RadioButton rb_led;
    private TextView choose;
    private Spinner spinner;


    private int startHour = 0;
    private int startMin = 0;
    private int endHour = 0;
    private int endMin = 0;

    private DAO dao;

    private Date curDate;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");

    private ActionThread actionThread = null;
    private long actionThread_ID = -1;

    private final android.os.Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                //ThreadManager.getThreadManeger().sendSTM(0x02);
                Infrared.setChecked(true);
            }
            if (msg.what == 2) {
                //ThreadManager.getThreadManeger().sendSTM(0x04);
                Infrared.setChecked(false);
                actionThread_ID = -1;
                sync.setEnabled(true);
                cancel.setEnabled(false);
            }
        }
    };

    public Console_Fragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adminActivity = (AdminActivity) context;
        handler = adminActivity.getHandler();
        setHasOptionsMenu(false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_console, container, false);

        Infrared = v.findViewById(R.id.st_Infrared);
        Light = v.findViewById(R.id.st_Light);
        Thread = v.findViewById(R.id.bt_Thread);
        Add = v.findViewById(R.id.bt_Add);
        Delet = v.findViewById(R.id.bt_Delete);
        message = v.findViewById(R.id.message);
        sync = v.findViewById(R.id.bt_time_setting);
        cancel = v.findViewById(R.id.bt_time_cancel);
        cardView = v.findViewById(R.id.carView);
        model = v.findViewById(R.id.rg_model);
        rb_default = v.findViewById(R.id.rb_default);
        rb_beep = v.findViewById(R.id.rb_beep);
        rb_led = v.findViewById(R.id.rb_led);
        choose = v.findViewById(R.id.tv_choose);
        startTime = v.findViewById(R.id.tp_start);
        endTime = v.findViewById(R.id.tp_end);
        spinner = v.findViewById(R.id.spinner);


        startTime.setIs24HourView(true);//设置为24小时显示格式
        startTime.setCurrentHour(0); //当前小时
        startTime.setCurrentMinute(0); //当前分钟
        //设置父布局focus，子控件不会focus，以此禁止调起键盘
        endTime.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        endTime.setIs24HourView(true);
        endTime.setCurrentHour(0);
        endTime.setCurrentMinute(0);
        endTime.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);


        dao = new DAO(getContext());

        //message.append(getDeviceIpAddress()+"\n");
        message.setFocusable(true);
        message.setFocusableInTouchMode(true);


        Thread.setVisibility(View.GONE);
        Add.setVisibility(View.GONE);
        Delet.setVisibility(View.GONE);
        cardView.setVisibility(View.GONE);
        choose.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        cancel.setEnabled(false);

        Infrared.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (ThreadManager.getThreadManeger().vector.size() == 0) {
                        Toast.makeText(getContext(), "尚未连接设备", Toast.LENGTH_SHORT).show();
                        Infrared.setChecked(false);
                    } else {
                        ThreadManager.getThreadManeger().sendSTM(0x02);
                        spinner.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (ThreadManager.getThreadManeger().vector.size() == 0) {
                        Toast.makeText(getContext(), "尚未连接设备", Toast.LENGTH_SHORT).show();
                        Infrared.setChecked(true);
                    } else {
                        ThreadManager.getThreadManeger().sendSTM(0x04);
                        spinner.setVisibility(View.GONE);
                    }
                }
            }
        });

        Light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (ThreadManager.getThreadManeger().vector.size() == 0) {
                        Toast.makeText(getContext(), "尚未连接设备", Toast.LENGTH_SHORT).show();
                        Light.setChecked(false);
                    } else {
                        ThreadManager.getThreadManeger().sendSTM(0x01);
                    }
                } else {
                    if (ThreadManager.getThreadManeger().vector.size() == 0) {
                        Toast.makeText(getContext(), "尚未连接设备", Toast.LENGTH_SHORT).show();
                        Light.setChecked(true);
                    } else {
                        ThreadManager.getThreadManeger().sendSTM(0x03);
                    }
                }
            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardView.getVisibility() == View.GONE) {
                    cardView.setVisibility(View.VISIBLE);
                } else {
                    cardView.setVisibility(View.GONE);
                }
            }
        });

        model.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_default:
                        ThreadManager.getThreadManeger().sendSTM(0x02);
                        cardView.setVisibility(View.GONE);
                    case R.id.rb_beep:
                        ThreadManager.getThreadManeger().sendSTM(0x05);
                        cardView.setVisibility(View.GONE);
                        break;
                    case R.id.rb_led:
                        ThreadManager.getThreadManeger().sendSTM(0x06);
                        cardView.setVisibility(View.GONE);
                        break;
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Log.e("spinner", String.valueOf(position));
                        ThreadManager.getThreadManeger().sendSTM(0x02);
                        break;
                    case 1:
                        Log.e("spinner", String.valueOf(position));
                        ThreadManager.getThreadManeger().sendSTM(0x05);
                        break;
                    case 2:
                        Log.e("spinner", String.valueOf(position));
                        ThreadManager.getThreadManeger().sendSTM(0x06);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ThreadManager.getThreadManeger().vector.size() == 0) {
                    Toast.makeText(getContext(), "尚未连接设备", Toast.LENGTH_SHORT).show();
                } else {
                    curDate = new Date(System.currentTimeMillis());
                    message.append(formatter.format(curDate));
                    message.append("已经设置：\n");
                    message.append(startHour + "时" + startMin + "分 打开红外\n");
                    message.append(endHour + "时" + endMin + "分 关闭红外\n");
                    message.append("\n");
                    if (actionThread_ID == -1) {
                        actionThread = new ActionThread(startHour, startMin, endHour, endMin, hd);
                        actionThread_ID = actionThread.getId();
                        actionThread.start();
                        sync.setEnabled(false);
                        cancel.setEnabled(true);
                        startTime.setEnabled(false);
                        endTime.setEnabled(false);
                    } else {
                        actionThread.onStop();
                        Log.e("actionThread", String.valueOf(actionThread.getId()));
                        actionThread = new ActionThread(startHour, startMin, endHour, endMin, hd);
                        Log.e("actionThread", String.valueOf(actionThread.getId()));
                        actionThread.start();
                    }
//                    if (actionThread == null) {
//                        actionThread = new ActionThread(startHour, startMin, endHour, endMin, hd);
//                        Log.e("actionThread", String.valueOf(actionThread.getId()));
//                        actionThread.start();
//                        sync.setEnabled(false);
//                        cancel.setEnabled(true);
//                        startTime.setEnabled(false);
//                        endTime.setEnabled(false);
//                    } else {
//                        actionThread.onStop();
//                        Log.e("actionThread", String.valueOf(actionThread.getId()));
//                        actionThread = new ActionThread(startHour, startMin, endHour, endMin, hd);
//                        Log.e("actionThread", String.valueOf(actionThread.getId()));
//                        actionThread.start();
//                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionThread_ID != -1) {
                    actionThread_ID = -1;
                    actionThread.onStop();
                    sync.setEnabled(true);
                    cancel.setEnabled(false);
                    startTime.setEnabled(true);
                    endTime.setEnabled(true);
                }
            }
        });

        Thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListenerThread listenerThread = new ListenerThread(handler);
                Log.e("ApStetting", "创建线程");
                listenerThread.start();
                v.findViewById(R.id.bt_Thread).setEnabled(false);
                message.append(getDeviceIpAddress());
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao.insert();
            }
        });


        startTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                startHour = hourOfDay;
                startMin = minute;
            }
        });
        endTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                endHour = hourOfDay;
                endMin = minute;
            }
        });

        return v;
    }

    public Switch getInfrared() {
        return Infrared;
    }

    public Switch getLight() {
        return Light;
    }

    public TextView getMessage() {
        return message;
    }

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

}
