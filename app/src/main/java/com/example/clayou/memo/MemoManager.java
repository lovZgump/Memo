package com.example.clayou.memo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by 10295 on 2018/5/7.
 */

public class MemoManager {

    private Context mContext;
    private List<Memo> list;

    private String currentFolderName;//Memo的所属文件夹
    private BaseAdapter adapter; //适配器
    private DBManager dbManager;//数据库管理类

    public MemoManager(Context mContext, String currentFolderName) {
        this.currentFolderName = currentFolderName;
        this.mContext = mContext;
        dbManager = new DBManager(mContext);
    }

    public MemoManager(Context mContext,String currentFolderName, List<Memo> list, BaseAdapter adapter) {
        this (mContext, currentFolderName);
        this.list = list;
        this.adapter = adapter;
    }

    public void addDescription() {

        Memo memo = new Memo("第一次使用Memo", new Date(), "相逢是缘，谢谢使用...");
        add(memo);
    }

    /**
     * 新建Note
     * @param note
     */
    public void add(Memo note){
        dbManager.insert("Memo",note);
    }

    public void ItemClick(int position){
        final Memo select_item = list.get(position);
        ItemClick(select_item);
    }

    /**
     * 打开note
     * @param select_item
     */
    private void ItemClick(Memo select_item){

        Intent intent = new Intent(mContext,ContentActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("memo", select_item);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    /**
     * 点击了删除
     * @param position
     */
    public void deleteClick(int position){


        Memo select_item = list.get(position);
        delete(select_item);

    }

    /**
     * 数据库交互
     * @param note
     */
    private void delete(Memo note) {
        list.remove(note);

        adapter.notifyDataSetChanged();

        dbManager.delete("Memo",note);
    }

    /**
     * 删除Note
     * @param note
     */
    public void deleteNote(Memo note) {

        final Memo note1 = note;
        dbManager.delete("Memo",note1);

    }

}
