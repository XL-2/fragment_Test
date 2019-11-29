package com.example.lenovo.fragment_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FunctionAdpter extends BaseAdapter {
    private List<String> list;

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    private LayoutInflater inflater;

    public FunctionAdpter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.function_item, null);

        TextView function = view.findViewById(R.id.functionName);
        ImageView image = view.findViewById(R.id.functionImage);

        function.setText(list.get(position));
        image.setImageResource(R.drawable.ic_find_previous_holo_light);

        return view;
    }
}
