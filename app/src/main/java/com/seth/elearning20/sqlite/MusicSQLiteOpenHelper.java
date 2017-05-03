package com.seth.elearning20.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Seth on 2017/5/3.
 */

public class MusicSQLiteOpenHelper extends SQLiteOpenHelper {


    public MusicSQLiteOpenHelper(Context context) {

        super(context, "musicinfos.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table music (_ID bigint primary key,TITLE varchar(50),ALBUM varchar(50)," +
                "DISPLAY_NAME varchar(50), ARTIST varchar(50), DURATION bigint, SIZE bigint, " +
                "URL varchar(50), ALBUM_ID bigint, ISLIKE integer, LIKENUM integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
