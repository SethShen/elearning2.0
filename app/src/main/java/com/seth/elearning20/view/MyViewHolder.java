package com.seth.elearning20.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seth.elearning20.R;
import com.seth.elearning20.info.MusicInfo;

/**
 * Created by Seth on 2017/5/10.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    private TextView musicTitle;
    private TextView musicAuthor;
    private TextView musicalbum;
    private TextView musiclikes;
    private TextView deletItem;
    public LinearLayout layout;
    private MusicInfo mMusicInfo;
    private int itemClicked;

    public MyViewHolder(View itemView) {
        super(itemView);
        musicTitle = (TextView) itemView.findViewById(R.id.music_title);
        musicAuthor = (TextView) itemView.findViewById(R.id.author);
        musicalbum = (TextView) itemView.findViewById(R.id.album_name);
        musiclikes = (TextView) itemView.findViewById(R.id.num_like);
        deletItem = (TextView) itemView.findViewById(R.id.item_delete);
        layout = (LinearLayout) itemView.findViewById(R.id.item_layout);
    }

        public void bindMusic(MusicInfo music, int position){
            mMusicInfo = music;
            itemClicked = position;
            musicTitle.setText(mMusicInfo.getTitle());
            musicAuthor.setText(mMusicInfo.getArtist());
            musicalbum.setText("("+mMusicInfo.getAlbum()+")");
            musiclikes.setText(""+0);

        }
}
