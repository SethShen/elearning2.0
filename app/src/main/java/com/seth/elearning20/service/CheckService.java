package com.seth.elearning20.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.seth.elearning20.LanuchPage;
import com.seth.elearning20.info.UserInfo;
import com.seth.elearning20.login_regist.CompleteInfoPage;
import com.seth.elearning20.login_regist.LoginPage;
import com.seth.elearning20.utils.StreamUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by Seth on 2017/5/5.
 */

public class CheckService extends Service {
    private OkHttpClient client = new OkHttpClient();
    private FragmentActivity mActivity;
    private static UserInfo userInfo;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public CheckService(){
        if(userInfo==null)
           userInfo = UserInfo.getUserInfo();
    }



    public CheckService(FragmentActivity activity){
        mActivity = activity;
    }


    /**
     * 根据传入path调用SendGetCheck()请求网络
     * @param path
     */
    public void login(final String path, final int i) {
        final UserInfo user = userInfo;
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
                if(result&&userInfo.getFrogUrl()==null){
                    DownloadFrog();
                }
            }
        }).start();
    }


    public void uploadFrog(final File img, final UserInfo userInfo, final Context context) {


        //RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"),img);
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        RequestBody requestBody = multipartBuilder.type(MultipartBuilder.FORM)
                .addFormDataPart("name",UserInfo.getUserInfo().getName())
                .addFormDataPart("file","ss.jpg", RequestBody.create(MediaType.parse("application/octet-stream"),img)).build();

        Request.Builder builder = new Request.Builder();
        Request request = builder.url("http://115.159.71.92:8080/eLearningManager/user/uploadPic").post(requestBody).build();
        executeRequest(request);


    }

    private void executeRequest(Request request) {
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("okhttpRespond","failure");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.i("okhttpRespond","success");
                final String res = response.body().string();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity,res,Toast.LENGTH_SHORT).show();
                    }
                });
//                getApplicationContext().
//                Toast.makeText(getApplication(),res,Toast.LENGTH_SHORT);
                Log.i("okhttpRespond",res);
            }
        });
    }

    private void DownloadFrog() {

        String path = "http://115.159.71.92:8080/eLearningManager/user/getPic?name=";
        String usr= null;
        try {
            usr = URLEncoder.encode(userInfo.getName(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        path+= usr;
        Request.Builder builder = new Request.Builder();
        final Request request = builder
                .get()
                .url(path)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("okhttpRespond","failure");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.i("okhttpRespond","onResponse:");
                InputStream is = response.body().byteStream();

                int len =0;
                File dir = new File(Environment.getExternalStorageDirectory()+"/Frog",userInfo.getName()+".jpg");
                byte[] buf = new byte[128];
                FileOutputStream fos = new FileOutputStream(dir);

                while ((len=is.read(buf)) != -1){
                    fos.write(buf,0,len);
                }
                fos.flush();
                fos.close();
                is.close();
                Log.i("okhttpRespond","download Success!");
                userInfo.setFrogUrl(dir.getAbsolutePath());
            }
        });
    }

    public void updateOnlineMusic(String path){
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(path).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("updateMusic","Failure"+e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                //String result = response.body().string();
                InputStream is = response.body().byteStream();

                //InputStream inputStream = conn.getInputStream();
                String result = StreamUtils.streamToString(is);
                LanuchPage.setOnlinemusic(result);
                Log.i("updateMusic","success"+ result);
            }
        });
    }

    /****
     * 登陆请求网络并判断结果
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

            //设置超时时间
            conn.setConnectTimeout(2500);
            //请求方式
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            if(code == 200){
                InputStream inputStream = conn.getInputStream();
                String result = StreamUtils.streamToString(inputStream);
                Log.i("backnum",result);
                String strs[] = result.split(",");
                if(strs[0].equals("-1"))
                    flag = false;
                else {
                    flag = true;
                    if(strs[0].equals("1")){
                        UserInfo userInfo = UserInfo.getUserInfo();
                        userInfo.setPhone(strs[1]);
                        userInfo.setEmail(strs[2]);
                    }
                }
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

    public static void setUserInfo(UserInfo userInfo) {
        CheckService.userInfo = userInfo;
    }
}
