package com.example.clayou.memo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by 10295 on 2018/5/8.
 */

public class CreateNewMemoActivity extends AppCompatActivity implements View.OnClickListener{

    //题目
    private EditText title;
    //内容
    private RichEditor mEditor;
    //日期
    private Date date;
    //日期视图
    private TextView date_view;
    //位置
    private TextView location;

    private String currentFolderName;
    private String username;

    //模式
    private boolean model; // (false 新建模式   true 编辑模式)

    //编辑的Note
    private Memo edit_Memo;

    //隐藏其他组件
    private Boolean hide = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        init();
    }

    private void init() {

        TextView  model_title = (TextView) findViewById(R.id.title_toolbar);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        model = bundle.getBoolean("model");
        if (model) {
            model_title.setText("编辑Memo");
            edit_Memo = (Memo)intent.getSerializableExtra("memo");
            currentFolderName = edit_Memo.getFolderName();
            username = edit_Memo.getUsername();
        }else {
            model_title.setText("新建Memo");
        }

        init_NoteEditor();
        init_view();
        init_Toolbar();

        if(model) {
            init_edit();
        }

    }

    private  void init_view(){

        title =  findViewById(R.id.title_create);
        location  = findViewById(R.id.location_create);

        date_view = findViewById(R.id.date_create);
        date = new Date( );
        date_view.setText(date.getDetailDate());



        Button btn_red = (Button) findViewById(R.id.btn_red);
        btn_red.setOnClickListener(this);
        Button btn_orange = (Button) findViewById(R.id.btn_orange);
        btn_orange.setOnClickListener(this);
        Button btn_green = (Button) findViewById(R.id.btn_green);
        btn_green.setOnClickListener(this);

        init_bottom();
    }

    private  void init_NoteEditor() {

        mEditor =  findViewById(R.id.editor);

        mEditor.setFontSize(14);
        mEditor.setPlaceholder("在这里写下内容");

        findViewById(R.id.action_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.bottom_create).setVisibility(View.VISIBLE);
                findViewById(R.id.editor_bottom).setVisibility(View.GONE);
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });


        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });
        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_deleteline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });


        findViewById(R.id.action_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertTodo();
            }
        });

        findViewById(R.id.action_menulist).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mEditor.setNumbers();
            }
        });


        findViewById(R.id.action_menubullte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });


        findViewById(R.id.action_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });
        findViewById(R.id.action_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });
    }

    private void init_edit(){

        Log.d("name", "init_edit: "+edit_Memo.getName());
        title.setText( edit_Memo.getName() );
        mEditor.setHtml( edit_Memo.getMemo() );
        date_view.setText( edit_Memo.getDate().getDetailDate() );
        location.setText("神秘空间");

    }

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

                    findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
                    findViewById(R.id.second).setVisibility(View.VISIBLE);
                    findViewById(R.id.third).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.toolbar).setVisibility(View.GONE);
                    findViewById(R.id.second).setVisibility(View.GONE);
                    findViewById(R.id.third).setVisibility(View.GONE);
                }
                hide = !hide;


            }
        });
        findViewById(R.id.location_bottom_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getLocation();
            }
        });

    }

    private  void init_Toolbar(){

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.pic_deleteall);//设置取消图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
        });


        toolbar.inflateMenu(R.menu.menu_create);//设置右上角的填充菜单


        if(model) {//编辑模式
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    MemoManager memoManager = new MemoManager(CreateNewMemoActivity.this, currentFolderName);

                    Memo newNote = new Memo(title.getText().toString(), edit_Memo.getDate(), currentFolderName,
                            username, mEditor.getHtml());

                    memoManager.update(edit_Memo, newNote);
                    Toast.makeText(CreateNewMemoActivity.this, "已保存", Toast.LENGTH_SHORT).show();
                    finish();

                    return false;
                }
            });


        }else {//新建模式
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {


//                    String titleName = title.getText().toString();
//                    if(StringUtil.isEmpty(titleName)){
//                        titleName="未命名";
//                    }
//                    Note create_note = new Note(titleName, date,
//                            location.getText().toString(), mEditor.getHtml(),
//                            currentFolderName, level);
//
//                    NoteManager noteManager = new NoteManager(CreateActivity.this, currentFolderName);
//                    noteManager.add(create_note);
//                    hideOrOpenKeyBoard();
//                    finish();

                    return false;
                }
            });

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



    /**
     * 键盘的显示和隐藏
     */
    private void hideOrOpenKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
