package com.example.ozner.mvpdemo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by ozner_67 on 2016/5/23.
 */
public interface ApiService {
    @POST("FamilyAccount/AppLogin")
    public Call<String> login(@Part("phone") String phone, @Part("password") String password);

    @GET("http://www.baidu.com")
    public Call<String> getBaiDu();
}
