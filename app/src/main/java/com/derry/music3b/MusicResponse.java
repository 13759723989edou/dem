package com.derry.music3b;
//处理音乐响应的数据模型类
public class MusicResponse {

        private String song;
        private String artist;
        private String imageUrl;

        // 构造函数
        public MusicResponse(String song, String artist, String imageUrl) {
            this.song = song;
            this.artist = artist;
            this.imageUrl = imageUrl;
        }

        // 获取音乐标题
        public String getSong() {
            return song;
        }

        // 获取音乐艺术家
        public String getArtist() {
            return artist;
        }


        // 获取音乐封面图像的 URL
        public String getImageUrl() {
            return imageUrl;
        }

}
