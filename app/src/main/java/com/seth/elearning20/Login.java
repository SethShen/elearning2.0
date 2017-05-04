package com.seth.elearning20;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seth.elearning20.login_regist.ForgetPassPage;
import com.seth.elearning20.login_regist.LoginPage;
import com.seth.elearning20.login_regist.RegistPage;

public class Login extends FragmentActivity {

    private Fragment login_fm;
    private Fragment regist_fm;
    private Fragment forgetPass_fm;
    private Fragment resetPass_fm;
    private FragmentManager fm;
    private static Fragment contentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();//隐藏标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏状态栏
        setContentView(R.layout.activity_login);
        fm = getSupportFragmentManager();

        login_fm = fm.findFragmentById(R.id.activity_login);
        regist_fm = fm.findFragmentById(R.id.activity_login);
        forgetPass_fm = fm.findFragmentById(R.id.activity_login);
        resetPass_fm = fm.findFragmentById(R.id.activity_login);

        if(login_fm == null){
            login_fm = new LoginPage();
            fm.beginTransaction().add(R.id.activity_login, login_fm).commit();
            contentFragment = login_fm;
        }

    }

    public static Fragment getContentFragment() {
        return contentFragment;
    }

    public static void setContentFragment(Fragment contentFragment1) {
        contentFragment = contentFragment1;
    }

    @Override
    public void finish() {
        super.finish();
        Log.i("SQLite","销毁登陆activity");
    }
}
//    //调用注册fragment
//    public void Regist(){
//        if(regist_fm == null){
//            regist_fm = new RegistPage();
//            fm.beginTransaction().replace(R.id.activity_login, regist_fm).commit();
//        }
//    }
//    //调用密码找回fragment
//    public void FindPassword(){
//        if(forgetPass_fm==null){
//            forgetPass_fm = new ForgetPassPage();
//            fm.beginTransaction().replace(R.id.activity_login, forgetPass_fm);
//        }
//    }