package com.example.clayou.memo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.Collections;
import java.util.List;

public class UserInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String username;

    private MemoManager memoManager;

    private SwipeListView mListView;
    private List<Memo> mData;

    private static boolean isExit;

    //初始化
    private String currentFolderName ="Memo";

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listView_setting();
        findViewById(R.id.action_menu).bringToFront();
    }

    protected void onStart(){
        super.onStart();
        listView_setting();
        findViewById(R.id.action_menu).bringToFront();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 主界面的初始化
     */
    public void init (){

        listView_setting();
        fab_setting();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if (id == R.id.nav_logout) {
            Toast.makeText(UserInfoActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return  false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void exit() {
        if (!isExit) {
            isExit = true;
            //Toast.makeText(this, "再按一次返回键退出应用", Toast.LENGTH_SHORT).show();
            Snackbar.make(this.findViewById(R.id.fab), "再按一次返回键退出应用", Snackbar.LENGTH_SHORT)
                       .setAction("Action", null).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);
        }
    }

    private void hide_fabMenu(){
        //关闭fab菜单
        FloatingActionsMenu menu = findViewById(R.id.action_menu);
        if(menu!=null) menu.collapse();
    }

    public void listView_setting(){

        hide_fabMenu();

        mData = new DBManager(this).search(currentFolderName);

        Collections.sort(mData, new ComparatorUtil());

        MainSwipeAdapter adapter = new MainSwipeAdapter(this,mData);

        memoManager = new MemoManager(this, currentFolderName, mData, adapter);

        MainCreator mainCreator = new MainCreator(this);

        mListView = findViewById(R.id.list_view);
        mListView.setMenuCreator(mainCreator);
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        mListView.setAdapter(adapter);

        MainScrollview scrollview = findViewById(R.id.main_scrollView);
        scrollview.setOnScrollListener(new MainScrollview.ScrollViewListener() {
            @Override
            public void onScroll(int dy) {
                if (dy > 0) {//下滑
                    showOrHideFab(false);
                } else if (dy <= 0 ) {//上滑
                    showOrHideFab(true);
                }
            }
        });

        setDescription();
        view_Listener();
        emptyListCheck();
    }

    private void view_Listener() {

        //点击监听
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                memoManager.ItemClick(position);
            }
        });


        mListView.setOnMenuItemClickListener( new SwipeMenuListView.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {//具体实现

                switch (index){
                    //edit
                    case 0:
                        memoManager.editClick(position);
                        break;
                    case 1:
//                        Intent intent = new Intent(MainActivity.this,FilesActivity.class);
//                        intent.putExtra("move",true);
//
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("note",mData.get(position));
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//
//                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//                        //move
                        break;
                    case 2:
                        memoManager.deleteClick(position);
                        emptyListCheck();
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public void emptyListCheck(){



        int number = 0;
        if(mData!=null){
            number=mData.size();
        }

        if(number == 0) {
            //hide and show
            mListView.setVisibility(View.GONE);
            RelativeLayout empty = (RelativeLayout) findViewById(R.id.empty);
            empty.setVisibility(View.VISIBLE);

            TextView info = (TextView) findViewById(R.id.text_empty);
            info.setText("还没有任何Memo");
        }else{
            mListView.setVisibility(View.VISIBLE);
            RelativeLayout empty = (RelativeLayout) findViewById(R.id.empty);
            empty.setVisibility(View.GONE);
        }
    }

    private void fab_setting(){


        com.getbase.floatingactionbutton.FloatingActionButton fab = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.action_note);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        com.getbase.floatingactionbutton.FloatingActionButton fab_quick = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.action_quick);

        fab_quick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //noteManager.add();
                //关闭fab菜单
                FloatingActionsMenu menu = (FloatingActionsMenu)findViewById(R.id.action_menu);
                menu.collapse();

                Intent intent = new Intent(UserInfoActivity.this,WriteMemoActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

                findViewById(R.id.action_menu).bringToFront();
            }
        });
    }

    private void showOrHideFab(boolean show){

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);

        if(show){
            fab.setVisibility(View.VISIBLE);
        }else{
            fab.setVisibility(View.GONE);
        }

    }

    private void setDescription(){
        if(AppUtil.haveDescription(this)){
            memoManager.addDescription();
        }
    }
}
