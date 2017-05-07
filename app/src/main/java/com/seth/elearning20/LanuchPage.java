package com.seth.elearning20;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ImageView;

import com.seth.elearning20.service.CheckService;
import com.seth.elearning20.sqlite.SqlDao;
import com.seth.elearning20.utils.StreamUtils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seth on 2017/5/4.
 */

public class LanuchPage extends Activity {
    private static boolean flag = false;



    /*用于设置验证结果*/
    public static void setFlag(boolean Flag) {
        flag = Flag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        String getName ;
        String getPassword ;
        List<String>  localact = new SqlDao(getApplication()).usrQuery();
        if(localact!=null) {
            getName = localact.get(0);
            getPassword = localact.get(1);
        /*在service中获取网络验证*/
            new CheckService().save(getpath(getName, getPassword), 1);
        }
        /*延时等待网络判断*/
        new Handler().postDelayed(new splashhandler(), 3000);


    }

    /**
     * 将name,和password转换成url
     * @param name
     * @param password
     * @return  url
     */
    private String getpath(String name,String password) {
        String path = "http://115.159.71.92:8080/eLearningManager/user/login?name=";
        String usr_pass = name;
        try {
            usr_pass = URLEncoder.encode(usr_pass,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        path = path + usr_pass+ "&password=" + password;
        return path;
    }

    /***
     * 等待service网络请求后判断验证是否成功，并判断进入登陆还是主界面
     */
    private class splashhandler implements Runnable {
        @Override
        public void run() {
            if(flag)
                startActivity(new Intent(getApplication(),FrontPage.class));
            else
                startActivity(new Intent(getApplication(),Login.class));
            LanuchPage.this.finish();
        }
    }


}
