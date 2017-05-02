package com.seth.elearning20;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.seth.elearning20.adapter.MusicAdapter;
import com.seth.elearning20.info.MusicInfo;
import com.seth.elearning20.info.MusicList;
import com.seth.elearning20.service.MusicService;

import java.io.IOException;
import java.util.List;

/**
 * Created by Seth on 2017/4/27.
 */

public class ListenPage extends Activity {
    private static final String EXTRA_MUSIC_ID = "playerpage";
    private SeekBar mSeekBar;
    private boolean isPrepared;
    private boolean tag2 = false;
    private TextView mMusicTitle;
    private TextView mAllTimeTextView;
    private TextView mTimeTextView;
    private ImageView mPlayOrPause;
    private ImageView mNext;
    private ImageView mLast;
    private ImageView likes;
    private ImageView delete;
    private List<MusicInfo> mMusicInfos;
    private MusicInfo mMusicInfo;
    private MusicService mMusicService;
    private int position;

    public static Intent newIntent(Context packageContext, int music_id){
        Intent intent = new Intent(packageContext, ListenPage.class);
        intent.putExtra(EXTRA_MUSIC_ID, music_id);

        return intent;
    }

//    public static MusicPlayer newInstance(int position){
//        Bundle args = new Bundle();
//        args.putInt(ARG_MUSIC_ID, position);
//
//        MusicPlayer musicPlayer = new MusicPlayer();
//        musicPlayer.setArguments(args);
//        return musicPlayer;
//
//    }

// 在 Activity中调用 bindService 保持与Service 的通信
    private void bindServiceConnection(){
        Intent intent = MusicService.newIntent(ListenPage.this, position);
        startService(intent);
        bindService(intent,serviceConnection,this.BIND_AUTO_CREATE);
    }

    //回调onServiceConnection 函数，通过IBinder获取Service对象，实现Actirvice的绑定
    private ServiceConnection serviceConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service){
            mMusicService = ((MusicService.MyBinder)(service)).getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName name){
            Log.i("MusicService","Disconnected");
            mMusicService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Intent intent = getIntent();
        position = (int)intent.getSerializableExtra(EXTRA_MUSIC_ID);

        mMusicInfos = MusicList.get(this).getMusicInfos();
        mMusicInfo = mMusicInfos.get(position);
        FindViewById();
        bindServiceConnection();
        myListener();
        mAllTimeTextView.setText(MusicAdapter.formatTime(mMusicInfo.getDuration()).toString());
        mMusicTitle.setText(mMusicInfo.getTitle());                         //设置文章标题
        //mAllTimeTextView.setText(MusicAdapter.formatTime(mMusicService.mMediaPlayer.getDuration()));    //设置进度条时间

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mMusicService.mMediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    public Handler mHandler = new Handler();

    public Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mTimeTextView.setText(MusicAdapter.formatTime(mMusicService.mMediaPlayer.getCurrentPosition()));
            mSeekBar.setProgress(mMusicService.mMediaPlayer.getCurrentPosition());
            mSeekBar.setMax(mMusicService.mMediaPlayer.getDuration());
            mHandler.postDelayed(mRunnable,200);
            mAllTimeTextView.setText(MusicAdapter.formatTime(mMusicService.mMediaPlayer.getDuration()));    //设置进度条时间

        }
    };

    private void FindViewById() {
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mAllTimeTextView = (TextView) findViewById(R.id.total_time);
        mTimeTextView = (TextView) findViewById(R.id.now_time);
        mPlayOrPause = (ImageView) findViewById(R.id.pause);
        mNext = (ImageView) findViewById(R.id.next_music);
        mLast = (ImageView) findViewById(R.id.last_music);
        mMusicTitle = (TextView) findViewById(R.id.music_title);
        likes = (ImageView) findViewById(R.id.music_like);
        delete = (ImageView) findViewById(R.id.music_delete);
    }
    private void myListener() {
        ImageView imageView = (ImageView) findViewById(R.id.album);
        final ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360.0f);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);

        mPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPrepared == false) {
                    mSeekBar.setProgress(mMusicService.mMediaPlayer.getCurrentPosition());
                    mSeekBar.setMax(mMusicService.mMediaPlayer.getDuration());
                    isPrepared = true;
                }
                if (mMusicService.mMediaPlayer.isPlaying()) {
                    mMusicService.mMediaPlayer.pause();
                    animator.pause();
                    mPlayOrPause.setImageResource(R.drawable.ic_play_music);
                } else {
                    mMusicService.mMediaPlayer.start();
                    mPlayOrPause.setImageResource(R.drawable.ic_pause_music);
                    if(animator.isStarted())
                        animator.resume();
                    else
                        animator.start();
                }
                if(tag2 == false){
                    mHandler.post(mRunnable);
                    tag2 = true;
                }
            }

        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if(position==mMusicInfos.size()){
                    position = 0;
                }
                if(tag2 == false){
                    mHandler.post(mRunnable);
                    tag2 = true;
                }
                mMusicInfo = mMusicInfos.get(position);
                mMusicService.binder.nextMusic();
                mMusicTitle.setText(mMusicInfo.getTitle());
                animator.start();
                mPlayOrPause.setImageResource(R.drawable.ic_pause_music);
            }
        });

        mLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                if(position<0){
                    position = mMusicInfos.size()-1;
                }
                if(tag2 == false){
                    mHandler.post(mRunnable);
                    tag2 = true;
                }
                mMusicInfo = mMusicInfos.get(position);
                mMusicService.binder.lastMusic();
                mMusicTitle.setText(mMusicInfo.getTitle());
                animator.start();
                mPlayOrPause.setImageResource(R.drawable.ic_pause_music);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MusicService","ActivityDestory");
        unbindService(serviceConnection);
    }
}

