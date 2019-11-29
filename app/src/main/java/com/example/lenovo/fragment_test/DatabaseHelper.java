package com.example.lenovo.fragment_test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {


    /**
     *
     * context
     * name
     * factory
     * version
     */
    public DatabaseHelper(Context context) {
        super(context, "MyDatabase.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库时的回调函数（第一次创建）
        Log.e("DatabaseHelper.db","创建数据库");
        //创建字段
        String sql = "create table "+dbMessage.db_name+" (date VARCHAR,time VARCHAR)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //升级数据库时的回调函数
        Log.e("DatabaseHelper","升级数据库");
    }
}
