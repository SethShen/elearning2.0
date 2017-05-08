package com.seth.elearning20.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seth.elearning20.ListenPage;
import com.seth.elearning20.R;
import com.seth.elearning20.info.MusicInfo;

import java.util.List;

/**
 * Created by Seth on 2017/5/1.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder>{

    private Context mContext;
    private List<MusicInfo> mMusicInfos;
    private int musicIndex;

    public MusicAdapter(List<MusicInfo> musicInfos, Context context){
        mContext = context;
        mMusicInfos = musicInfos;
    }


    @Override
    public MusicAdapter.MusicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.music_list,parent,false);

        return new MusicAdapter.MusicHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicAdapter.MusicHolder holder, int position) {
        MusicInfo  musicInfo = mMusicInfos.get(position);
        holder.bindMusic(musicInfo,position);
    }

    @Override
    public int getItemCount() {
        return mMusicInfos.size();
    }

    public static String formatTime(long time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }


    public class MusicHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView musicTitle;
        private TextView musicAuthor;
        private TextView musicalbum;
        private TextView musiclikes;
        private MusicInfo mMusicInfo;

        private int itemClicked;

        public MusicHolder(View itemView) {
            super(itemView);

            musicTitle = (TextView) itemView.findViewById(R.id.music_title);
            musicAuthor = (TextView) itemView.findViewById(R.id.author);
            musicalbum = (TextView) itemView.findViewById(R.id.album_name);
            musiclikes = (TextView) itemView.findViewById(R.id.num_like);
            itemView.setOnClickListener(this);
        }
        public void bindMusic(MusicInfo music, int position){
            mMusicInfo = music;
            itemClicked = position;
            musicTitle.setText(mMusicInfo.getTitle());
            musicAuthor.setText(mMusicInfo.getArtist());
            musicalbum.setText("("+mMusicInfo.getAlbum()+")");
            musiclikes.setText(""+0);

        }

        @Override
        public void onClick(View v) {
            musicIndex = itemClicked;
            Intent intent = ListenPage.newIntent(mContext, itemClicked,mMusicInfo.getUrl());
            mContext.startActivity(intent);
        }
    }
}
