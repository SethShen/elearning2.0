package com.seth.elearning20.info;

/**
 * Created by Seth on 2017/5/1.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

public class MusicList extends FragmentActivity {
    private static MusicList sMusicList;
    private List<MusicInfo> mMusicInfos;

    public static MusicList get(Context context){
        if(sMusicList == null){
            sMusicList = new MusicList(context);
        }
        return sMusicList;
    }

    public static void MusicRemove(String Title){
        int index;

        for(index=0;index < sMusicList.getMusicInfos().size(); index ++){
            if(sMusicList.getMusicInfos().get(index).getTitle().equals(Title))
                break;
        }
        sMusicList.getMusicInfos().remove(index);

    }

    public MusicList(Context context){

        mMusicInfos = new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();
        Cursor mCursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ALBUM_ID}, null, null, null);

        for (int i = 0; i < mCursor.getCount(); ++i) {
            MusicInfo musicInfo = new MusicInfo();
            mCursor.moveToNext();
            long id = mCursor.getLong(0);
            String title = mCursor.getString(1);
            String album = mCursor.getString(2);
            String displayName = mCursor.getString(3);
            String artist = mCursor.getString(4);
            long duration = mCursor.getLong(5);
            long size = mCursor.getLong(6);
            String url = mCursor.getString(7);
            long albumId = mCursor.getLong(8);

            musicInfo.setTitle(title);
            musicInfo.setId(id);
            musicInfo.setAlbum(album);
            musicInfo.setArtist(artist);
            musicInfo.setSize(size);
            musicInfo.setDisplayName(displayName);
            musicInfo.setDuration(duration);
            musicInfo.setUrl(url);
            musicInfo.setAlbumId(albumId);

            mMusicInfos.add(musicInfo);
        }
    }

    //从数据库中获取歌曲列表
    public List<MusicInfo> getMusicInfos() {
        return mMusicInfos;
    }
}
