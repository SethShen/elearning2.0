package com.seth.elearning20.utils;


import android.content.Context;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.seth.elearning20.FrontPage;
import com.seth.elearning20.frontfragment.SpokenPage;
import com.seth.elearning20.info.MusicInfo;
import com.seth.elearning20.info.MusicList;
import com.seth.elearning20.info.UserInfo;
import com.seth.elearning20.sqlite.SqlDao;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Seth on 2017/5/8.
 */

public class AudioRecoderUtils {

    //文件路径
    private static String filePath;
    //文件夹
    private static File mFile;

    private static MediaRecorder mMediaRecorder;
    private final String Tag = "recoder";
    public static final int MAX_LENGTH = 1000 * 60 * 10;    //设置最大录音时长
    private static UserInfo userinfo;
    private Context mContext;
    private FragmentActivity mActivity;

    public AudioRecoderUtils(Context context,FragmentActivity activity) {
        mContext =context;
        mActivity = activity;
        userinfo = UserInfo.getUserInfo(mContext);

    }

    public void StartRecoder() {
        mMediaRecorder.start();
    }
    public void ResumeRecoder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mMediaRecorder.resume();
        }
    }

    public void PauseRecoder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mMediaRecorder.pause();
        }
    }

    public void saveRecoder() {
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;
        MediaScannerConnection.scanFile(mContext,new String[]{mFile.getAbsolutePath()},null,
                new MediaScannerConnection.OnScanCompletedListener(){
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });

        Runnable save = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    //获取最新媒体库*/
                    final SqlDao sqlDao = new SqlDao(mContext);
                    final List<MusicInfo> MusicInfos = sqlDao.ContentProviderQuery(mContext);
                    final Iterator<MusicInfo> iterator1 = MusicInfos.iterator();

                    MusicInfo musicInfo;
                    List<MusicInfo> infos;
                    /*将最新添加的录音添加到List和数据库中*/
                    while (iterator1.hasNext()){
                        musicInfo = iterator1.next();
                        String[] strs=mFile.getName().split("\\.");

                        Log.i("ExternalStorage",musicInfo.getTitle());
                        /*判断与新添加录音同名的文件*/
                        if(strs[0].equals(musicInfo.getTitle())){
                            infos = MusicList.getmRadioInfos();
                            final int infosize = infos.size();
                            Log.i("ExternalStorage",musicInfo.getTitle());
                            infos.add(0,musicInfo);
                            boolean result;
                            result = sqlDao.addMusicInfo(musicInfo);
                            Log.i("ExternalStorage",result + "");
                            /*在UI线程里更新RecyclerView*/
                            mActivity.runOnUiThread(new Runnable(){
                                @Override
                                public void run() {
                                    //更新UI
                                        Log.i("ExternalStorage","update" +  "");
                                        SpokenPage.radioAdapter.notifyItemInserted(infosize);
                                    }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                }
        };
        new Thread(save).start();


    }

    public void deleteRecoder() {
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;
        mFile.delete();
    }


    public static void InitFile() {
        /***********创建url*/
        File dir = new File(Environment.getExternalStorageDirectory() + "/elearning" + File.separator + userinfo.getPhone());
        if (!dir.exists()) {
            dir.mkdirs();
        }
//        File.createTempFile(,
//                ".amr", iRecAudioDir);
        mFile = new File(Environment.getExternalStorageDirectory() + "/elearning" + File.separator + userinfo.getPhone()+ File.separator + userinfo.getName() + "_" + userinfo.getRecoderNum() + ".amr");
        userinfo.setRecoderNum(userinfo.getRecoderNum() + 1);
        filePath = mFile.getAbsolutePath();
        /****************************************/

        try {
            mMediaRecorder = new MediaRecorder();
            // 麦克风源录音
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);

            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);

            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

            mMediaRecorder.setOutputFile(filePath);

            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

