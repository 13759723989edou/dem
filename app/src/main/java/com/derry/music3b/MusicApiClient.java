package com.derry.music3b;
//进行网络请求的管理类
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MusicApiClient {
    private static final String BASE_URL = "http://8.222.172.78:3000/";

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
//定义音乐 API 请求的接口
    public interface MusicApiService {
        @GET("music/search")
        Call<MusicResponse> searchMusic(@Query("query") String query);
    }
    public static void searchMusic(String query, Callback<MusicResponse> callback) {
        MusicApiService musicApiService = getRetrofitInstance().create(MusicApiService.class);
        Call<MusicResponse> call = musicApiService.searchMusic(query);
        call.enqueue(callback);
    }

}




