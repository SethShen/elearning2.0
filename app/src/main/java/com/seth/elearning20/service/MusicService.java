package com.seth.elearning20.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Seth on 2017/5/2.
 */

public class MusicService extends Service {
    public MediaPlayer mMediaPlayer;
    public boolean tag = false;
    private String Url = null;

    public MusicService(){
    }
    //通过 Binder 来保持 Activity 和 Service 的通信
    public class MyBinder extends Binder {
        public MusicService getService(){
            return MusicService.this;
        }
    }
    public MyBinder binder = new MyBinder();


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MusicService","onCreat");
    }

    @Override
    public boolean onUnbind(Intent intent){
        return  super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MusicService","onStartCommand");
        String url = Url;
        Url = intent.getStringExtra("player");
        if(!Url.equals(url)){
                if(mMediaPlayer != null){
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                }
                try {
                    mMediaPlayer = new MediaPlayer();

                    mMediaPlayer.setDataSource(Url);
                    mMediaPlayer.prepare();
                    mMediaPlayer.setLooping(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
