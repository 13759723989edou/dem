package com.derry.music3b;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicAdapter.LocalMusicViewHolder> {
//适配器负责将音乐数据列表数据musicBeanList与RecyclerView中的每个项进行绑定
    Context context;

    private List<LocalMusicBean>musicBeanList;
    public LocalMusicAdapter(List<LocalMusicBean>musicBeanList){
        this.musicBeanList=musicBeanList;
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;//setOnItemClickListener 传递接口
        Log.d("Music3b","开始检索本地音乐");
    }

    public interface OnItemClickListener{
        public void OnItemClick(View view,int position);//OnItemClick 调用接口
    }
    public LocalMusicAdapter(Context context, List<LocalMusicBean> musicBeanList) {
        this.context = context;
        this.musicBeanList = musicBeanList;
    }
    @NonNull
    @Override
    public LocalMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_main,parent,false);
        return new LocalMusicViewHolder(view);
    }

    @Override

    public void onBindViewHolder(@NonNull LocalMusicViewHolder holder, int position) {
//绑定数据到ViewHolder的视图
        LocalMusicBean musicBean = musicBeanList.get(position);
        holder.idTv.setText(musicBean.getId());
        holder.songTv.setText(musicBean.getSong());
        holder.singerTv.setText(musicBean.getSinger());
        holder.albumTv.setText(musicBean.getAlbum());
        holder.timeTv.setText(musicBean.getDuration());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                onItemClickListener.OnItemClick(view,position);//接口对调
            }
        });
    }
//在 `onCreateViewHolder()` 方法中，你可以创建 RecyclerView 的每个项的视图，并将其封装到一个 ViewHolder 对象中返回。

    @Override
    public int getItemCount() {
//在使用 RecyclerView 和自定义适配器时，你需要重写 `getItemCount()` 方法来告诉 RecyclerView 有多少项需要显示。
//RecyclerView 在布局和滚动时，会根据这个返回值来确定需要创建和绑定的视图数量。
        return musicBeanList.size();
    }


    static class LocalMusicViewHolder extends RecyclerView.ViewHolder{
        TextView idTv,songTv,singerTv,albumTv,timeTv;
        public LocalMusicViewHolder(View itemView){
            super(itemView);
            idTv=itemView.findViewById(R.id.item_local_music_num);
            songTv=itemView.findViewById(R.id.item_local_music_song);
            singerTv=itemView.findViewById(R.id.item_local_music_singer);
            albumTv=itemView.findViewById(R.id.item_local_music_album);
            timeTv=itemView.findViewById(R.id.item_local_music_durtion);
            Log.d("Music3b","检索本地音乐完成");
        }
    }
}
//`LocalMusicAdapter` 是自定义的适配器类，它继承自 RecyclerView.Adapter，
// 并使用 ViewHolder 模式来管理每个项的视图。




