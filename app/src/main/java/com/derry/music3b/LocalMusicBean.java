package com.derry.music3b;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
public class LocalMusicBean {
    private String id;//歌曲id
    private String song;
    private String singer;
    private String album;
    private int duration;//歌曲时长
    private String path;//歌曲路径
//通过在构造函数中执行数据库插入操作，将歌曲名和歌手名插入到名为 "music_table" 的表中
    public LocalMusicBean(Context context, String song, String singer) {
        this.song = song;
        this.singer = singer;
        // Initialize the database helper and get the writable database
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Perform any necessary database operations here
        ContentValues values = new ContentValues();
        values.put("song", song);
        values.put("singer", singer);
        db.insert("music_table", null, values);
        // Close the database
        db.close();
    }
    public LocalMusicBean(String song, String singer) {
        this.id = id;
        this.song = song;
        this.singer = singer;
        //this.album = album;
        this.duration = duration;
        this.path = path;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

//    public String getAlbum() {
//        return album;
//    }
//
//    public void setAlbum(String album) {
//        this.album = album;
//    }

    public int  getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPath() {

        return path;
    }

    public void setPath(String path) {

        this.path = path;

    }

}
