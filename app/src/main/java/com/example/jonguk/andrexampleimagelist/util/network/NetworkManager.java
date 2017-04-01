package com.example.jonguk.andrexampleimagelist.util.network;

import com.example.jonguk.andrexampleimagelist.util.parser.GsonManager;
import com.example.jonguk.andrexampleimagelist.util.thread.ThreadHelper;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jonguk on 2017. 3. 28..
 */

public class NetworkManager {
    private static final String TAG = "NetworkManager";
    private static final String BASE_URL = "https://apis.daum.net/";

    private static Retrofit sInstance;

    private NetworkManager(){}

    public static Retrofit getInstance() {
        if (sInstance == null) {
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            client.addInterceptor(loggingInterceptor);

            sInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client.build())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(ThreadHelper.io()))
                    .addConverterFactory(GsonConverterFactory.create(GsonManager.getGson()))
                    .build();
        }
        return sInstance;
    }
}
