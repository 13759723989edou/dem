package com.derry.music3b;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//MyDatabaseHelper 用于创建数据库和表，而 MyDatabase 用于执行具体的数据库操作，如插入音乐信息、查询音乐列表等
public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "music_database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "music_table";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_ARTIST = "artist";
    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_ARTIST + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
