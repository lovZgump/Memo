package com.example.clayou.memo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity  {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };



    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the login form.

        Log.d("memo", "onClick: i'm here");

        mEmailView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                return false;
            }
        });

        mPasswordView2 = findViewById(R.id.password2);
        mPasswordView2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

    }


    private void attemptRegister() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String password2 = mPasswordView2.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.password_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password2)) {
            mPasswordView2.setError(getString(R.string.password2_field_required));
            focusView = mPasswordView2;
            cancel =true;
        } else if (!isPasswordValid(password, password2)) {
            mPasswordView2.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView2;
            cancel =true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.email_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Account newUser = new Account();
            newUser.setUsername(email);
            newUser.setPassword(password);
            newUser.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null ) {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(RegisterActivity.this, "注册失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            intent.putExtra("newUsername", email);
            intent.putExtra("newPassword", password);
            setResult(RESULT_OK, intent);
            finish();

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic

        //List<Account> usernames = DataSupport.select("username").find(Account.class);

//        for(Account account : usernames){
//            if(account.equals(email))
//                return false;
//        }


        return email.contains("@");
    }

    private boolean isPasswordValid(String password, String password2) {
        //TODO: Replace this with your own logic
        return password.equals(password2);
    }




    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(RegisterActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


}