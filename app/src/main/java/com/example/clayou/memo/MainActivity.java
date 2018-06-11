package com.example.clayou.memo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;

    private EditText accountEdit;
    private EditText passwordEdit;

    private String username;
    private String password;

    private static final int LOGIN = 1;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN:
                    attemptLogin();
                    break;
                default:
                    break;
            }
        }
    };

    private int flag = 1;

    private boolean isExit;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this, "c234fd8bea5c0e096466625d03a86f2a");


        pref = PreferenceManager.getDefaultSharedPreferences(this);

        accountEdit = findViewById(R.id.input_account);
        passwordEdit = findViewById(R.id.input_password);
        rememberPass = findViewById(R.id.remember_pass);

        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        boolean isRemember = pref.getBoolean("remember_password", false);
        if(isRemember){
            // 将账号和密码都设置到文本框中
            username = pref.getString("username", "");
            password = pref.getString("password", "");
            accountEdit.setText(username);
            accountEdit.setSelection(username.length());
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }


        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = accountEdit.getText().toString();
                password = passwordEdit.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BmobQuery<Account> query = new BmobQuery<Account>();
                        query.addWhereEqualTo("username", username);
                        query.findObjects(new FindListener<Account>() {
                            @Override
                            public void done(List<Account> list, BmobException e) {
                                if (list.isEmpty() || !(list.get(0).getPassword().equals(password))) {
                                    //Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                    flag = 0;
                                } else if (list.get(0).getPassword().equals(password)) {
                                    flag = 1;
                                }
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message message = new Message();
                        message.what = LOGIN;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });

        Button register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("111", "onClick: i'm here");

                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK) {
                    String newUsername = data.getStringExtra("newUsername");
                    String newPassword = data.getStringExtra("newPassword");
                    accountEdit.setText(newUsername);
                    passwordEdit.setText(newPassword);
                    accountEdit.setSelection(newUsername.length());
                }
                break;
            default:
        }
    }


    private void attemptLogin(){

        if (flag == 1){
            Log.d("111", "onPostExecute: i'm here");
            editor = pref.edit();
            if (rememberPass.isChecked()) {
                editor.putBoolean("remember_password", true);
                editor.putString("username", username);
                editor.putString("password", password);
            } else {
                editor.putBoolean("remember_password", true);
                editor.putString("username", username);
                editor.putString("password", "");
            }
            editor.apply();

            Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }else {
            Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            passwordEdit.requestFocus();
            passwordEdit.setSelection(password.length());
        }

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
            Toast.makeText(this, "再按一次返回键退出应用", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);
        }
    }


//    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
//
//        private final String mEmail;
//        private final String mPassword;
//
//        UserLoginTask(String email, String password) {
//            mEmail = email;
//            mPassword = password;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            BmobQuery<Account> query = new BmobQuery<Account>();
//            query.addWhereEqualTo("username", username);
//            query.findObjects(new FindListener<Account>() {
//                @Override
//                public void done(List<Account> list, BmobException e) {
//                    if (list.isEmpty() || !(list.get(0).getPassword().equals(password))) {
//                        Log.d("111", "done: "+list.get(0).getUsername());
//                        //Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
//                        flag = 0;
//                    } else if (list.get(0).getPassword().equals(password)) {
//                        flag = 1;
//                    }
//                }
//            });
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            //mAuthTask = null;
//
//            if(flag == 1){
//                Log.d("111", "onPostExecute: i'm here");
//                editor = pref.edit();
//                if (rememberPass.isChecked()) {
//                    editor.putBoolean("remember_password", true);
//                    editor.putString("username", username);
//                    editor.putString("password", password);
//                } else {
//                    editor.putBoolean("remember_password", true);
//                    editor.putString("username", username);
//                    editor.putString("password", "");
//                }
//                editor.apply();
//
//                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
//                intent.putExtra("username", username);
//                startActivity(intent);
//                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
//
//            } else {
//                Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
//                passwordEdit.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            //mAuthTask = null;
//        }
//    }

}

