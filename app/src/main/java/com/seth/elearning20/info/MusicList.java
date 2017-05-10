package com.seth.elearning20.info;

/**
 * Created by Seth on 2017/5/1.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.seth.elearning20.sqlite.SqlDao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MusicList extends FragmentActivity {
    private String TAG = "SQLite";
    private static MusicList sMusicList;
    private static List<MusicInfo> mMusicInfos;
    private static List<MusicInfo> mRadioInfos;

    public static MusicList get(Context context){
        if(sMusicList == null){
            sMusicList = new MusicList(context);
        }
        return sMusicList;
    }
    public static MusicList get(){
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
        mRadioInfos = new ArrayList<>();
        /*获取本地数据库资源*/
        SqlDao sqlDao = new SqlDao(context);
        List<MusicInfo> MusicInfos = sqlDao.localQuery();
        //获取迭代器
        Iterator<MusicInfo> iterator1 = MusicInfos.iterator();
//        while (iterator1.hasNext()){
//            MusicInfo musicInfo = iterator1.next();
//            //将音乐和录音分开存取
//            if(musicInfo.getUrl().endsWith(".amr")){
//                mRadioInfos.add(musicInfo);
//            }else{
//                mMusicInfos.add(musicInfo);
//            }
//        }
        //如果本地资源不存在，从ContentProvider中获取
        if(mMusicInfos.size() == 0){
            MusicInfos = sqlDao.ContentProviderQuery(context);
            iterator1 = MusicInfos.iterator();
            //同样是将音乐和录音分开
            while(iterator1.hasNext()){
                MusicInfo musicInfo = iterator1.next();
                //sqlDao.addMusicInfo(musicInfo);
                if(musicInfo.getUrl().endsWith(".amr")){
                    Log.i(TAG,"添加"+musicInfo.getUrl());
                    Log.i(TAG,"添加"+ musicInfo.getTitle());
                    Log.i(TAG,"添加"+ musicInfo.getArtist());
                    Log.i(TAG,"添加"+ musicInfo.getDisplayName());
                    mRadioInfos.add(musicInfo);
                }else{
                    mMusicInfos.add(musicInfo);
                }
            }
            //iterator1 = MusicInfos.iterator();
            //while(iterator1.hasNext()){
                //boolean result = sqlDao.addMusicInfo((MusicInfo) iterator1.next());
                //Log.i(TAG,"添加"+ iterator1.next().getUrl());
           // }

        }

    }

    //从数据库中获取歌曲列表
    public List<MusicInfo> getMusicInfos() {
        return mMusicInfos;
    }

    public static List<MusicInfo> getmRadioInfos() {
        return mRadioInfos;
    }

    public static void setLike(int position) {
        mMusicInfos.get(position).setIslike(1);
        int i =mMusicInfos.get(position).getLikenum();
        mMusicInfos.get(position).setLikenum(i+1);
    }

    public static void setDisLike(int position) {
        mMusicInfos.get(position).setIslike(0);
        int i =mMusicInfos.get(position).getLikenum();
        mMusicInfos.get(position).setLikenum(i-1);
    }
}
//    ContentResolver contentResolver = context.getContentResolver();
//    Cursor mCursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//            new String[]{MediaStore.Audio.Media._ID,
//                    MediaStore.Audio.Media.TITLE,
//                    MediaStore.Audio.Media.ALBUM,
//                    MediaStore.Audio.Media.DISPLAY_NAME,
//                    MediaStore.Audio.Media.ARTIST,
//                    MediaStore.Audio.Media.DURATION,
//                    MediaStore.Audio.Media.SIZE,
//                    MediaStore.Audio.Media.DATA,
//                    MediaStore.Audio.Media.ALBUM_ID}, null, null, null);
//
//for (int i = 0; i < mCursor.getCount(); ++i) {
//        MusicInfo musicInfo = new MusicInfo();
//        mCursor.moveToNext();
//        long id = mCursor.getLong(0);
//        String title = mCursor.getString(1);
//        String album = mCursor.getString(2);
//        String displayName = mCursor.getString(3);
//        String artist = mCursor.getString(4);
//        long duration = mCursor.getLong(5);
//        long size = mCursor.getLong(6);
//        String url = mCursor.getString(7);
//        long albumId = mCursor.getLong(8);
//
//        musicInfo.setTitle(title);
//        musicInfo.setId(id);
//        musicInfo.setAlbum(album);
//        musicInfo.setArtist(artist);
//        musicInfo.setSize(size);
//        musicInfo.setDisplayName(displayName);
//        musicInfo.setDuration(duration);
//        musicInfo.setUrl(url);
//        musicInfo.setAlbumId(albumId);
//
//        mMusicInfos.add(musicInfo);
//        }