package com.example.clayou.memo;

import cn.bmob.v3.BmobObject;

/**
 * Created by 10295 on 2018/4/11.
 */

//用户类
public class Account extends BmobObject {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
