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
         Context context;

     List<LocalMusicBean>song;

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;//setOnItemClickListener 传递接口
        Log.d("Music3b","开始检索本地音乐");
    }

    public interface OnItemClickListener{
        public void OnItemClick(View view,int position);//OnItemClick 调用接口
    }
    public LocalMusicAdapter(Context context, List<LocalMusicBean> mDatas) {
        this.context = context;
        this.song = mDatas;
    }
    @NonNull
    @Override
    public LocalMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_local_music,parent,false);
        return new LocalMusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalMusicViewHolder holder, @SuppressLint("RecyclerView") int position) {
        LocalMusicBean musicBean = song.get(position);
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

    @Override
    public int getItemCount() {
        return song.size();
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




