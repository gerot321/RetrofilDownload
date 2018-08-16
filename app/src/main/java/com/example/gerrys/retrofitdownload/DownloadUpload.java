package com.example.gerrys.retrofitdownload;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gerrys.retrofitdownload.Model.UploadImageResponse;
import com.example.gerrys.retrofitdownload.Util.Preference;
import com.example.gerrys.retrofitdownload.service.FileDownloadClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownloadUpload extends AppCompatActivity {
    private static final int MY_PERMISSION_GRANTED = 100;
    private static final int ACTIVITY_START_CAMERA_APP =0;
    private File file;
    private Uri uri;
    private Button downloadButton,uploadButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloadupload);

        downloadButton = (Button) findViewById(R.id.Download);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadFile();
            }
        });

        uploadButton = (Button) findViewById(R.id.Upload);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            uploadButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        if(ContextCompat.checkSelfPermission(DownloadUpload.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DownloadUpload.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSION_GRANTED);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                uploadButton.setEnabled(true);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                uploadFile(uri);
            }
        }
    }
    private void uploadFile(Uri uri) {
        String path = getRealPathFromUri(uri,DownloadUpload.this);
        File files = new File(path);
        final EditText name = (EditText) findViewById(R.id.editText);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder().addInterceptor(interceptor);
        okhttpBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder newRequest = request.newBuilder()
                        .header("apiKey","xFebVVh3H9ypv9GAy7zg")
                        .header("Authorization", "Bearer " + Preference.getToken());
                return chain.proceed(newRequest.build()) ;
            }
        });
        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"),file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file",file.getName(),mFile);
        RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM,name.getText().toString());
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://mpm.stagingdevbox.com/")
                .client(okhttpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        FileDownloadClient uploadImage = retrofit.create(FileDownloadClient.class);
        Call<UploadImageResponse> fileUpload = uploadImage.uploads("409","imageIdentityCard",fileToUpload);
        fileUpload.enqueue(new Callback<UploadImageResponse>() {
            @Override
            public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                Toast.makeText(DownloadUpload.this, "Upload Success : "+response.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                Toast.makeText(DownloadUpload.this, "Upload Fail : "+t.toString(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private String getRealPathFromUri(Uri uri, DownloadUpload mainActivity) {
        Cursor cursor = mainActivity.getContentResolver().query(uri,null,null,null,null);
        if(cursor == null){
            return uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File   storageDir    = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        file         = new File(storageDir.getAbsolutePath() + "/my_picture.png");

        uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 100);
    }
    private void downloadFile() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder().addInterceptor(interceptor);
        okhttpBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder newRequest = request.newBuilder().header("apiKey","xFebVVh3H9ypv9GAy7zg");

                return chain.proceed(newRequest.build()) ;
            }
        });
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://mpm.stagingdevbox.com/")
                .client(okhttpBuilder.build());
        Retrofit retrofit = builder.build();
        FileDownloadClient fileDownloadClient =  retrofit.create(FileDownloadClient.class);
        Call<ResponseBody> call = fileDownloadClient.downloadFile();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                boolean success = writeResponseBodyToDisk(response.body());
                if(success){
                    Toast.makeText(DownloadUpload.this, "Download Success : "+ response.body().toString(), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(DownloadUpload.this, "Download Success : "+success, Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DownloadUpload.this, "Fail", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {

            File futureStudioIconFile = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    ,"tes.pdf");
            if(futureStudioIconFile.exists()){
                return false;
            }else{
                InputStream inputStream = null;
                OutputStream outputStream = null;

                try {
                    byte[] fileReader = new byte[4096];

                    long fileSize = body.contentLength();
                    long fileSizeDownloaded = 0;

                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(futureStudioIconFile);

                    while (true) {
                        int read = inputStream.read(fileReader);

                        if (read == -1) {
                            break;
                        }

                        outputStream.write(fileReader, 0, read);

                        fileSizeDownloaded += read;

                        Log.d("tes", "file download: " + fileSizeDownloaded + " of " + fileSize);
                    }

                    outputStream.flush();

                    return true;
                } catch (IOException e) {
                    return false;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            }

        } catch (IOException e) {
            return false;
        }
    }
}
