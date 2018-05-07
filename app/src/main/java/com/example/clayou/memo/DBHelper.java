package com.example.clayou.memo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库工具类
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String CREATE_NOTE =

            "create table Memo ("
                    + "item blob)";


    private Context mContext;


    private static DBHelper dbHelper = null;


    private DBHelper(Context context) {
        //数据库名
        super(context, "Memo.db", null, 6);
        this.mContext = context;
    }

    /**
     * 单例模式
     * @param context
     * @return
     */
    public static DBHelper getInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }

    /**
     * 初始化
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Memo");
        onCreate(db);
    }


}

