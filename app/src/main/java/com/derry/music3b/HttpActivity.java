package com.derry.music3b;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HttpActivity extends AppCompatActivity {
    List<MusicResponse> musicList;
    private MusicResponse adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        musicList=new ArrayList<>();
        MusicApiClient.searchMusic("query", new Callback<MusicResponse>() {
            @Override
            public void onResponse(Call<MusicResponse> call, Response<MusicResponse> response) {
                if (response.isSuccessful()) {
                    MusicResponse musicResponse = response.body();// 处理音乐响应数据
                } else {
                    // 处理请求失败的情况
                }
            }

            @Override
            public void onFailure(Call<MusicResponse> call, Throwable t) {
                // 处理请求失败的情况
            }
        });
        TextView textView=findViewById(R.id.httpMusic);
        textView.setOnClickListener(new View.OnClickListener() {
          @Override
         public void onClick(View view){

           }
         });

    }


}






