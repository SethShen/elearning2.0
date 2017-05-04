package com.seth.elearning20.sqlite;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.util.Log;

import com.seth.elearning20.info.MusicInfo;
import com.seth.elearning20.info.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seth on 2017/5/3.
 */

public class SqlDao {
    private MusicSQLiteOpenHelper mySqliteOpenHelper;

    public SqlDao(Context context) {
        //创建一个帮助类对象
        mySqliteOpenHelper = new MusicSQLiteOpenHelper(context);
    }
/*******************向数据库中添加用户***************************/
    public boolean addUsrInfo(UserInfo bean){
        //执行sql语句需要sqliteDatabase对象
        //调用getReadableDatabase方法,来初始化数据库的创建
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();


        values.put("NAME", bean.getName());
        values.put("PHONE", bean.getPhone());
        values.put("PASSWORD", bean.getPassword());
        values.put("EMAIL", bean.getEmail());
        values.put("FROG_URL", bean.getFrogUrl());
        long result = db.insert("usr", null, values);
        //底层是在拼装sql语句
        //关闭数据库对象
        db.close();
        if (result != -1) {//-1代表添加失败
            return true;
        } else {
            return false;
        }
    }
/*******************向数据库中添加音频***************************/
    public boolean addMusicInfo(MusicInfo bean) {
        //执行sql语句需要sqliteDatabase对象
        //调用getReadableDatabase方法,来初始化数据库的创建
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        //是用map封装的对象，用来存放值
        values.put("_ID", bean.getId());
        values.put("TITLE", bean.getTitle());
        values.put("ALBUM", bean.getAlbum());
        values.put("DISPLAY_NAME", bean.getDisplayName());
        values.put("ARTIST", bean.getArtist());
        values.put("DURATION", bean.getDuration());
        values.put("SIZE", bean.getSize());
        values.put("URL", bean.getUrl());
        values.put("ALBUM_ID", bean.getAlbumId());
        values.put("ISLIKE", bean.islike());
        values.put("LIKENUM", bean.getLikenum());


        long result = db.insert("music", null, values);
        //底层是在拼装sql语句
        //关闭数据库对象
        db.close();
        if (result != -1) {//-1代表添加失败
            return true;
        } else {
            return false;
        }
    }
/*****************删除用户名对应用户记录*************************/
public int delUsr() {
    //执行sql语句需要sqliteDatabase对象
    //调用getReadableDatabase方法,来初始化数据库的创建
    SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
    int result = db.delete("usr", null, null);
    //关闭数据库对象
    db.close();

    return result;
}
/***********************查询用户名对应密码**************************/
    public List<String> usrQuery(){

        //执行sql语句需要sqliteDatabase对象
        //调用getReadableDatabase方法,来初始化数据库的创建
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();

        Cursor cursor = db.query("usr", new String[]{"NAME","PASSWORD"}, null, null, null, null,null);
        List<String> password = new ArrayList<>();
        //解析Cursor中的数据
        if(cursor != null && cursor.getCount() >0){//判断cursor中是否存在数据
            cursor.moveToFirst();
            password.add(cursor.getString(0));
            password.add(cursor.getString(1));
            cursor.close();//关闭结果集
        }else{
            password = null;
        }
        //关闭数据库对象
        db.close();
        return password;
    }

/************************删除id对应音频资源***************************/
    public int del(String music_id) {
        //执行sql语句需要sqliteDatabase对象
        //调用getReadableDatabase方法,来初始化数据库的创建
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        int result = db.delete("music", "_ID= ?", new String[]{music_id});
        //关闭数据库对象
        db.close();

        return result;
    }
/**************修改音频喜欢以及喜欢数************************/
    public int update(MusicInfo bean) {

        //执行sql语句需要sqliteDatabase对象
        //调用getReadableDatabase方法,来初始化数据库的创建
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();

        ContentValues values = new ContentValues();//是用map封装的对象，用来存放值
        //是用map封装的对象，用来存放值
        values.put("ISLIKE", bean.islike());
        values.put("LIKENUM", bean.getLikenum());

        int result = db.update("music", values, "_ID = ?", new String[]{bean.getId() + ""});
        //关闭数据库对象
        db.close();
        return result;
    }
/*******************查找elearning数据库全部音频***********************/
    public List<MusicInfo> localQuery(){
        List<MusicInfo> mMusicInfos = new ArrayList<>();
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();

        Cursor cursor = db.query("music",null,null,null,null,null,null);
        Log.i("SQLite", ""+cursor.getCount());
        for (int i = 0; i < cursor.getCount()&&cursor.getCount()>0; ++i){
            MusicInfo musicInfo = new MusicInfo();
            cursor.moveToNext();
            String title = cursor.getString(cursor.getColumnIndex("TITLE"));
            String album = cursor.getString(cursor.getColumnIndex("ALBUM"));
            String displayName = cursor.getString(cursor.getColumnIndex("DISPLAY_NAME"));
            String artist = cursor.getString(cursor.getColumnIndex("ARTIST"));
            long duration = cursor.getLong(cursor.getColumnIndex("DURATION"));
            long size = cursor.getLong(cursor.getColumnIndex("SIZE"));
            String url = cursor.getString(cursor.getColumnIndex("URL"));
            long albumId = cursor.getLong(cursor.getColumnIndex("ALBUM_ID"));
            int islike =  cursor.getInt(cursor.getColumnIndex("ISLIKE"));
            int likenum = cursor.getInt(cursor.getColumnIndex("LIKENUM"));
            long id = cursor.getLong(cursor.getColumnIndex("_ID"));

            musicInfo.setTitle(title);
            musicInfo.setId(id);
            musicInfo.setAlbum(album);
            musicInfo.setArtist(artist);
            musicInfo.setSize(size);
            musicInfo.setDisplayName(displayName);
            musicInfo.setDuration(duration);
            musicInfo.setUrl(url);
            musicInfo.setAlbumId(albumId);
            musicInfo.setIslike(islike);
            musicInfo.setLikenum(likenum);

            mMusicInfos.add(musicInfo);
        }
        cursor.close();
        return mMusicInfos;
    }
/*******************查找ContentProviders中记录的音频********************/
    public List<MusicInfo>  ContentProviderQuery(Context context){
        List<MusicInfo> mMusicInfos = new ArrayList<>();

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
        mCursor.close();
        return  mMusicInfos;
    }

}

//    public boolean query(User bean,Context mContext){
//        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
//        Cursor cursor = db.query("user",new String[]{ "password"},"account = ?",new String[]{bean.account},null, null, "_id desc");
//        if(cursor != null && cursor.getCount() > 0){
//            //循环遍历结果集，获取每一行的内容
//            String password = "";
//            while(cursor.moveToNext()){//条件，游标能否定位到下一行
//                //获取数据
//                password = cursor.getString(0);
//            }
//            cursor.close();//关闭结果集
//            if(password.equals(bean.password)) {
//                Toast.makeText(mContext, "验证成功", Toast.LENGTH_SHORT).show();
//                return true;
//            }else{
//                Toast.makeText(mContext,"密码错误", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        }else{
//            cursor.close();
//            Toast.makeText(mContext,"账号不存在！",Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        Cursor tet = mContext.getContentResolver().query()
//    }
//    public void query(String name){
//
//        //执行sql语句需要sqliteDatabase对象
//        //调用getReadableDatabase方法,来初始化数据库的创建
//        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
//
//        Cursor cursor = db.query("user", new String[]{"_id","account","password"}, "account = ?", new String[]{name}, null, null, "_id desc");
//        //解析Cursor中的数据
//        if(cursor != null && cursor.getCount() >0){//判断cursor中是否存在数据
//
//            //循环遍历结果集，获取每一行的内容
//            while(cursor.moveToNext()){//条件，游标能否定位到下一行
//                //获取数据
//                int id = cursor.getInt(0);
//                String name_str = cursor.getString(1);
//                String password = cursor.getString(2);
//                System.out.println("_id:"+id+";account:"+name_str+";password:"+password);
//            }
//            cursor.close();//关闭结果集
//
//        }
//        //关闭数据库对象
//        db.close();
//
//    }
