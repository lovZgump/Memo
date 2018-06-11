package com.example.clayou.memo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * 编辑对话框
 */

public class EditDialog extends android.app.Dialog implements View.OnClickListener{

    private TextView title;
    private EditText info;
    private Button yes;
    private Button no;
    private Context mContext;


    private MyOnClickListener noListener;
    private MyOnClickListener yesListener;

    public EditDialog(Context context) {
        super(context, R.style.MyDialog);
        mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit);
        initView();
        initEvent();
    }

    /**
     * 初始化界面
     */
    protected void initView(){

        yes = (Button) findViewById(R.id.yes_dialog);
        no = (Button) findViewById(R.id.no_dialog);
        info = (EditText) findViewById(R.id.edit_dialog);
        title = (TextView) findViewById(R.id.title_dialog);

        findViewById(R.id.btn_red).setOnClickListener(this);
        findViewById(R.id.btn_green).setOnClickListener(this);
        findViewById(R.id.btn_orange).setOnClickListener(this);
    }


    protected  void initEvent() {

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesListener != null) {
                    yesListener.onClick();
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noListener != null) {
                  noListener.onClick();
                }else{
                    dismiss();
                }
            }
        });

    }

    public void setYesListener (MyOnClickListener listener){
        this.yesListener = listener;
    }


    public void setNoListener (MyOnClickListener listener){
        this.noListener = listener;
    }

    public String getInfo() {

        return info.getText().toString();
    }



    public void setTitle(String title) {
        this.title.setText(title);
    }


    public void setInfo(String msg){
        if(info!=null){
            info.setText(msg);
            info.setSelection(info.length());
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_red:
            case R.id.btn_green:
            case R.id.btn_orange:
                break;
        }
    }

}
