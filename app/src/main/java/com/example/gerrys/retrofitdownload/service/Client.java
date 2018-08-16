package com.example.gerrys.retrofitdownload.service;

import com.example.gerrys.retrofitdownload.Model.ResponseModel;
import com.example.gerrys.retrofitdownload.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Client {

    @POST("api/web/agent/login")
    Call<ResponseModel> createAccount(
            @Body User user
    );
}
