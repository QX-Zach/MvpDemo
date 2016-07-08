package com.example.ozner.mvpdemo;

import android.app.Application;

import org.xutils.x;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ozner_67 on 2016/4/25.
 */
public class BaseApplication extends Application {
    private static ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        initAPI();
    }

    public void initAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://wechat.hoyofuwu.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public static ApiService getApiService() {
        return apiService;
    }
}
