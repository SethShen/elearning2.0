package com.seth.elearning20.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Seth on 2017/5/3.
 */

public class MusicSQLiteOpenHelper extends SQLiteOpenHelper {
    private static int version = 4;         //数据库版本

    public MusicSQLiteOpenHelper(Context context) {
        super(context, "musicinfos.db", null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table music (_ID bigint primary key,TITLE varchar(50),ALBUM varchar(50)," +
                "DISPLAY_NAME varchar(50), ARTIST varchar(50), DURATION bigint, SIZE bigint, " +
                "URL varchar(50), ALBUM_ID bigint, ISLIKE integer, LIKENUM integer)");
        db.execSQL("create table usr(NAME varchar(50) primary key, PHONE varchar(50)," +
                " PASSWORD varchar(50),EMAIL varchar(50), FROG_URL varchar(50),RECODER_NUM integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i("SQLite","更新数据库");
    }
}
