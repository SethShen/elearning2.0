package com.seth.elearning20.utils;


import android.content.Context;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;

import com.seth.elearning20.info.UserInfo;

import java.io.File;
import java.io.IOException;

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

    public AudioRecoderUtils(Context context) {
        userinfo = UserInfo.getUserInfo(context);

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

