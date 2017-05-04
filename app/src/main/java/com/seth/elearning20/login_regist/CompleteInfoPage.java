package com.seth.elearning20.login_regist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.seth.elearning20.FrontPage;
import com.seth.elearning20.R;
import com.seth.elearning20.info.UserInfo;
import com.seth.elearning20.sqlite.SqlDao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Seth on 2017/5/3.
 */

public class CompleteInfoPage extends Fragment{
    private EditText usrName;
    private EditText usrPassword1;
    private EditText usrPassword2;
    private String usrPhone;
    private EditText usrEmail;
    private RelativeLayout save;
    private boolean result;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.complete_information_fragment, container, false);
        usrPhone = "18868217689";
        FindViewById(view);

        SaveInfo();


        return view;
    }

    private void SaveInfo() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = usrName.getText().toString();
                String password1 = usrPassword1.getText().toString();
                String password2 = usrPassword2.getText().toString();
                String Email = usrEmail.getText().toString();
                UserInfo usr;
                if(name.equals("")){
                    toast("请输入用户名！");
                }else if(password1.equals("")){
                    toast("请输入密码！");
                }else if(password2.equals("")){
                    toast("请输认密码");
                }else if(Email.equals("")){
                    toast("请输入邮箱");
                }else if(!password1.equals(password2)){
                    toast("密码不相等！");
                }else if(!checkEmail(Email)){
                    toast("请输入正确的邮箱格式!");
                }else{
                    usr = new UserInfo(name,password1,usrPhone,Email,null);
                    result = new SqlDao(getContext()).addUsrInfo(usr);
                    toast(""+result);
                }
                if(result){
                    Intent intent = new Intent(getActivity(), FrontPage.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }

        });

    }

    private void FindViewById(View view) {
        usrName = (EditText) view.findViewById(R.id.nick_name);
        usrPassword1 = (EditText) view.findViewById(R.id.password1);
        usrPassword2 = (EditText) view.findViewById(R.id.password2);
        usrEmail = (EditText) view.findViewById(R.id.email_address);
        save = (RelativeLayout) view.findViewById(R.id.save_info);
    }
    /**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){
        boolean flag = false;
        try{
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
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
}