package com.example.gerrys.retrofitdownload.service;

import com.example.gerrys.retrofitdownload.Model.UploadImageResponse;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface FileDownloadClient {
    @GET("assets/upload/file/01026218000220370epolicy.pdf")
    Call<ResponseBody> downloadFile();
    @Multipart
    @POST("/api/web/policy/upload/{polisId}/{fieldName}")
    Call<UploadImageResponse> uploads(
            @Path("polisId") String policyId,
            @Path("fieldName") String fieldName,
            @Part MultipartBody.Part file
    );
}
