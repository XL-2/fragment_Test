package com.example.lenovo.fragment_test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Info_Fragment extends Fragment {
    private ListView listView;

    private DAO dao;

    public Info_Fragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_info, container, false);

        /**
         * 从资源文件获取功能，后期方便修改
         */
        listView=v.findViewById(R.id.lv_menu);
        final FunctionAdpter functionAdpter = new FunctionAdpter(getContext());
        String s[]=getResources().getStringArray(R.array.function);
        functionAdpter.setList(Arrays.asList(s));
        listView.setAdapter(functionAdpter);

        dao = new DAO(getContext());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 2:
                        new AlertDialog.Builder(getContext())
                                .setTitle(functionAdpter.getList().get(position))
                                .setIcon(R.drawable.pic)
                                .setMessage("是否清空数据库！！！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dao.deleteAll();
                                    }
                                })
                                .setNegativeButton("取消",null)
                                .create().show();
                        break;
                        default:
                            Toast.makeText(getContext(),functionAdpter.getList().get(position)+"：待开发",Toast.LENGTH_SHORT).show();

                }

            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
