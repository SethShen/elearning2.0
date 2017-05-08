package com.seth.elearning20.login_regist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seth.elearning20.FrontPage;
import com.seth.elearning20.LanuchPage;
import com.seth.elearning20.Login;
import com.seth.elearning20.R;
import com.seth.elearning20.info.UserInfo;
import com.seth.elearning20.service.CheckService;
import com.seth.elearning20.sqlite.SqlDao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Seth on 2017/4/29.
 */

public class LoginPage extends Fragment{
    private Button loginBtn;
    private RelativeLayout findPassword;
    private RelativeLayout newRegist;
    private EditText usrName;
    private EditText usrPassword;
    private static boolean flag = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        loginBtn = (Button) view.findViewById(R.id.login_btn);
        findPassword = (RelativeLayout) view.findViewById(R.id.fetchPassword);
        newRegist = (RelativeLayout) view.findViewById(R.id.regist);
        usrName = (EditText) view.findViewById(R.id.username);
        usrPassword = (EditText) view.findViewById(R.id.password);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = usrName.getText().toString();
                final String password = usrPassword.getText().toString();
                /******************本地数据库校验**********************
                 List<String> local = new SqlDao(getContext()).usrQuery();
                 if(name.equals("")){
                 toast("请输入用户名！");
                 }else if(password.equals("")){
                 toast("请输入密码！");
                 }else if(local==null || ! name.equals(local.get(0))){
                 toast("用户名或密码错误");
                 }else if(local==null || ! password.equals(local.get(1))){
                 toast("用户名或密码错误");
                 }else {
                 Intent intent = new Intent(getActivity(), FrontPage.class);
                 startActivity(intent);
                 getActivity().finish();
                 }
                 *************************网络验证**************************/
                /*在service中获取网络验证*/
                new CheckService().save(getpath(name, password), 3);
                /*延时等待网络判断*/
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(flag==true){
                            UserInfo userInfo = UserInfo.setsUserInfo(name,password);
                            new SqlDao(getContext()).addUsrInfo(userInfo);
                            Intent intent = new Intent(getActivity(), FrontPage.class);
                            startActivity(intent);
                            getActivity().finish();
                        }else{
                            toast("用户名或密码错误");
                        }
                    }
                }, 1000);
                /*********************************************************************/

            }
        });
        findPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ForgetPassPage();
                //Fragment fragment1 = Login.getContentFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.activity_login,fragment);
                //transaction.hide(fragment1).add(R.id.activity_login, fragment);
                Login.setContentFragment(fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        newRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new RegistPage();
                //Fragment fragment1 = Login.getContentFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.activity_login,fragment);
                //transaction.hide(fragment1).add(R.id.activity_login, fragment);
                Login.setContentFragment(fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
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
    //吐司的一个小方法
    private void toast(final String str) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void setFlag(boolean flag) {
        LoginPage.flag = flag;
    }
}


