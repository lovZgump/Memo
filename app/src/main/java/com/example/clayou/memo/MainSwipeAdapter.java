package com.example.clayou.memo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;
import java.util.Random;

/**
 * 主菜单适配器
 */

public class MainSwipeAdapter extends BaseAdapter {


    private List<Memo> mData ;
    private Context mContext;

    public MainSwipeAdapter(Context mContext, List<Memo> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(null == convertView) {
            convertView = View.inflate(mContext, R.layout.item_main, null);
        }

        Memo note = ((Memo)getItem(position));



        TextView txt = convertView.findViewById(R.id.title_item);
        TextView date = convertView.findViewById(R.id.date_item);
        TextView content = convertView.findViewById(R.id.content_item);

        txt.setText(note.getName());
        date.setText(note.getDate().getEasyDate());


        TextView month = convertView.findViewById(R.id.month_item);
        TextView day = convertView.findViewById(R.id.day_item);

        month.setText(note.getDate().getMonthString());
        day.setText(note.getDate().getDayString());


        StringBuilder sb= new StringBuilder();
        if(StringUtil.isEmpty(note.getMemo())){
            sb.append(" ");
        }else{
            String clearContent = StringUtil.clearHtml(note.getMemo());
            content.setVisibility(View.VISIBLE);


            if(clearContent.length()<32){
                sb.append(clearContent);
            }
            else if(note.getMemo().length() > 32){
                sb.append(clearContent.substring(0,31));
                sb.append("...");
            }
        }

        content.setText(StringUtil.clearEnter(sb.toString()));


        View view = convertView.findViewById(R.id.level_item);

        Random random = new Random();
        int index = random.nextInt(3);

        if(index == 0)
            view.setBackgroundResource(R.color.green);
        else if (index == 1)
            view.setBackgroundResource(R.color.orange);
        else
            view.setBackgroundResource(R.color.red);


        return convertView;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
