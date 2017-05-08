package com.seth.elearning20.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.seth.elearning20.ListenPage;
import com.seth.elearning20.info.MusicInfo;
import com.seth.elearning20.info.MusicList;

import java.io.IOException;
import java.util.List;

/**
 * Created by Seth on 2017/5/2.
 */

public class MusicService extends Service {
    public MediaPlayer mMediaPlayer;
    public List<MusicInfo> mMusicInfos;
    public static final String MUSIC_PLAY = "music_play_key";
    public static boolean tag = false;
    private String Url = null;
    private int musicId;

    public static Intent newIntent(Context packageContext, int music_id, boolean isType){
        Intent intent = new Intent(packageContext, MusicService.class);
        intent.putExtra(MUSIC_PLAY, music_id);
        tag = isType;
        return intent;
    }

    public MusicService(){
    }
    //通过 Binder 来保持 Activity 和 Service 的通信
    public class MyBinder extends Binder {
        public MusicService getService(){
            return MusicService.this;
        }

        public void nextMusic(){
            musicId++;
            if(musicId == mMusicInfos.size())
                musicId = 0;
            if(mMediaPlayer != null){
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
            try {
                mMediaPlayer = new MediaPlayer();
                Url = mMusicInfos.get(musicId).getUrl();
                mMediaPlayer.setDataSource(Url);
                mMediaPlayer.prepare();
                mMediaPlayer.setLooping(true);
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void lastMusic(){
            musicId--;
            if(musicId < 0)
            musicId = mMusicInfos.size()-1;
            if(mMediaPlayer != null){
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
            try {
                mMediaPlayer = new MediaPlayer();
                Url = mMusicInfos.get(musicId).getUrl();
                mMediaPlayer.setDataSource(Url);
                mMediaPlayer.prepare();
                mMediaPlayer.setLooping(true);
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        if(!tag)
            mMusicInfos = MusicList.get().getMusicInfos();
        else
            mMusicInfos = MusicList.get().getmRadioInfos();
        String url = Url;
        musicId = intent.getIntExtra(MUSIC_PLAY,0);
        Url = mMusicInfos.get(musicId).getUrl();
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
