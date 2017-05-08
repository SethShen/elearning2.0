package com.seth.elearning20.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.seth.elearning20.LanuchPage;
import com.seth.elearning20.info.UserInfo;
import com.seth.elearning20.login_regist.CompleteInfoPage;
import com.seth.elearning20.login_regist.LoginPage;
import com.seth.elearning20.utils.StreamUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by Seth on 2017/5/5.
 */

public class CheckService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static void uploadFrog(final File img, final UserInfo userInfo, final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.i("backresult","start");
                OkHttpUtils.post()
                        .addParams("name",userInfo.getName())
                        .addFile("usrFrog","usr_frog"+userInfo.getPhone()+".jpg",img)
                        .url("http://115.159.71.92:8080/eLearningManager/user/uploadPic")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.i("backresult","error");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                                Log.i("backresult",response);
                            }
                        });
            }
        }).start();
    }
    /**
     * 根据传入path调用SendGetCheck()请求网络
     * @param path
     */
    public static void save(final String path, final int i) {
        //用子线程跑网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result;
                result = SendGetCheck(path);
                if(i==1)
                    LanuchPage.setFlag(result);
                else if(i==2)
                    CompleteInfoPage.setFlag(result);
                else if(i==3)
                    LoginPage.setFlag(result);
            }
        }).start();
    }

    /****
     * 请求网络并判断结果
     * @param path
     * @return 判断成功与否
     */
    public static boolean SendGetCheck(String path){
        boolean flag = false;
        try {
            Log.i("backnum",path);
            StringBuilder url = new StringBuilder(path);
            //获取HttpURLConnection对象
            HttpURLConnection conn = (HttpURLConnection)new URL(url.toString()).openConnection();

            Log.i("backnum","1");
            //设置超时时间
            conn.setConnectTimeout(2500);
            Log.i("backnum","2");
            //请求方式
            conn.setRequestMethod("GET");
            Log.i("backnum","3");
            int code = conn.getResponseCode();
            Log.i("backnum",code+"4");
            if(code == 200){
                InputStream inputStream = conn.getInputStream();
                String result = StreamUtils.streamToString(inputStream);
                Log.i("backnum",result);
                if(result.equals("1"))
                    flag = true;
                else
                    flag =  false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 发送GET请求
     * @param path  请求路径
     * @return 请求是否成功
     * @throws Exception
     */
    private static boolean SendGETRequest(String path, String ecoding) throws Exception{
        // http://127.0.0.1:8080/Register/ManageServlet?name=1233&password=abc
        StringBuilder url = new StringBuilder(path);
        //获取HttpURLConnection对象
        HttpURLConnection conn = (HttpURLConnection)new URL(url.toString()).openConnection();
        //设置超时时间
        conn.setConnectTimeout(3000);
        //请求方式
        conn.setRequestMethod("GET");
        int code = conn.getResponseCode();
        Log.i("backnum",code+"");
        if(code == 200){
            InputStream inputStream = conn.getInputStream();
            String result = StreamUtils.streamToString(inputStream);
            Log.i("backnum",result);
            if(result.equals("1"))
                return true;
            else
                return false;
        }else {
            return false;
        }
    }


}
