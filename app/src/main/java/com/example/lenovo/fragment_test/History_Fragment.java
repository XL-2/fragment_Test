package com.example.lenovo.fragment_test;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class History_Fragment extends Fragment {

    private DAO dao;
    private DataAdapter dataAdapter;
    private ListView listView;
    private SwipeRefreshLayout refreshLayout;
    private Toolbar toolbar;

    public History_Fragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_history, container, false);

        listView = v.findViewById(R.id.lt_list);
        refreshLayout = v.findViewById(R.id.refresh);

        /**
         * 数据库操作
         */
        dao = new DAO(getContext());
        dataAdapter = new DataAdapter(getContext());
        dataAdapter.setDatalist(dao.query());
        listView.setAdapter(dataAdapter);



        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataAdapter.setDatalist(dao.query());
                listView.setAdapter(dataAdapter);
                refreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false); //停止并隐藏刷新图标
                    }
                });
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);

        //得到SearchView
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("输入日期查找 yyyyy-mm-dd");//设置默认无内容时的文字提示


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                //主动关闭软键盘
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                //将SearchView还原成图标
                searchView.onActionViewCollapsed();
                //TODO 查询相关操作
                List list = dao.search(s);
                if(list.size() == 0){
                    Toast.makeText(getActivity(), "无查询结果，请检查输入格式", Toast.LENGTH_SHORT).show();
                }else {
                    dataAdapter.setDatalist(list);
                    listView.setAdapter(dataAdapter);
                }



                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

    }
}
