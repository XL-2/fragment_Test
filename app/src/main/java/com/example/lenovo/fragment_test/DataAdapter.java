package com.example.lenovo.fragment_test;

import android.content.Context;
import android.icu.text.IDNA;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataAdapter extends BaseAdapter {
    private List<Map<String,Object>> datalist;
    private LayoutInflater inflater;

    public DataAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setDatalist(List<Map<String, Object>> datalist) {
        this.datalist = datalist;
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item,null);

        TextView date = view.findViewById(R.id.tv_date);
        TextView time = view.findViewById(R.id.tv_time);

        Map map = datalist.get(position);
        date.setText((String) map.get("date"));
        time.setText((String) map.get("time"));

        return view;
    }
}
