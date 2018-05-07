package com.example.clayou.memo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by 10295 on 2018/5/7.
 */

public class ContentActivity extends AppCompatActivity {

    private Memo memo;

    private MemoManager memoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Intent intent = getIntent();
        memo = (Memo)intent.getSerializableExtra("memo");
        init();
    }

    private void init(){
        init_toolbar();
        init_view();
        init_bottom();
    }

    private void init_toolbar() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_content);

        mToolbar.setNavigationIcon(R.drawable.pic_back);


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
            }
        });

        TextView mTitle = (TextView) findViewById(R.id.title_toolbar);
        mTitle.setText(memo.getName());

    }

    private void init_view() {
        memoManager = new MemoManager(this, "Memo");

        final TextView date = (TextView)findViewById(R.id.date);
        date.setText(memo.getDate().getDetailDate());

        RichEditor content = (RichEditor) findViewById(R.id.editor);
        content.setHtml(memo.getMemo());
        content.setInputEnabled(false);

        TextView numberFollow = (TextView)findViewById(R.id.numberFollow_content);
        numberFollow.setText(" "+StringUtil.clearHtml(content.getHtml()).length()+" ");

        Random random = new Random();
        int index = random.nextInt(3);
        switch (index) {
            case 1:
                findViewById(R.id.level_content).setBackgroundResource(R.drawable.radius_green);
                break;
            case 2:
                findViewById(R.id.level_content).setBackgroundResource(R.drawable.radius_orange);
                break;
            case 3:
                findViewById(R.id.level_content).setBackgroundResource(R.drawable.radius_red);
                break;
        }

    }

    private void init_bottom() {

        //编辑
        findViewById(R.id.edit_bottom_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //删除
        findViewById(R.id.delete_bottom_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memoManager.deleteNote(memo);
                finish();
            }
        });
        //移动到
        findViewById(R.id.move_bottom_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //位置
        findViewById(R.id.location_bottom_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
    }

    private void getLocation() {
        //正在获取定位....
        final ProDialog proDialog = new ProDialog(this,
                getResources().getString(R.string.location));

        proDialog.show();

        final TextView location = (TextView) findViewById(R.id.location_content);
        location.setVisibility(View.VISIBLE);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                proDialog.dismiss();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1500);

        location.setText("哈哈，我猜你是地球人");
    }

}
