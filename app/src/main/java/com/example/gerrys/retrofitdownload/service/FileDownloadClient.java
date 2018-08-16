package com.example.gerrys.retrofitdownload.service;

import com.example.gerrys.retrofitdownload.Model.UploadImageResponse;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileDownloadClient {
    @GET("assets/upload/file/01026218000220370epolicy.pdf")
    Call<ResponseBody> downloadFile();
    @Multipart
    @POST("api/web/policy/upload/409/imageIdentityCard")
    Call<UploadImageResponse> uploadFile(
            @Part MultipartBody.Part file
            );
}
