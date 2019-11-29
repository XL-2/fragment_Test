package com.example.lenovo.fragment_test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AdminActivity extends AppCompatActivity {

    private final android.os.Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                //Toast.makeText(AdminActivity.this,"收到消息啦！"+msg.obj,Toast.LENGTH_LONG).show();
                if(msg.obj.toString().equals("WARNING")){
                    dao.insert();
                    if(ThreadManager.getThreadManeger().vector.size()==2){
                        ThreadManager.getThreadManeger().sendMobile((String) msg.obj);
                    }
                    curDate = new Date(System.currentTimeMillis());
                    console_fragment.getMessage().append(formatter.format(curDate)+":\n");

                    console_fragment.getMessage().append("***检测到有人入侵！***\n\n");
                }
                if(msg.obj.toString().equals("SEARCH")){
                    ThreadManager.getThreadManeger().sendJson(dao.query(), Thread.currentThread().getId());
                }
                if(msg.obj.toString().equals("OPENINFRARED")){
                    console_fragment.getInfrared().setChecked(true);
                }
                if(msg.obj.toString().equals("CLOSEINFRARED")){
                    console_fragment.getInfrared().setChecked(false);
                }
                if(msg.obj.toString().equals("OPENLIGHT")){
                    console_fragment.getLight().setChecked(true);
                }
                if(msg.obj.toString().equals("CLOSELIGHT")){
                    console_fragment.getLight().setChecked(false);
                }
            }

            if (msg.obj=="有设备接入"){
                Toast.makeText(AdminActivity.this,""+msg.obj,Toast.LENGTH_SHORT).show();
            }
        }
    };

    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private static final Console_Fragment console_fragment = new Console_Fragment();
    private static final History_Fragment history_fragment = new History_Fragment();
    private static final Info_Fragment info_fragment = new Info_Fragment();

    private Date curDate;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");


    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        ListenerThread listenerThread = new ListenerThread(handler);
        Log.e("ApStetting", "创建线程");
        listenerThread.start();

        /**
         * 数据库DAO
         */
        dao = new DAO(this);
        /**
         * 8.0定位权限
         */
        if (ContextCompat.checkSelfPermission(AdminActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(AdminActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
        /**
         * 8.0线程权限
         */
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        viewPager = findViewById(R.id.fragment_view);
        fragmentList = new ArrayList<>();
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        tabLayout = findViewById(R.id.tablayout);


        //fragmentList.add(new Link_Fragment());
        fragmentList.add(console_fragment);
        fragmentList.add(history_fragment);
        fragmentList.add(info_fragment);

        viewPager.setAdapter(viewPagerAdapter);
        //关联viewpager
        tabLayout.setupWithViewPager(viewPager);

        //Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_sysbar_quicksettings);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_sysbar_quicksettings);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_menu_recent_history);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_menu_info_details);

        tabLayout.setBackgroundColor(getColorPrimary());

        tabLayout.setTabMode(TabLayout.MODE_FIXED);

    }

    /**
     * 获取主题的主要颜色
     * @return
     */
    public int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }


    public int getColorAccent(){
        TypedValue typedValue = new  TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        return typedValue.data;
    }

    public Handler getHandler() {
        return handler;
    }

}
