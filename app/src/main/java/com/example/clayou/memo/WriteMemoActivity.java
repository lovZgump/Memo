package com.example.clayou.memo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import jp.wasabeef.richeditor.RichEditor;

public class WriteMemoActivity extends AppCompatActivity {


    private String username;
    private String memo;
    private long datetime;


    private EditText memoEdit;

    //日期
    private Date date;

    //编辑的Note
    private Memo edit_Memo;

    //隐藏其他组件
    private Boolean hide = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_memo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        Log.d("1111", "onCreate: "+username);

        memoEdit = findViewById(R.id.write_memo);

        findViewById(R.id.action_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.bottom_create).setVisibility(View.VISIBLE);
                findViewById(R.id.editor_bottom).setVisibility(View.GONE);
            }
        });
        
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                release();
            }
        });

        init_bottom();

    }

    private void release() {
        MemoManager memoManager = new MemoManager(WriteMemoActivity.this, "Memo");

        date = new Date();

        String memo = memoEdit.getText().toString();
        String titleName = "";
        if (memo.length() < 2) {
            titleName = "未命名";
        }else {
            titleName = memo.substring(0, 2);
        }
        Memo newMemo = new Memo(titleName,date, "Memo",username, memo);
        memoManager.add(newMemo);
        hideOrOpenKeyBoard();
        finish();

    }

    private void hideOrOpenKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.release_memo) {
            release();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 底部栏的初始化
     */
    private void init_bottom(){

        findViewById(R.id.open_bottom_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.bottom_create).setVisibility(View.GONE);
                findViewById(R.id.editor_bottom).setVisibility(View.VISIBLE);
            }
        });



        findViewById(R.id.reBack_bottom_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(hide){
                    //findViewById(R.id.toolbar_layout).setVisibility(View.VISIBLE);
                }else{
                    //findViewById(R.id.toolbar_layout).setVisibility(View.GONE);
                }
                hide = !hide;


            }
        });
        findViewById(R.id.location_bottom_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }
}
