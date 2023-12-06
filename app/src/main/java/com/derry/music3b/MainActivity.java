package com.derry.music3b;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;
import android.widget.RelativeLayout;
import android.widget.Toast;


//public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//    //创建需要用到的控件的变量
//    private TextView tv1,tv2;
//    private FragmentManager fm;
//    private FragmentTransaction ft;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        //绑定控件
//        tv1=(TextView)findViewById(R.id.menu1);
//        tv2=(TextView)findViewById(R.id.menu2);
//        //设置监听器，固定写法
//        tv1.setOnClickListener(this);
//        tv2.setOnClickListener(this);
//        //若是继承FragmentActivity，fm=getFragmentManger();
//        fm=getSupportFragmentManager();
//        //fm可以理解为Fragment显示的管理者，ft就是它的改变者
//        ft=fm.beginTransaction();
//        //默认情况下就显示frag1
//        ft.replace(R.id.content,new frag1());
//        //提交改变的内容
//        ft.commit();
//    }
//    @Override
//    //控件的点击事件
//    public void onClick(View v){
//        ft=fm.beginTransaction();
//        //切换选项卡
//        switch (v.getId()){
//            case R.id.menu1:
//                ft.replace(R.id.content,new frag1());
//                break;
//            case R.id.menu2:
//                ft.replace(R.id.content,new frag2());
//                break;
//            default:
//                break;
//        }
//        ft.commit();
//    }
//}
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView nextIv,playIv,lastIv,albumIv;
    TextView singerTv,songTv;
    RecyclerView musicRv;
//数据源
    List<LocalMusicBean>mDatas;
    private LocalMusicAdapter adapter;

//记录当前正在播放的音乐位置
    int currnetPlayPosition=-1;
    //    记录暂停音乐时进度条的位置
    int currentPausePositionInSong = 0;
    MediaPlayer mediaPlayer;
    private LocalMusicBean v;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mediaPlayer=new MediaPlayer();
        mDatas=new ArrayList<>();
//创建适配器
        adapter = new LocalMusicAdapter(this, mDatas);
        musicRv.setAdapter(adapter);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);//vertical垂直滑动
        musicRv.setLayoutManager(layoutManager);
        //加载本地数据源
        loadLocalMusicData();
        //        设置每一项的点击事件
        setEventListener();
    }

    private void initView() {
        /*初始化控件的函数*/
        nextIv=findViewById(R.id.local_music_bottom_iv_next);
        playIv=findViewById(R.id.local_music_bottom_iv_play);
        lastIv=findViewById(R.id.local_music_bottom_iv_last);
        singerTv=findViewById(R.id.local_music_bottom_tv_singer);
        songTv=findViewById(R.id.local_music_bottom_tv_song);
        musicRv=findViewById(R.id.local_music_rv);
        nextIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
    }


    private void setEventListener() {
        /* 设置每一项的点击事件*/
        adapter.setOnItemClickListener(new LocalMusicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                currnetPlayPosition = position;
                LocalMusicBean musicBean = mDatas.get(position);
                playMusicInMusicBean(musicBean);
            }
        });
    }

    public void playMusicInMusicBean(LocalMusicBean musicBean) {
        /*根据传入对象播放音乐*/
        //设置底部显示的歌手名称和歌曲名
        singerTv.setText(musicBean.getSinger());
        songTv.setText(musicBean.getSong());
        stopMusic();
//                重置多媒体播放器
        mediaPlayer.reset();
//                设置新的播放路径
        try {
            mediaPlayer.setDataSource(musicBean.getPath());
            String albumArt = musicBean.getAlbumArt();
            Log.i("lsh123", "playMusicInMusicBean: albumpath=="+albumArt);
            Bitmap bm = BitmapFactory.decodeFile(albumArt);
            Log.i("lsh123", "playMusicInMusicBean: bm=="+bm);
            albumIv.setImageBitmap(bm);
            playMusic();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 点击播放按钮播放音乐，或者暂停从新播放
     * 播放音乐有两种情况：
     * 1.从暂停到播放
     * 2.从停止到播放
     * */
    private void playMusic() {
        /* 播放音乐的函数*/
        if (mediaPlayer!=null&&!mediaPlayer.isPlaying()) {
            if (currentPausePositionInSong == 0) {
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
//                从暂停到播放
                mediaPlayer.seekTo(currentPausePositionInSong);
                mediaPlayer.start();
            }
            playIv.setImageResource(R.mipmap.icon_pause);
        }
    }
    private void pauseMusic() {
        /* 暂停音乐的函数*/
        if (mediaPlayer!=null&&mediaPlayer.isPlaying()) {
            currentPausePositionInSong = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            playIv.setImageResource(R.mipmap.icon_play);
        }
    }
    private void stopMusic() {
        /* 停止音乐的函数*/
        if (mediaPlayer!=null) {
            currentPausePositionInSong = 0;
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.stop();
            playIv.setImageResource(R.mipmap.icon_play);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }


    private void loadLocalMusicData(){
        /*加载本地存储当中的音乐mp3文件到集合当中*/
        //1、获取ContentResolver对象
        ContentResolver resolver = getContentResolver();
        //2、获取本地音乐存储的uri地址
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        //3、开始查询地址
        Cursor cursor = resolver.query(uri, null, null, null, null);


        public List<LocalMusicBean>() {
            List<LocalMusicBean> song = new ArrayList<>();

            Uri songUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
            String[] projection = {
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.ALBUM_ID;
            }

            String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
            String sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";

            Cursor cursor1= getContentResolver().query(songUri, projection, selection, null, sortOrder);;
            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
                int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
                int albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
                int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);

                //4、遍历Cursor
        int id=0;
        while (cursor.moveToNext()) {
            String title = cursor.getString(titleColumn);
            String artist = cursor.getString(artistColumn);
            String album = cursor.getString(albumIdColumn);
            id++;
            String sid = String.valueOf(id);
            String data = cursor.getString(dataColumn);
            long duration = cursor.getLong(durationColumn);
            SimpleDateFormat sdf = new SimpleDateFormat("mm;ss");
            String time = sdf.format(new Date(duration));
            //将一行当中的数据封装到对象当中
            new LocalMusicBean(sid,title,artist,time,data);
            mDatas.add(new LocalMusicBean());
            //val song= Song(title,artist,id,duration,albumId)


        }
        //数据源变化，提示适配器更新
        adapter.notifyDataSetChanged();
    }
    private String getAlbumArt(String album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = this.getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + album_id),
                projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        cur = null;
        return album_art;
    }


    public void initView(){
        /*初始化控件的函数*/
        nextIv=findViewById(R.id.local_music_bottom_iv_next);
        playIv=findViewById(R.id.local_music_bottom_iv_play);
        lastIv=findViewById(R.id.local_music_bottom_iv_last);
        singerTv=findViewById(R.id.local_music_bottom_tv_singer);
        songTv=findViewById(R.id.local_music_bottom_tv_song);
        musicRv=findViewById(R.id.local_music_rv);
        nextIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.local_music_bottom_iv_last:
                if (currnetPlayPosition ==0) {
                    Toast.makeText(this,"已经是第一首了，没有上一曲！",Toast.LENGTH_SHORT).show();
                    return;
                }
                currnetPlayPosition = currnetPlayPosition-1;
                LocalMusicBean lastBean = mDatas.get(currnetPlayPosition);
                playMusicInMusicBean(lastBean);
                break;
            case R.id.local_music_bottom_iv_next:
                if (currnetPlayPosition ==mDatas.size()-1) {
                    Toast.makeText(this,"已经是最后一首了，没有下一曲！",Toast.LENGTH_SHORT).show();
                    return;
                }
                currnetPlayPosition = currnetPlayPosition+1;
                LocalMusicBean nextBean = mDatas.get(currnetPlayPosition);
                playMusicInMusicBean(nextBean);
                break;
            case R.id.local_music_bottom_iv_play:
                if (currnetPlayPosition == -1) {
//                    并没有选中要播放的音乐
                    Toast.makeText(this, "请选择想要播放的音乐", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mediaPlayer.isPlaying()) {
//                    此时处于播放状态，需要暂停音乐
                    pauseMusic();
                }else {
//                    此时没有播放音乐，点击开始播放音乐
                    playMusic();
                }
                break;
        }
    }
}