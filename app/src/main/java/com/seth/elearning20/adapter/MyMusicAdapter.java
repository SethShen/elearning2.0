package com.seth.elearning20.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seth.elearning20.R;
import com.seth.elearning20.info.MusicInfo;
import com.seth.elearning20.view.MyViewHolder;

import java.util.List;

/**
 * Created by Seth on 2017/5/10.
 */

public class MyMusicAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<MusicInfo> mMusicInfos;
    private int musicIndex;

    public MyMusicAdapter(List<MusicInfo> musicInfos, Context context){
        mContext = context;
        mMusicInfos = musicInfos;
        Log.i("MyRecyclerView",musicInfos.size()+"");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.music_list,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MusicInfo  musicInfo = mMusicInfos.get(position);
        MyViewHolder viewHolder = (MyViewHolder)holder;
        viewHolder.bindMusic(musicInfo,position);
    }

    @Override
    public int getItemCount() {
        return mMusicInfos.size();
    }

    public void removeItem(int position) {
        mMusicInfos.remove(position);
        notifyDataSetChanged();
    }
}
