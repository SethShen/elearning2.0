package com.seth.elearning20.login_regist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.seth.elearning20.FrontPage;
import com.seth.elearning20.R;
import com.seth.elearning20.info.UserInfo;
import com.seth.elearning20.service.CheckService;
import com.seth.elearning20.sqlite.SqlDao;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Seth on 2017/5/3.
 */

public class CompleteInfoPage extends Fragment {
    private static  CompleteInfoPage sCompleteInfoPage;
    private EditText usrName;
    private EditText usrPassword1;
    private EditText usrPassword2;
    private String usrPhone;
    private EditText usrEmail;
    private static CircleImageView frog;
    private RelativeLayout save;
    private boolean result;
    private static boolean flag = false;
    private static File img_file;
    private UserInfo usr;

    /*传值key*/
    private static boolean resultflag = false;
    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sCompleteInfoPage = this;
        View view = inflater.inflate(R.layout.complete_information_fragment, container, false);
        usrPhone = "18868217689";
        FindViewById(view);

        setUsrFrog();
        SaveInfo();
        return view;
    }


    private void setUsrFrog() {
        frog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialogFragment dialog = new ImageDialogFragment();
                dialog.show(getFragmentManager(),"imageDialogFragment");

            }
        });

    }

    /****
     * 监听事件
     */
    private void SaveInfo() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = usrName.getText().toString();
                String password1 = usrPassword1.getText().toString();
                String password2 = usrPassword2.getText().toString();
                String Email = usrEmail.getText().toString();
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
                    String imgUrl = null;
                    if(img_file!=null)
                        imgUrl = img_file.getAbsolutePath();
                    usr = new UserInfo(name,password1,usrPhone,Email,imgUrl);
                    new CheckService().save(setPath(name,password1,usrPhone,Email),2);
                    toast("正在验证用户信息。。。");
                    //等待网络请求
                    new Handler().postDelayed(new checkInfo(),2500);
                }
            }

        });

    }

    public static void setFlag(boolean Flag) {
        flag = Flag;
    }

    /***
     * 将用户传入的参数转换为Url
     * @param name  用户名
     * @param password1 密码
     * @param usrPhone  手机号
     * @param usrEmail  邮箱地址
     * @return  Url
     */
    private String setPath(String name, String password1, String usrPhone, String usrEmail) {
        String path = "http://115.159.71.92:8080/eLearningManager/user/register?name=";
        try {
            String url_name = URLEncoder.encode(name,"utf-8");
            path = path + url_name + "&password=" + password1 + "&phoneNumber=" + usrPhone + "&email=" + usrEmail;
            Log.i("backnum",path);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return path;
    }


    private void FindViewById(View view) {
        usrName = (EditText) view.findViewById(R.id.nick_name);
        usrPassword1 = (EditText) view.findViewById(R.id.password1);
        usrPassword2 = (EditText) view.findViewById(R.id.password2);
        usrEmail = (EditText) view.findViewById(R.id.email_address);
        save = (RelativeLayout) view.findViewById(R.id.save_info);
        frog = (CircleImageView) view.findViewById(R.id.usrFrog);
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



    private class checkInfo implements Runnable {
        @Override
        public void run() {
            if(flag){
                result = new SqlDao(getContext()).addUsrInfo(usr);
                toast("信息保存成功"+result);
                Intent intent = new Intent(getActivity(), FrontPage.class);
                startActivity(intent);
                getActivity().finish();
            }else{
                toast("用户名已存在！");
            }
        }
    }


    public static CompleteInfoPage getCompleteInfoPage() {
        return sCompleteInfoPage;
    }

    public static CircleImageView getFrog() {
        return frog;
    }

    public static void setImg_file(File img_file) {
        CompleteInfoPage.img_file = img_file;
    }
}