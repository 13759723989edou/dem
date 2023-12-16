package com.derry.music3b;

public class LocalMusicBean {
    private String id;//歌曲id
    private String song;
    private String singer;
    private String album;
    private int duration;//歌曲时长
    private String path;//歌曲路径

    public LocalMusicBean() {
        this.id = id;
        this.song = song;
        this.singer = singer;
        this.album = album;
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

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
