package com.derry.music3b;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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
    List<LocalMusicBean>musicBeanList;
    private LocalMusicAdapter adapter;

//记录当前正在播放的音乐位置
    int currnetPlayPosition=-1;
//记录暂停音乐时进度条的位置
    int currentPausePositionInSong = 0;
    MediaPlayer mediaPlayer;
    private LocalMusicBean v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mediaPlayer = new MediaPlayer();
        List<LocalMusicBean>musicBeanList= new ArrayList<>();
//通过创建适配器并将音乐列表数据传递给适配器的构造函数，然后将适配器设置给 RecyclerView，适配器会自动根据数据源来绑定每个项的视图，
//从而在 RecyclerView 中显示音乐列表的数据。
        //创建Recyclerview
        RecyclerView MusicListView=findViewById(R.id.MusicListView);
        //创建音乐列表数据
        musicBeanList=new ArrayList<>();
        //添加音乐数据到列表

        //创建适配器并设置给RecyclerView
        adapter =new LocalMusicAdapter(musicBeanList);
        MusicListView.setAdapter(adapter);
////创建适配器
//        adapter = new LocalMusicAdapter(this, musicBeanList);
//        //musicRv.setAdapter(adapter);
////设置布局管理器
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);//vertical垂直滑动
//        musicRv.setLayoutManager(layoutManager);
// //加载本地数据源
//        localMusicList();
// 设置每一项的点击事件
        setEventListener();

//跳转到下一个界面

        TextView textView=findViewById(R.id.local_music_bottom_tv_song);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//在点击事件中实现跳转到下一个界面的逻辑
                Intent intent=new Intent(MainActivity.this,MusicActivity.class);
                startActivity(intent);
            }
        });



    }
private void initialize(){
    View localMusic = findViewById(R.id.localMusic);
    View httpMusic = findViewById(R.id.httpMusic);
//    localMusic.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent intent=new Intent(MainActivity.this,SongListActivity.class);
//            startActivity(intent);
//        }
//    });
    httpMusic.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent1=new Intent(MainActivity.this,HttpActivity.class);
            startActivity(intent1);
        }
    });
}
    private void initView() {
        /*初始化控件的函数*/
        nextIv=findViewById(R.id.local_music_bottom_iv_next);
        playIv=findViewById(R.id.local_music_bottom_iv_play);
        lastIv=findViewById(R.id.local_music_bottom_iv_last);
        singerTv=findViewById(R.id.local_music_bottom_tv_singer);
        songTv=findViewById(R.id.local_music_bottom_tv_song);
        //musicRv=findViewById(R.id.local_music_rv);
        nextIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        int id=view.getId();
        if (id==R.id.local_music_bottom_iv_last){
            if (currnetPlayPosition ==0) {
                Toast.makeText(this,"已经是第一首了，没有上一曲！",Toast.LENGTH_SHORT).show();
                return;
            }
            currnetPlayPosition = currnetPlayPosition-1;
            LocalMusicBean lastBean = musicBeanList.get(currnetPlayPosition);
            playMusicInMusicBean(lastBean);
        }
        if (id==R.id.local_music_bottom_iv_next) {
            if (currnetPlayPosition ==musicBeanList.size()-1) {
                    Toast.makeText(this,"已经是最后一首了，没有下一曲！",Toast.LENGTH_SHORT).show();
                    return;
                }
                currnetPlayPosition = currnetPlayPosition+1;
                LocalMusicBean nextBean = musicBeanList.get(currnetPlayPosition);
                playMusicInMusicBean(nextBean);
        }
        if (id==R.id.local_music_bottom_iv_play){
            if (currnetPlayPosition == -1) {
//            并没有选中要播放的音乐
                    Toast.makeText(this, "请选择想要播放的音乐", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mediaPlayer.isPlaying()) {
//          此时处于播放状态，需要暂停音乐
                    pauseMusic();
                }else {
//             此时没有播放音乐，点击开始播放音乐
                    playMusic();
                }
        }
    }


    private void setEventListener() {
        /* 设置每一项的点击事件*/
        adapter.setOnItemClickListener(new LocalMusicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                currnetPlayPosition = position;
                LocalMusicBean musicBean = musicBeanList.get(position);
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
            //Log.i("lsh123", "playMusicInMusicBean: albumpath=="+albumArt);
            //Bitmap bm = BitmapFactory.decodeFile(albumArt);
            //Log.i("lsh123", "playMusicInMusicBean: bm=="+bm);
            //albumIv.setImageBitmap(bm);
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
            playIv.setImageResource(R.drawable.baseline_pause_24);
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


    private List<LocalMusicBean> localMusicList() {
        List<LocalMusicBean>musicBeansList= new ArrayList<>();
        ContentResolver contentResolver=getContentResolver();
        Cursor cursor=null;//Cursor 是一种用于遍历查询结果集的接口
        try{
            cursor=contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null
            ,null,null);
            if (cursor!=null&&cursor.moveToFirst()){
                int titleColumn=cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int artistColumn=cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                do {
                    String title=cursor.getString(titleColumn);
                    String artist=cursor.getString(artistColumn);
                    LocalMusicBean localMusicBean=new LocalMusicBean(title,artist);
                    musicBeanList.add(localMusicBean);
                }while (cursor.moveToNext());//cursor.moveToNext()用于检查是否还有下一行数据可供遍历
            }
        }catch (Exception e){
            Log.e("localMusicList","Error retrieving music data:"+e.getMessage());

        }finally {
            if (cursor!=null){
                cursor.close();
            }
        }
        return musicBeanList;
//        /*加载本地存储当中的音乐mp3文件到集合当中*/
//        //1、获取ContentResolver对象
//        ContentResolver contentResolver = getContentResolver();
//        //2、获取本地音乐存储的uri地址
//        Uri uri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
//
//        //3、开始查询地址
//        Cursor cursor = contentResolver.query(uri, null, null, null, null);
//
//
////        public List<LocalMusicBean> retrieveSong() {
//        List<LocalMusicBean> song = new ArrayList<>();
//
//        Uri songUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
//        String[] projection = {
//                MediaStore.Audio.Media._ID,
//                MediaStore.Audio.Media.DISPLAY_NAME,
//                MediaStore.Audio.Media.ARTIST,
//                MediaStore.Audio.Media.DURATION,
//                MediaStore.Audio.Media.DATA,
//                MediaStore.Audio.Media.ALBUM_ID
//        };
//
//        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
//        String sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";
//
//        Cursor cursor1 = getContentResolver().query(songUri, projection, selection, null, sortOrder);
//
//        if (cursor != null) {
//            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
//            int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
//            int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
//            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
//            int albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
//            int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
//
//            //4、遍历Cursor
//            int id = 0;
//            while (cursor.moveToNext()) {
//                String title = cursor.getString(titleColumn);
//                String artist = cursor.getString(artistColumn);
//                String album = cursor.getString(albumIdColumn);
//                id++;
//                String sid = String.valueOf(id);
//                String data = cursor.getString(dataColumn);
//                long duration = cursor.getLong(durationColumn);
//                SimpleDateFormat sdf = new SimpleDateFormat("mm;ss");
//                String time = sdf.format(new Date(duration));
//                //将一行当中的数据封装到对象当中
//                new LocalMusicBean(sid, title, artist, time, data);
//                mDatas.add(new LocalMusicBean(sid, title, artist, time, data));
//                //val song= Song(title,artist,id,duration,albumId)
//
//            }
//        }
//        //数据源变化，提示适配器更新
//        adapter.notifyDataSetChanged();
    }
}