package com.seth.elearning20.info;

import android.content.Context;
import android.util.Log;

import com.seth.elearning20.sqlite.SqlDao;

/**
 * Created by Seth on 2017/5/4.
 */

public class UserInfo {
    private static UserInfo sUserInfo;
    private String name;
    private String password;
    private String phone;
    private String email;
    private String frogUrl;

    public UserInfo(){}


    public UserInfo(String name, String password, String phone, String email, String frogUrl) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.frogUrl = frogUrl;
    }
    public static UserInfo setsUserInfo(String name, String password){
        sUserInfo = new UserInfo();
        sUserInfo.setName(name);
        sUserInfo.setPassword(password);
        return sUserInfo;
    }

    public static UserInfo getUserInfo(Context context){
        if(sUserInfo==null){
            sUserInfo = new SqlDao(context).usrQuery();
        }
        return sUserInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFrogUrl() {
        return frogUrl;
    }

    public void setFrogUrl(String frogUrl) {
        this.frogUrl = frogUrl;
    }
}
