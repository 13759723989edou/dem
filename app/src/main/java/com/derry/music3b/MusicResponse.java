package com.derry.music3b;
//处理音乐响应的数据模型类
public class MusicResponse {

        private String title;
        private String artist;
        private String imageUrl;

        // 构造函数
        public MusicResponse(String title, String artist, String imageUrl) {
            this.title = title;
            this.artist = artist;
            this.imageUrl = imageUrl;
        }

        // 获取音乐标题
        public String getTitle() {
            return title;
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
