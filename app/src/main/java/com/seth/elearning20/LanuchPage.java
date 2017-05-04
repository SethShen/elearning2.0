package com.seth.elearning20;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.widget.ImageView;

import com.seth.elearning20.sqlite.SqlDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seth on 2017/5/4.
 */

public class LanuchPage extends Activity {
    private boolean flag = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);


//        List<String>  localact  = new ArrayList<>();
//        localact.add("aaa");
//        localact.add("12356");
        String getName = "aaa";
        String getPassword = "123456";
        List<String>  localact = new SqlDao(getApplication()).usrQuery();
        if(localact!=null&&getName.equals(localact.get(0))&&getPassword.equals(localact.get(1))){
            flag = true;
        }else{
            flag = false;
        }
        new Handler().postDelayed(new splashhandler(), 2000);


    }

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
