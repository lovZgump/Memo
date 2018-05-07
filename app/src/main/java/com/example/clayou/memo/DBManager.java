package com.example.clayou.memo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * 数据库管理类
 */

public class DBManager {

    private Context mContext;

    public DBManager(Context context){
        mContext=context;
    }

    /**
     * 插入一个Note
     * @param folderName
     * @param note
     */
    public void insert(String folderName,Memo note) {

        //对象转换成用二进制数组处理
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(note);
            objectOutputStream.flush();

            byte []data = arrayOutputStream.toByteArray();

            objectOutputStream.close();
            arrayOutputStream.close();

            DBHelper dbHelper = DBHelper.getInstance(mContext);
            SQLiteDatabase database = dbHelper.getWritableDatabase();

            database.execSQL(
                    "insert into "+folderName+"(item) values(?)", new Object[] {data});
            database.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一个Note
     * @param folderName
     * @param note
     */

    public void delete(String folderName,Memo note){



        SQLiteDatabase database = DBHelper.getInstance(mContext).getWritableDatabase();


        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(note);
            objectOutputStream.flush();

            byte [] data = arrayOutputStream.toByteArray();

            objectOutputStream.close();
            arrayOutputStream.close();
            database.execSQL("delete from "+ folderName + " where item = ?",new Object[] {data});

            if( !folderName.equals("recycle")){
                Memo note1 = note.getClone();
                //设置删除日期
                note1.setDeleteDate(new Date());
                insert("recycle",(note1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        database.close();
    }


    /**
     * 查询某一文件夹下的Note
     * @param folderName
     * @return
     */
    public ArrayList<Memo> search(String folderName) {


        ArrayList <Memo> list = new ArrayList<>();

        Memo note ;

        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("select * from " + folderName, null);

        if (cursor != null) {

            while (cursor.moveToNext()) {
                byte data[] = cursor.getBlob(cursor.getColumnIndex("item"));
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                    note = (Memo) inputStream.readObject();
                    list.add(note);
                    inputStream.close();
                    arrayInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }

        return list;
    }


    /**
     * Note转化为二进制数组
     * @param note
     * @return
     */
    private byte[] getData(Memo note){

        byte [] data = null;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(note);
            objectOutputStream.flush();
            data = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  data;
    }

    /**
     * 获取表名
     * @return
     */
    public ArrayList<String> getTableName(){

        ArrayList<String>  tableName = new ArrayList<>();

        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery
                ("select name from sqlite_master where type='table' order by name", null);

        while(cursor.moveToNext()){
            String name = cursor.getString(0);
            tableName.add(name);
        }
        //删除系统表
        tableName.remove("android_metadata");
        tableName.remove("sqlite_sequence");
        //回收站
        tableName.remove("recycle");

        cursor.close();
        return tableName;
    }

    /**
     * 获取表的长度
     * @param tableName
     * @return
     */
    public int getTableLength(String tableName){


        if(tableName.equals("最近删除")){
            tableName = "recycle";
        }

        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor=database.rawQuery("select count(*) from "+ tableName, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();

        return (int)count;
    }

    /**
     * 清空文件夹
     * @param folderName
     * @return
     */
    public int clearAllFolder(String folderName){
        DBHelper dbHelper = DBHelper.getInstance(mContext);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        return  database.delete(folderName,null,null);
    }

    /**
     * 更新Note
     * @param folderName
     * @param preNote
     * @param newNote
     */
    public void upDate(String folderName,Memo preNote,Memo newNote){

        byte[] pre_data = getData(preNote);
        byte[] new_data = getData(newNote);

        SQLiteDatabase database = DBHelper.getInstance(mContext).getWritableDatabase();

        database.execSQL("update "+ folderName+ " set item = ? where item = ?",
                new Object[]{new_data,pre_data});

        database.close();
    }

}
