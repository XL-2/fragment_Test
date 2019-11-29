package com.example.lenovo.fragment_test;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DAO {

    private final DatabaseHelper databaseHelper;
    private SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
    private DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    private Date curDate;

    private String date;
    private String time;

    public DAO (Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    public void insert(){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String sql = "insert into "+dbMessage.db_name+" (date,time) values(?,?)";
        curDate =  new Date(System.currentTimeMillis());
        date = formatterDate.format(curDate);
        time = formatterTime.format(curDate);
        db.execSQL(sql,new Object[]{date,time});
        db.close();
    }

    public void deleteAll(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql = "delete from "+dbMessage.db_name;
        db.execSQL(sql);
        db.close();
    }

    public void delete(){

    }

    public List<Map<String, Object>> query(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql = "select * from "+dbMessage.db_name;
        Cursor cursor = db.rawQuery(sql,null);

        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();

        while (cursor.moveToNext()){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("date",cursor.getString(0));
            map.put("time",cursor.getString(1));
            list.add(map);
        }
        //倒序
        Collections.reverse(list);
        //System.out.println(list);

        cursor.close();
        db.close();

        return list;
    }

    public List<Map<String, Object>> search(String s){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String sql = "select * from "+dbMessage.db_name+" where date = \'"+s+"\'";
        Cursor cursor = db.rawQuery(sql,null);

        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();

        while (cursor.moveToNext()){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("date",cursor.getString(0));
            map.put("time",cursor.getString(1));
            list.add(map);
        }
        //倒序
        Collections.reverse(list);
        System.out.println(list);

        cursor.close();
        db.close();

        return list;
    }

    public void updata(){

    }

}
